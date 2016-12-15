package pl.swidurski.gp;

import lombok.Getter;
import lombok.Setter;
import pl.swidurski.gp.operators.Variable;

import java.util.List;

/**
 * Created by student on 11.12.2016.
 */
public class Individual {

    @Getter
    @Setter
    private double fitness;
    @Getter
    @Setter
    private Variable dependentVariable;
    @Getter
    @Setter
    private List<Variable> independentVariables;

    @Getter
    private final OperatorNode root;

    public Individual(OperatorNode root) {
        this.root = root;
        // To avoid cycles
        if (root.getParent() != null){
            root.setParent(null);
            root.setDepth(0);
        }
    }

    public String asSymbol() {
        return root.asSymbol();
    }

    public double calculate() {
        return root.calculate(this).calculate();
    }
}
