package tsp.projects.genetic;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;

import java.util.*;

public class Selection {


    //A TESTER
    public static ArrayList<Path> tournament( ArrayList<Path> population, int nPlayer, int nWinner, Evaluation evaluation) {
        Comparator<Path> comparator = Comparator.comparingDouble(evaluation::evaluate);
        //ArrayList<Path> population = new ArrayList<>(Arrays.asList(parents));
        ArrayList<Path> newPopulation = new ArrayList<>();

        while (population.size() >= nPlayer) {
            ArrayList<Path> players = new ArrayList<>();
            for (int i = 0; i < nPlayer; i++) {
                int x = (int) (Math.random() * (population.size()));
                players.add(population.get(x));
                population.remove(x);
            }
            players.sort(comparator);
            newPopulation.addAll(players.subList(0, nWinner));
        }
        return newPopulation;
    }


}
