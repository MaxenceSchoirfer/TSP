package tsp.projects.greedy;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.ArrayList;

public class Greedy extends CompetitorProject {

    private int length;
    private  int[] path;

    public Greedy(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Greedy");
    }

    @Override
    public void initialization() {
        this.length = this.problem.getLength();
        path = new int[length];


        //   path[0] = 0;
        Coordinates lastCity = this.problem.getCoordinates(0);

        for (int k = 1; k < length ; k++) {
            int nextCity = -1;
            double distance = Double.MAX_VALUE;

            loop:
            for (int i = 1; i < length; i++) {

                for (Integer integer : path) {
                    if (i == integer) continue loop;
                }

                if (this.problem.getCoordinates(i).distance(lastCity) < distance) {
                    distance = this.problem.getCoordinates(i).distance(lastCity);
                    nextCity = i;
                }


            }
            if (nextCity != -1) path[k] = nextCity;
            lastCity = this.problem.getCoordinates(nextCity);


        }


        System.out.println("test");

    }

    @Override
    public void loop() {
        Path path = new Path (this.path);
        this.evaluation.evaluate (path);
    }


}
