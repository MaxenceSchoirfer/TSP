package tsp.projects.geneticopt;

import tsp.evaluation.Evaluation;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;

public class Adam extends CompetitorProject {

    private Project currentProject;

    public Adam(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.setMethodName("H7");
        this.setAuthors("Maxence Schoirfer");
        if (evaluation.getProblem().getLength() < 200)
            this.currentProject = new GeneticSmallInstance(evaluation, 0.9, 1.0);
        else if (evaluation.getProblem().getLength() < 1000)
            this.currentProject = new GeneticLargeInstance(evaluation, 0.9, 0.4);
        else if (evaluation.getProblem().getLength() < 2000)
            this.currentProject = new GeneticLargeInstance(evaluation, 0.4, 0);
        //else this.currentProject = new SimulatedAnnealing(evaluation);
//        this.currentProject = new Recuit(this.evaluation);
    }

    @Override
    public void initialization() {
        currentProject.initialization();
    }

    @Override
    public void loop() {
        currentProject.loop();
    }
}
