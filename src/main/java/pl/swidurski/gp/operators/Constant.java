package pl.swidurski.gp.operators;

import lombok.Getter;
import pl.swidurski.gp.Individual;
import pl.swidurski.gp.OperatorNode;


/**
 * Author: Krystian Świdurski
 */
public class Constant extends Terminal {

    @Getter
    private double value;

    public Constant(double value) {
        super(String.valueOf(value));
        this.value = value;
    }


    @Override
    public Function toFunction(Individual individual, OperatorNode node) {
        return () -> value;
    }
}
