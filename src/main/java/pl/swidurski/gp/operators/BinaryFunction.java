package pl.swidurski.gp.operators;

import pl.swidurski.gp.Individual;
import pl.swidurski.gp.OperatorNode;

/**
 * Author: Krystian Świdurski
 */
public abstract class BinaryFunction extends Operator {

    protected BinaryFunction(String name) {
        super(name);
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public Function toFunction(Individual individual, OperatorNode node) {
        OperatorNode first = node.getChild(0);
        OperatorNode second = node.getChild(1);
        return calculate(individual, first.calculate(individual), second.calculate(individual));
    }

    protected abstract Function calculate(Individual individual, Function first, Function second);


    @Override
    public String asSymbol(OperatorNode node) {
        return String.format("(%s %s %s)", node.getChild(0).asSymbol(), getName(), node.getChild(1).asSymbol());
    }
}
