package pl.swidurski.gui;

import javafx.concurrent.Task;
import pl.swidurski.gp.GeneticAlgorithm;
import pl.swidurski.gp.Individual;

/**
 * Created by k.swidurski on 2016-12-15.
 */
public class GpTask extends Task<Result> {

    private GeneticAlgorithm algorithm;

    public GpTask(GeneticAlgorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    @Override
    protected Result call() throws Exception {
        Individual champion = algorithm.start();
        return new Result(champion, algorithm);
    }
}
