package pl.swidurski.gp;

/**
 * Created by student on 11.12.2016.
 */
public interface Fitness {

    double calculate(Individual individual);

    default void calculateAndAssign(Individual individual) {
        individual.setFitness(calculate(individual));
    }
}
