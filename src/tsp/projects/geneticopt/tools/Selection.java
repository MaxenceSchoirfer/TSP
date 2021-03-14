package tsp.projects.geneticopt.tools;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.geneticopt.Adam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Selection {


    public static int[][] tournament(int[][] population, int numberPlayers, int numberWinners, Evaluation evaluation){
        ArrayList<int[]> populationClone = new ArrayList<>();
        Collections.addAll(populationClone, population);

        int indexSelectedPopulation = 0;
        int sizeSelectedPopulation = population.length / numberPlayers * numberWinners;
        int[][] selectedPopulation = new int[sizeSelectedPopulation][population[0].length];


        while (populationClone.size() >= numberPlayers){
            Path[] players = new Path[numberPlayers];
            for (int i = 0; i < numberPlayers; i++) {
                int x = (int) (Math.random() * (populationClone.size()));
                players[i] = new Path(populationClone.get(x));
                populationClone.remove(x);
            }

            int[][] winners = selectWinners(players,numberWinners, evaluation);
            for (int i = 0; i < numberWinners; i++) {
                selectedPopulation[indexSelectedPopulation] = winners[i];
                indexSelectedPopulation++;
            }
        }

        return selectedPopulation;
    }

    private static int[][] selectWinners(Path[] players, int numberWinners, Evaluation evaluation) {
        Comparator<Path> comparator = Comparator.comparingDouble(evaluation::evaluate);
        Arrays.sort(players,comparator);
        int[][] winners = new int[numberWinners][players[0].getPath().length];
        for (int i = 0; i < numberWinners; i++) {
            winners[i] = players[i].getPath();
        }
        return winners;
    }


}
