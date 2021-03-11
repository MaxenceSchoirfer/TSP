package tsp.projects.greedy;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.LogFileOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.Arrays;

public class Greedy extends CompetitorProject {

    private int length;
    private int[] path;

    private int startIndex;

    private double startTime;

    boolean end = false;


    int[][] sortedCities;

    LogFileOutput output;

    public Greedy(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Greedy");

        output = new LogFileOutput("trace.txt");


    }

    @Override
    public void initialization() {
        this.startTime = System.currentTimeMillis();
        this.length = this.problem.getLength();
        startIndex = 0;
        sortCities();
        //greedy();
        greedyBis();
    }

    private void greedy() {
        path = new int[length];
        Arrays.fill(path, -1);

        //  Coordinates lastCity = this.problem.getCoordinates(startIndex);
        double[] lastCity = new double[2];
        lastCity[0] = problem.data[startIndex][0];
        lastCity[1] = problem.data[startIndex][1];


        path[0] = startIndex;

        //choix de la ville n° k
        for (int k = 1; k < length; k++) {
            int nextCity = -1;
            double distance = Double.MAX_VALUE;


            //parcours de toutes les villes
            loop:
            for (int i = 0; i < length; i++) {

                //si la ville n° i est déjà dans path on continue
                for (int j = 0; j < length; j++) {
                    if (i == path[j]) continue loop;
                }
//                for (Integer integer : path) {
//                    if (i == integer) continue loop;
//                }

//                double[] city = new double[2];
//                city[0] = this.problem.data[i][0];
//                city[1] = this.problem.data[i][1];
//
                double dx = this.problem.data[i][0] - lastCity[0];
                double dy = this.problem.data[i][1] - lastCity[1];
                double d = Math.sqrt(dx * dx + dy * dy);
                if (d < distance) {
                    distance = d;
                    nextCity = i;
                }


            }
            if (nextCity != -1) {
                path[k] = nextCity;
                lastCity[0] = this.problem.data[nextCity][0];
                lastCity[1] = this.problem.data[nextCity][1];
            }

        }

        // output.print(Arrays.toString(path));
    }


    private void greedyBis() {
        boolean has0 = startIndex == 0;
        path = new int[length];
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



    }


    @Override
    public void loop() {
        Path path = new Path(this.path);
        this.evaluation.evaluate(path);
        if (!end) output.print(this.evaluation.evaluate(path) + " : " + Arrays.toString(this.path) + "\n");
        startIndex++;
        if (startIndex == length) {
            end = true;
            output.print("Time : " + (System.currentTimeMillis() - startTime));
        }

        //  if (startIndex < length) greedy();
        if (startIndex < length) greedyBis();
//        for (int[] p:sortedCities) {
////            this.evaluation.evaluate(new Path(p));
//            output.print(this.evaluation.evaluate(new Path(p))+ " : " + Arrays.toString(p) + "\n");
//        }
    }

    private void sortCities() {
        int[] copyCities = new int[length];
        for (int i = 0; i < length; i++) {
            copyCities[i] = i;
        }

        sortedCities = new int[length][length - 1];
        for (int i = 0; i < length; i++) {
      //      output.print("Time(" + i + " : " + (System.currentTimeMillis() - startTime) + "\n");
            sortedCities[i] = getNearestNeighbours(i, copyCities.clone());
        }


    }

    private int[] getNearestNeighbours(int origin, int[] clone) {



       // int[] sortedCities = new int[length - 1];
        int[] sortedCities = new int[length];
        sortedCities[0] = origin;
        int indexSortedCities = 0;

        double distance;

        int indexNearestCity;

        for (int n = 1; n < length ; n++) {
            distance = Double.MAX_VALUE;
            indexNearestCity = -1;
            for (int i = 0; i < length; i++) {
           //     if (clone[i] != -1) {
                if (clone[i] != -1 && clone[i] != origin) {

                    double d = distance(problem.data[clone[i]][0],problem.data[clone[i]][1],problem.data[origin][0],problem.data[origin][1]);
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

    double distance (double x1, double y1, double x2,double y2){
        double dx = x2 - x1;
        double dy = y2 - y1;
       return Math.sqrt(dx * dx + dy * dy);

    }


}
