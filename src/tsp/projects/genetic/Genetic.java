package tsp.projects.genetic;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.LogFileOutput;
import tsp.output.OutputWriter;
import tsp.output.StandardOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Genetic extends CompetitorProject {

    private LogFileOutput output;


    private ArrayList<String> debug;

    private int populationSize;

    //  private int[][] population;


    private ArrayList<Path> population;

    private double probabilityMutation = 0.8;
    private double probabilityCrossover = 1;

    private double sartTime;


    private int length;

    private int nGeneration = 0;

    //   int[][] sortedCities;

    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        this.setMethodName("Genetic");
        this.length = this.problem.getLength();
        this.populationSize = length * 2;
        this.output = new LogFileOutput("genetic.txt");

        // this.population = new int[populationSize][length];


        debug = new ArrayList<>();
    }

    private int[] getGreedyPath() {
        int[] path = new int[length];
        int startIndex = 34;
        Coordinates lastCity = this.problem.getCoordinates(startIndex);
        for (int k = 0; k < length; k++) {
            if (k == startIndex) continue;
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
        return path;
    }

    @Override
    public void initialization() {
        sartTime = System.currentTimeMillis();
        Greedy greedy = new Greedy(problem);
     //   output.print("Start Greedy init\n");
        greedy.initialization();
     //   output.print("Start Greedy get paths\n");
        int[][] greedyPaths = greedy.getPaths();
        population = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            population.add(new Path(greedyPaths[i]));
            population.add(new Path(Mutation.mutationIM(greedyPaths[i])));

//            population.add(new Path(length));
//            population.add(new Path(length));

            // population[i] = greedyPaths[i];
            // population[length+i] = Mutation.mutationIM(greedyPaths[i]);
        }

        for (Path p:population) {
            output.print(this.evaluation.evaluate(p) + "\n");
        }

//        for (int i = 1; i < populationSize; i++) {
//            if (i % 2 == 0) population.add(new Path(Mutation.mutationIM(greedyPath)));
//            else population.add(new Path(length));
//           // population.add(new Path(length));
//        }


        //   population.add();
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {
     //   output.print("Génération : " + nGeneration + "\n");
        nGeneration++;
        //parents
        ArrayList<Path> childs = new ArrayList<>();

        //TO-DO add probability crossover
        //selection for the crossover
     //   output.print("SELECT PARENTS");
        ArrayList<Path> selectedParents = Selection.tournament(population, 10, 5, this.evaluation);
     //   output.print("CROSSOVER");
        while (childs.size() < populationSize) {
            int rIndex1 = (int) (Math.random() * selectedParents.size());
            int rIndex2 = (int) (Math.random() * selectedParents.size());
            int[][] c = Crossover.crossoverOX1(selectedParents.get(rIndex1).getPath(), selectedParents.get(rIndex2).getPath());
            childs.add(new Path(c[0]));
            childs.add(new Path(c[1]));
        }

        //mutation
     //   output.print("MUTATION");
        Iterator<Path> iterator = childs.iterator();
        ArrayList<Path> mutatedChilds = new ArrayList<>();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            double proba = Math.random();
            iterator.remove();
            if (proba < probabilityCrossover) {
                mutatedChilds.add(new Path(Mutation.mutationIM(path.getPath())));
            } else {
                mutatedChilds.add(path);
            }
        }

    //    output.print("SELECT CHILS");
        ArrayList<Path> selectedChilds = Selection.tournament(mutatedChilds, 10, 5, this.evaluation);
        population.clear();
        population.addAll(selectedParents);
        population.addAll(selectedChilds);
      //  output.print("EVALUATION");
        for (Path path : population) {
            this.evaluation.evaluate(path);
        }

      //  output.print("Optimum = " + this.evaluation.getBestEvaluation() + "\n");

       // ecrireFichier("debug.txt", debug);

        //mutation

        //selection (si on choisi de garder les parents ET les enfants)
        // ou selection
    }


    public static void ecrireFichier(String nomFichier, List<String> lignes) {
        Writer fluxSortie = null;
        try {
            fluxSortie = new PrintWriter(new BufferedWriter(new FileWriter(
                    nomFichier)));
            for (int i = 0; i < lignes.size() - 1; i++) {
                fluxSortie.write(lignes.get(i) + "\n");
            }
            fluxSortie.write(lignes.get(lignes.size() - 1));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        try {
            fluxSortie.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
