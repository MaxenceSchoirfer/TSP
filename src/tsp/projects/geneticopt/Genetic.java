package tsp.projects.geneticopt;

import tsp.evaluation.Evaluation;
import tsp.output.LogFileOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;


public class Genetic extends CompetitorProject {

    private LogFileOutput output;


    private int populationSize;

    //  private int[][] population;


    //  private ArrayList<Path> population;

    private double probabilityMutation = 0.8;
    private double probabilityCrossover = 1;

    private double startTime;


    private int length;

    private int nGeneration = 0;


    private int[][] population;

    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.setMethodName("GeneticOpt");

        this.length = this.problem.getLength();
        this.populationSize = length * 2;
        this.output = new LogFileOutput("geneticOPT.txt");

    }


    @Override
    public void initialization() {
        startTime = System.currentTimeMillis();
        Greedy greedy = new Greedy(problem);
        greedy.initialization();
        int[][] greedyPaths = greedy.getPaths();

        population = new int[populationSize][length];

        for (int i = 0; i < length; i++) {
            population[i] = greedyPaths[i];
            population[length + i] = Mutation.mutationIM(greedyPaths[i]);
        }
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {
        output.print("Génération : " + nGeneration + "\n" );//+ "Optimum : " + this.evaluation.getBestEvaluation() + "\n");

        nGeneration++;

        int indexChildren = 0;
        int[][] children = new int[populationSize][length];
        int[][] selectedParents = Selection.tournament(population, 10, 5, this.evaluation);

        while (indexChildren < populationSize - 1) {
            int parent1 = (int) (Math.random() * selectedParents.length);
            int parent2 = (int) (Math.random() * selectedParents.length);
            int[][] c = Crossover.crossoverOX1(selectedParents[parent1], selectedParents[parent2]);
            children[indexChildren] = c[0];
            children[indexChildren + 1] = c[1];
            indexChildren += 2;
        }

        int[][] mutatedChildren = new int[populationSize][length];
        for (int i = 0; i < populationSize; i++) {
            if (Math.random() < probabilityMutation)
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
