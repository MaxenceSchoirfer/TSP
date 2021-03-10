package tsp.projects.greedy.nearest;

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

    LogFileOutput output;

    public Greedy(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Greedy");

        output = new LogFileOutput("nearest.txt");
    }

    @Override
    public void initialization() {
        this.startTime =  System.currentTimeMillis();
        this.length = this.problem.getLength();
        startIndex = 0;
        greedy();
    }

    private void greedy() {
        path = new int[length];
        Coordinates lastCity = this.problem.getCoordinates(startIndex);

        path[0] = startIndex;

        //choix de la ville n° k
        for (int k = 1; k < length; k++) {
            //      if (k  == startIndex)continue;
            int nextCity = -1;
            double distance = Double.MAX_VALUE;


            //parcours de toutes les villes
            loop:
            for (int i = 0; i < length; i++) {


                //TO-DO break 0
                //si la ville n° i est déjà dans path on continue
                for (Integer integer : path) {
                    if (i == integer) continue loop;
                }

                if (this.problem.getCoordinates(i).distance(lastCity) < distance) {
                    distance = this.problem.getCoordinates(i).distance(lastCity);
                    nextCity = i;
                }


            }
            if (nextCity != -1) {
                path[k] = nextCity;
            }
            lastCity = this.problem.getCoordinates(nextCity);


        }

        // output.print(Arrays.toString(path));
    }

    @Override
    public void loop() {
        Path path = new Path(this.path);
        this.evaluation.evaluate(path);
        if (!end)output.print(this.evaluation.evaluate(path) + " : " + Arrays.toString(this.path) + "\n");
        startIndex++;
        if (startIndex == length){
            end=true;
            output.print("Time : " + (System.currentTimeMillis() - startTime));
        }

        if (startIndex < length) greedy();
    }


}
