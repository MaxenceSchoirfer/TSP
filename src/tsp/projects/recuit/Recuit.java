package tsp.projects.recuit;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.LogFileOutput;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.ArrayList;
import java.util.Arrays;

public class Recuit extends CompetitorProject {

    private ArrayList<String> debug;
    private int [] path;
    private int length;
    private int startIndex;
    private double temperature ;
    private double delta ;
    private int iteration;
    private final int  maxT = 10;
    private Path actualpath;
    private Path bestpath;

    private LogFileOutput output;


    public Recuit(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        //this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Recuit simulé");
        debug = new ArrayList<>();
        output = new LogFileOutput("recuit.txt");
    }

    private boolean accept(){

        if(delta < 0) {
            bestpath = new Path(actualpath);
            return true;
        }
        else {
            double val = (-delta)/temperature;
            double proba = Math.exp(val);
            if (Math.random() < proba) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private void cooling (){
        temperature --;
        iteration = 0;
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
    	this.temperature = 4;
    	this.iteration = 0;
        this.length = this.problem.getLength();
        int [] gpath= getGreedyPath();
        actualpath = new Path(gpath);
        bestpath = new Path(gpath);
        this.evaluation.evaluate(actualpath);
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {

        Path mutatedPath = new Path(Mutation.mutationIM(actualpath.getPath()));
        delta = evaluation.evaluate(mutatedPath) - evaluation.evaluate(actualpath);
        if(accept()) {
            output.print("T : "+ temperature+"  Best path : " + Arrays.toString(bestpath.getPath()) + "\n");
            actualpath = mutatedPath;
        }
        iteration+= 1;
        if(iteration == maxT){
            cooling();
        }
   }
        
        


}