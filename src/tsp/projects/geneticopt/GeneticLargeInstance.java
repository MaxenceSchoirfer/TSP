package tsp.projects.geneticopt;

import tsp.evaluation.Evaluation;
import tsp.output.LogFileOutput;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;
import tsp.projects.geneticopt.tools.Crossover;
import tsp.projects.geneticopt.tools.Greedy;
import tsp.projects.geneticopt.tools.Mutation;
import tsp.projects.geneticopt.tools.Selection;

public class GeneticLargeInstance extends Project {

    private final static double PROBABILITY_MUTATION = 0.8;
    private final static double PROBABILITY_CROSSOVER = 1;

    private final LogFileOutput output = new LogFileOutput("GeneticLargeInstance.txt");
    private int nGeneration = 0;
    private double startTime;


    private final int length;
    private final int[][] population;

    public GeneticLargeInstance(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.setMethodName("GeneticOpt");

        this.length = this.problem.getLength();
        this.population = new int[length /2][length];
    }


    @Override
    public void initialization() {
        this.startTime = System.currentTimeMillis();
        Greedy greedy = new Greedy(problem);
        this.startTime = System.currentTimeMillis();
        greedy.initialization();
        int[][] greedyPaths = greedy.getPaths();
        //sort greedy path to take better
        for (int i = 0; i < population.length; i++) {
            population[i] = greedyPaths[i];
        }
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {
        // -------------------------------------- DEBUG -----------------------------------------------------------------------------
        output.print("Génération : " + nGeneration + "\n" + "Optimum : " + this.evaluation.getBestEvaluation() + "\n");
        nGeneration++;


        int indexChildren = 0;
        int[][] children = new int[population.length][length];
        int[][] selectedParents = Selection.tournament(population, 10, 5, this.evaluation);


        // -------------------------------------- CROSSOVER -------------------------------------------------------------------------------------
        while (indexChildren < population.length - 1) {
            int parent1 = (int) (Math.random() * selectedParents.length);
            int parent2 = (int) (Math.random() * selectedParents.length);

            int[][] c;
            if (Math.random() < PROBABILITY_CROSSOVER)
                c = Crossover.crossoverOX1(selectedParents[parent1], selectedParents[parent2]);
            else c = Crossover.crossoverNone(selectedParents[parent1], selectedParents[parent2]);

            children[indexChildren] = c[0];
            children[indexChildren + 1] = c[1];
            indexChildren += 2;
        }

        //--------------------------------------------- MUTATION -----------------------------------------------------------------------------------

        int[][] mutatedChildren = new int[population.length][length];
        for (int i = 0; i < population.length; i++) {
            if (Math.random() < PROBABILITY_MUTATION)
                mutatedChildren[i] = Mutation.mutationIM(children[i]);
            else mutatedChildren[i] = children[i];
        }

        int[][] selectedChildren = Selection.tournament(mutatedChildren, 10, 5, this.evaluation);

        for (int i = 0; i < selectedParents.length; i++) {
            population[i] = selectedParents[i];
            population[i + selectedParents.length] = selectedChildren[i];
        }
    }

}
