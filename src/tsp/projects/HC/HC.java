package tsp.projects.HC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.LogFileOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

public class HC extends CompetitorProject {

    private ArrayList<String> debug;
    private int [] path;
    private int length;
    private int startIndex;
    private Path bestpath;

    private LogFileOutput output;


    public HC(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        //this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Hill Climbing");
        debug = new ArrayList<>();
        output = new LogFileOutput("hc.txt");
    }

    private int[] getGreedyPath() {
    	  
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
          return path;
    }

    @Override
    public void initialization() {
    	this.startIndex = 0;
        this.length = this.problem.getLength();
        int [] gpath= getGreedyPath();
        bestpath = new Path(gpath);
        this.evaluation.evaluate(bestpath);
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {
        Path mutatedPath = new Path(Mutation.mutationIM(path.clone()));
        if(this.evaluation.getBestEvaluation() > this.evaluation.evaluate(mutatedPath)) {
            output.print("Best path : " + Arrays.toString(bestpath.getPath()) + "\n");
            bestpath = mutatedPath;
        }
   }
        
        


}