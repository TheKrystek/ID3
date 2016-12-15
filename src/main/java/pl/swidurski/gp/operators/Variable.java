package pl.swidurski.gp.operators;

import lombok.Getter;
import lombok.Setter;
import pl.swidurski.gp.Individual;
import pl.swidurski.gp.OperatorNode;
import pl.swidurski.gp.Range;

/**
 * Author: Krystian Świdurski
 */
public class Variable extends Terminal {

    @Getter
    protected final double defaultValue;
    @Getter
    private final VariableInstance instance;
    @Getter
    @Setter
    protected Range range;

    public Variable(String name) {
        this(name, 0.0);
    }

    public Variable(String name, double defaultValue) {
        super(name);
        this.defaultValue = defaultValue;
        this.instance = new VariableInstance(this);
    }


    @Override
    public Function toFunction(Individual individual, OperatorNode node) {
        return () -> instance.getValue();
    }
}
