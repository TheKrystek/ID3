package pl.swidurski.gp.operators.math;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.operators.BinaryFunction;
import pl.swidurski.gp.operators.Function;

/**
 * Author: Krystian Świdurski
 */
public class Exponentiation extends BinaryFunction {
    public Exponentiation() {
        super("^");
    }

    @Override
    protected Function calculate(Individual individual, Function first, Function second) {
        return () -> Math.pow(first.calculate(), second.calculate());
    }
}
