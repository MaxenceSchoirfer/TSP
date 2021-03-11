package tsp.projects.greedy.farthest;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.LogFileOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.Arrays;

public class FarthestRandom extends CompetitorProject {


    private int length;
    private int[] path;

    private int startIndex;

    private double startTime;

    boolean end = false;

    LogFileOutput output;


    public FarthestRandom(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("FarthestRandom");
        output = new LogFileOutput("farthest.txt");
    }

    @Override
    public void initialization() {
        this.startTime = System.currentTimeMillis();
        this.length = this.problem.getLength();
        startIndex = 0;
        greedy();
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
        main:
        for (int k = 1; k < length; k++) {
            if (k % 2 == 0) {
                loop:
                for (int i = 0; i < length; i++) {
                    for (int j = 0; j < length; j++) {
                        if (i == path[j]) continue loop;
                    }
                    path[k] = i;
                    lastCity[0] = this.problem.data[i][0];
                    lastCity[1] = this.problem.data[i][1];
                    continue main;
                }
            }
            int nextCity = -1;
            double distance = 0;


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
                if (d > distance) {
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

        if (startIndex < length) greedy();
    }


}
