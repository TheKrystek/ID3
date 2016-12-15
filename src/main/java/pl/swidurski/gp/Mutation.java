package pl.swidurski.gp;

/**
 * Created by student on 11.12.2016.
 */
public interface Mutation extends GeneticOperation {
    Population mutate(GeneticAlgorithm algorithm, Population first, Population second);
}
