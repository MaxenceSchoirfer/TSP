package tsp.projects.geneticopt;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.output.LogFileOutput;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;
import tsp.projects.geneticopt.tools.Greedy;
import tsp.projects.geneticopt.tools.Mutation;

import java.util.ArrayList;
import java.util.Arrays;

public class Recuit extends Project {

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
    private final double coef = 0.9;

    private LogFileOutput output;


    public Recuit(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        //this.addAuthor("Maxence Schoirfer");
        this.addAuthor("Badis Belhadj-Chaidi");
        this.setMethodName("Recuit simulé");
        //debug = new ArrayList<>();
        //output = new LogFileOutput("recuit.txt");
    }

    private boolean accept(){

        if(this.delta < 0) {
            this.bestpath = new Path(this.actualpath);
            return true;
        }
        else {
            double val = (-this.delta)/this.temperature;
            double proba = Math.exp(val);
            return Math.random() < proba;
        }
    }

    private void cooling (){
        if(temperature >0) {
            this.temperature--;
            this.iteration = 0;
        }
    }



    @Override
    public void initialization() {
    	this.startIndex = 0;
    	this.temperature = 4;
    	this.iteration = 0;
        this.length = this.problem.getLength();
        Greedy gr = new Greedy(this.problem);
        gr.initialization();

        int[] gpath = gr.getBestPath(this.evaluation);
        //int [] gpath= gr.getSinglePath(startIndex);
        this.actualpath = new Path(gpath);
        this.bestpath = new Path(gpath);
        this.evaluation.evaluate(actualpath);
    }


    //augmenter la mutation si l'optimum de change pas sur plusieurs génération
    @Override
    public void loop() {

        Path mutatedPath = new Path(Mutation.mutationIM(this.actualpath.getPath()));
        this.delta = this.evaluation.evaluate(mutatedPath) - this.evaluation.evaluate(actualpath);
        if(accept()) {
//            output.print("best: "+this.evaluation.getBestEvaluation()+", T : "+ temperature+"  Best path : " + Arrays.toString(this.bestpath.getPath()) + "\n");
            this.actualpath = mutatedPath;
        }

        iteration+= 1;
        if(iteration == maxT){
            cooling();
            if(temperature <= 0){
                temperature= 0.1;
            }
        }
   }
}