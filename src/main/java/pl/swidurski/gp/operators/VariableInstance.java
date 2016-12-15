package pl.swidurski.gp.operators;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Krystian Świdurski
 */
public class VariableInstance {

    private Variable variable;

    @Getter
    @Setter
    private double value;

    public VariableInstance(Variable variable) {
        this.variable = variable;
        this.value = variable.getDefaultValue();
    }

    public void setValueFromRange(int index) {
        value = variable.getRange().get(index);
    }
}
