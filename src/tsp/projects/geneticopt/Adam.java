package tsp.projects.geneticopt;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.Project;

public class Adam extends CompetitorProject {

    private Project currentProject;

    public Adam(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.setMethodName("Adam la bagarre");
        this.setAuthors("Maxence Schoirfer");
        if (evaluation.getProblem().getLength() < 200 || evaluation.getProblem().getLength() > 2000) this.currentProject = new GeneticSmallInstance(evaluation) ;
        else if (evaluation.getProblem().getLength() < 2000) this.currentProject = new GeneticLargeInstance(evaluation) ;

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
