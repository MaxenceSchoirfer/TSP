package tsp.projects.aco;

import tsp.evaluation.Evaluation;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

public class ACO extends CompetitorProject {

    private int size;


    //coefficient used to calculate the importance of pheromones (exploitation)
    private double alpha = 1;
    //coefficient used to calculate the importance of distances (exploration)
    private double beta = 1;

    private double[][] distances;
    private double[][] pheromones;
    private Ant[] ants;


    public ACO(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.setMethodName("ACO");
    }

    private void initMatrix() {
        distances = new double[size][size];
        pheromones = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                distances[i][j] = evaluation.getProblem().getCoordinates(i).distance(evaluation.getProblem().getCoordinates(j));
                pheromones[i][j] = 1;
            }
        }
    }

    @Override
    public void initialization() {
        System.out.println("hello");
        this.size = evaluation.getProblem().getLength();
        initMatrix();
        for (int i = 0; i < size; i++) {
            ants[i] = new Ant(i);
        }
    }

    @Override
    public void loop() {
        for (Ant ant : ants) {
            createSolution(ant);
  //          updatePheromone(ant);
        }
        //ants create theirs solution
        // update pheromone trails
        //allow deamon actions

    }

    private void createSolution(Ant ant) {
        //start at 1 because first city is fixed
        for (int i = 1; i < size; i++) {
            ant.path[i] = selectNextCity(ant.path[i - 1]);
        }
    }

    private int selectNextCity(int startCity) {
        double[] p = new double[size - 1];
        double total = 0;

        for (int j = 0; j < size; j++) {
            if (j == startCity) p[j] = 0;
            p[j] = Math.pow(pheromones[startCity][j], alpha) * Math.pow(1 / distances[startCity][j], beta);
            total += p[j];
        }

        for (int j = 0; j < size; j++) {
            p[j] /= total;
        }
        //

        return 0;
    }
}
