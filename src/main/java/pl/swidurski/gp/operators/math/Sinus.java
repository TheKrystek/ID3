package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.Function;
import pl.swidurski.gp.operators.UnaryFunction;

/**
 * Author: Krystian Świdurski
 */
public class Sinus extends UnaryFunction {
    public Sinus() {
        super("sin");
    }

    @Override
    protected Function calculate(Individual individual, Function function) {
        return () -> Math.sin(function.calculate());
    }
}
