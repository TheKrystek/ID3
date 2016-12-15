package pl.swidurski.gui;

import lombok.Data;
import pl.swidurski.gp.GeneticAlgorithm;
import pl.swidurski.gp.Individual;
import pl.swidurski.gp.Population;

/**
 * Created by k.swidurski on 2016-12-15.
 */
@Data
public class Result {

    private Population population;
    private int iteration;
    private int populationSize;
    private double fitness;
    private String equation;
    private Individual individual;

    public Result(Individual champion, GeneticAlgorithm algorithm) {
        fitness = champion.getFitness();
        equation = champion.getRoot().asSymbol();
        individual = champion;
        populationSize = algorithm.getPopulationSize();
        iteration = -1;
    }

    public Result(Individual champion, Population population, int iteration) {
        fitness = champion.getFitness();
        equation = champion.getRoot().asSymbol();
        individual = champion;
        populationSize = population.size();
        this.population = population;
        this.iteration = iteration;
    }
}
