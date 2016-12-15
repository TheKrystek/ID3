package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.Function;
import pl.swidurski.gp.operators.UnaryFunction;

/**
 * Author: Krystian Świdurski
 */
public class Cosinus extends UnaryFunction {
    public Cosinus() {
        super("cos");
    }


    @Override
    protected Function calculate(Individual individual, Function function) {
        return () -> Math.cos(function.calculate());
    }
}
