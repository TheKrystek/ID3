package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.Function;
import pl.swidurski.gp.operators.UnaryFunction;

/**
 * Author: Krystian Świdurski
 */
public class Logarithm extends UnaryFunction {
    public Logarithm() {
        super("log");
    }


    @Override
    protected Function calculate(Individual individual, Function function) {
        return () -> Math.log(function.calculate());
    }
}
