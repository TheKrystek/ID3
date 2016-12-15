package pl.swidurski.gp.operators;

import lombok.Getter;
import pl.swidurski.gp.Individual;
import pl.swidurski.gp.OperatorNode;


/**
 * Created by student on 11.12.2016.
 */
public abstract class Operator {

    @Getter
    protected final String name;

    protected Operator(String name) {
        this.name = name;
    }

    public abstract Function toFunction(Individual individual, OperatorNode node);

    public abstract int getArity();

    public abstract String asSymbol(OperatorNode node);
}
