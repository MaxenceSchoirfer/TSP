package tsp.projects.genetic;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.OutputWriter;
import tsp.output.StandardOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Genetic extends CompetitorProject {

    private ArrayList<String> debug;

    private int populationSize;
    private ArrayList<Path> population;

    private double probabilityMutation = 0.8;
    private double probabilityCrossover = 1;


    private int length;


    public Genetic(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Maxence Schoirfer");
        //this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Genetic");
        this.populationSize = this.problem.getLength() * 10;
        // this.populationSize = 1000;

        debug = new ArrayList<>();
    }

    private int[] getGreedyPath() {
        int[] path = new int[length];
        Coordinates lastCity = this.problem.getCoordinates(0);
        for (int k = 1; k < length; k++) {
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
        population = new ArrayList<>();
        this.length = this.problem.getLength();
        int[] greedyPath = getGreedyPath();
        population.add(new Path(greedyPath));
        for (int i = 1; i < populationSize; i++) {
            if (i % 2 == 0) population.add(new Path(Mutation.mutationIM(greedyPath)));
            else population.add(new Path(length));
           // population.add(new Path(length));
        }


     //   population.add();
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {
        debug.add("Génération : " + debug.size());
        //parents
        ArrayList<Path> childs = new ArrayList<>();

        //TO-DO add probability crossover
        //selection for the crossover
        ArrayList<Path> selectedParents = Selection.tournament(population, 80, 1, this.evaluation);
        while (childs.size() < populationSize) {
            int rIndex1 = (int) (Math.random() * selectedParents.size());
            int rIndex2 = (int) (Math.random() * selectedParents.size());
            int[][] c = Crossover.crossoverOX1(selectedParents.get(rIndex1).getPath(), selectedParents.get(rIndex2).getPath());
            childs.add(new Path(c[0]));
            childs.add(new Path(c[1]));
        }

        //mutation
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


        ArrayList<Path> selectedChilds = Selection.tournament(mutatedChilds, 10, 5, this.evaluation);
        population.clear();
        population.addAll(selectedParents);
        population.addAll(selectedChilds);
        for (Path path : population) {
            this.evaluation.evaluate(path);
        }

        debug.add("Optimum = " + this.evaluation.getBestEvaluation());

        ecrireFichier("debug.txt", debug);

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
