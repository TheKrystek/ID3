package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.Function;
import pl.swidurski.gp.operators.UnaryFunction;

/**
 * Author: Krystian Świdurski
 */
public class ProtectedLogarithm extends UnaryFunction {
    public ProtectedLogarithm() {
        super("log");
    }

    @Override
    protected Function calculate(Individual individual, Function function) {
        return () -> {
            double argument = function.calculate();
            if (argument <= 0.0) {
                return -1E5;
            }
            return Math.log(argument);
        };
    }
}
