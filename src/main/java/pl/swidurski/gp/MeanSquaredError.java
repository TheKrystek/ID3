package pl.swidurski.gp;

import pl.swidurski.gp.operators.VariableInstance;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Krystian Świdurski
 */
public class MeanSquaredError implements Fitness {
    @Override
    public double calculate(Individual individual) {
        final List<VariableInstance> instances = individual.getIndependentVariables().stream().map(p -> p.getInstance()).collect(Collectors.toList());
        final Range range = individual.getDependentVariable().getRange();
        final int size = range.size();
        double error = 0;

        for (int i = 0; i < size; i++) {
            // Setup new value for every variable instance
            for (VariableInstance instance : instances) {
                instance.setValueFromRange(i);
            }
            double result = individual.calculate();
            error += Math.pow(result - range.get(i), 2);
        }
        error /= size;
        if (Double.isNaN(error)){
            return 1e6;
        }
        return error;
    }
}
