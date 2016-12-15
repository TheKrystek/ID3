package pl.swidurski.gp;

/**
 * Created by student on 11.12.2016.
 */
public interface Crossover extends GeneticOperation {
    Population crossover(GeneticAlgorithm algorithm, Population first, Population second);
}
