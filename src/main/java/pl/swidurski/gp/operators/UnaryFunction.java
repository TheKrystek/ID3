package pl.swidurski.gp.operators;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.OperatorNode;

/**
 * Author: Krystian Świdurski
 */
public abstract class UnaryFunction extends Operator {

    protected UnaryFunction(String name) {
        super(name);
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public Function toFunction(Individual individual, OperatorNode node) {
        return calculate(individual, node.getChild(0).calculate(individual));
    }

    protected abstract Function calculate(Individual individual, Function function);

    @Override
    public String asSymbol(OperatorNode node) {
        return String.format("%s(%s)", getName(), node.getChild(0).asSymbol());
    }
}
