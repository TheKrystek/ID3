package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.BinaryFunction;
import pl.swidurski.gp.operators.Function;

/**
 * Author: Krystian Świdurski
 */
public class ProtectedPower extends BinaryFunction {
    public ProtectedPower() {
        super("^");
    }

    @Override
    protected Function calculate(Individual individual, Function first, Function second) {
        return () -> {
            double base = first.calculate();
            double exponent = second.calculate();

            if (base == 0 && exponent < 0) {
                return 0;
            }

            if (base < 0) {
                base *= -1;
            }

            return Math.pow(base, exponent);
        };
    }
}
