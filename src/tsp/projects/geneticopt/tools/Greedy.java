package tsp.projects.geneticopt.tools;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.output.LogFileOutput;

import java.util.Arrays;

public class Greedy {

    private LogFileOutput output = new LogFileOutput("trace.txt");
    private double startTime;


    private final Problem problem;
    private final int length;


    private int[][] sortedCities;

    private final int[][] paths;

    private double[][] data;

    public Greedy(Problem problem) {
        this.problem = problem;
        this.data = problem.getData();
        this.length = this.problem.getLength();
        this.paths = new int[length][length];
    }

    public int[] getSinglePath(int startIndex) {
        int[] path = new int[length];
        Arrays.fill(path, -1);
        path[0] = startIndex;

        double[] lastCity = new double[2];
        lastCity[0] = data[startIndex][0];
        lastCity[1] = data[startIndex][1];


        //select city k
        for (int k = 1; k < length; k++) {
            int nextCity = -1;
            double distance = Double.MAX_VALUE;

            loop:
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (i == path[j]) continue loop;
                }

                double dx = this.data[i][0] - lastCity[0];
                double dy = this.data[i][1] - lastCity[1];
                double d = Math.sqrt(dx * dx + dy * dy);
                if (d < distance) {
                    distance = d;
                    nextCity = i;
                }
            }

            if (nextCity != -1) {
                path[k] = nextCity;
                lastCity[0] = this.data[nextCity][0];
                lastCity[1] = this.data[nextCity][1];
            }

        }
        return path;
    }

    public void initialization() {
        //this.startTime = System.currentTimeMillis();
        sortCities();
        for (int i = 0; i < length; i++) {
            output.print("Create path " + i + "\n");
            paths[i] = creatPath(i);
        }
    }

    private int[] creatPath(int startIndex) {
        boolean has0 = startIndex == 0;
        int[] path = new int[length];
        path[0] = startIndex;


        for (int i = 1; i < length; i++) {
            //on parcours les plus proches voisin de la dernière ville
            loop:
            for (Integer nearestNeighbourCity : sortedCities[path[i - 1]]) {
                if (!has0 && nearestNeighbourCity == 0) {
                    path[i] = nearestNeighbourCity;
                    has0 = true;
                    break;
                }
                //si le voisin n'est pas contenu dans path on l'ajoute
                for (Integer cityAlreadyOnPath : path) {
                    if (nearestNeighbourCity.equals(cityAlreadyOnPath)) continue loop;
                }
                path[i] = nearestNeighbourCity;
                break;
            }
        }
        return path;
    }

    private void sortCities() {
        int[] copyCities = new int[length];
        for (int i = 0; i < length; i++) {
            copyCities[i] = i;
        }

        this.sortedCities = new int[length][length];
        for (int i = 0; i < length; i++) {
            output.print("Sort city " + i + "\n");
            sortedCities[i] = getNearestNeighbours(i, copyCities.clone());
        }
    }

    private int[] getNearestNeighbours(int origin, int[] clone) {
        //int[] sortedCities = new int[length - 1];
        int[] sortedCities = new int[length];
        sortedCities[0] = origin;
        int indexSortedCities = 0;

        double distance;
        int indexNearestCity;

        for (int n = 0; n < length - 1; n++) {
            distance = Double.MAX_VALUE;
            indexNearestCity = -1;
            for (int i = 0; i < length; i++) {
                // if (clone[i] != -1 && clone[i] != origin) {
                if (clone[i] != -1) {
                    double d = distance(data[clone[i]][0], data[clone[i]][1], data[origin][0], data[origin][1]);
                    if (d < distance) {
                        distance = d;
                        indexNearestCity = i;
                    }
                }
            }
            sortedCities[indexSortedCities] = clone[indexNearestCity];
            indexSortedCities++;
            clone[indexNearestCity] = -1;
        }
        return sortedCities;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }


    public int[] getBestPath(Evaluation evaluation){
        int[] bestPath = paths[0];
        double bestEvalutation = Double.MAX_VALUE;
        for (int i = 0; i < problem.getLength(); i++) {
            if (evaluation.evaluate(new Path(paths[i])) < bestEvalutation){
                bestPath = paths[i];
                bestEvalutation = evaluation.getBestEvaluation();
            }
        }
        return bestPath;
    }

    public int[][] getPaths() {
        return paths;
    }

}
