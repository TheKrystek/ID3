package pl.swidurski.gp.operators;

import pl.swidurski.gp.OperatorNode;

/**
 * Author: Krystian Świdurski
 */
public abstract class Terminal extends Operator {


    public Terminal(String name) {
        super(name);
    }

    @Override
    public int getArity() {
        return 0;
    }

    @Override
    public String asSymbol(OperatorNode node) {
        return getName();
    }
}
