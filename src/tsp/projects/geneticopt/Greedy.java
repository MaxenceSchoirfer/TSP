package tsp.projects.geneticopt;

import tsp.evaluation.Problem;
import tsp.output.LogFileOutput;

public class Greedy {

    private LogFileOutput output;

    private Problem problem;

    private int[] path;
    private int length;


    private int[][] sortedCities;
    private int startIndex;

    private int[][] paths;

    private double startTime;

    boolean end = false;


    public Greedy(Problem problem) {
        this.problem = problem;
        this.output = new LogFileOutput("trace.txt");
        this.length = this.problem.getLength();
        this.paths = new int[length][length];
    }

    public void initialization() {
        this.startTime = System.currentTimeMillis();
        sortCities();
        for (int i = 0; i < length; i++){
            createPaths(i);
        }
    }

    private void createPaths(int startIndex) {
        boolean has0 = startIndex == 0;
        paths[startIndex][0] = startIndex;


        for (int i = 1; i < length; i++) {
            //on parcours les plus proches voisin de la dernière ville
            loop:
            for (Integer nearestNeighbourCity : sortedCities[paths[startIndex][i - 1]]) {
                if (!has0 && nearestNeighbourCity == 0) {
                    paths[startIndex][i] = nearestNeighbourCity;
                    has0 = true;
                    break;
                }
                //si le voisin n'est pas contenu dans path on l'ajoute
                for (Integer cityAlreadyOnPath : paths[startIndex]) {
                    if (nearestNeighbourCity.equals(cityAlreadyOnPath)) continue loop;
                }
                paths[startIndex][i] = nearestNeighbourCity;
                break;
            }
        }
    }


    private void sortCities() {
        int[] copyCities = new int[length];
        for (int i = 0; i < length; i++) {
            copyCities[i] = i;
        }

        this.sortedCities = new int[length][length];
        for (int i = 0; i < length; i++) {
            //output.print("Time(" + i + " : " + (System.currentTimeMillis() - startTime) + "\n");
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
                    double d = distance(problem.data[clone[i]][0], problem.data[clone[i]][1], problem.data[origin][0], problem.data[origin][1]);
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

    public int[][] getPaths() {
        return paths;
    }

}
