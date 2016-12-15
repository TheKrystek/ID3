package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.BinaryFunction;
import pl.swidurski.gp.operators.Function;

/**
 * Author: Krystian Świdurski
 */
public class ProtectedDivision extends BinaryFunction {
    private static final double DEFAULT_DIVISOR = 1E-5;

    public ProtectedDivision() {
        super("%");
    }

    @Override
    protected Function calculate(Individual individual, Function first, Function second) {
        double dividend = first.calculate();
        double divisor = second.calculate();

        if (divisor == 0) {
            return () -> dividend / DEFAULT_DIVISOR;
        }
        return () -> dividend / divisor;
    }
}
