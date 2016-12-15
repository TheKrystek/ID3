package pl.swidurski.gp;

import pl.swidurski.gp.operators.Operator;
import pl.swidurski.gp.operators.math.Addition;
import pl.swidurski.gp.operators.math.Multiplication;
import pl.swidurski.gp.operators.math.ProtectedDivision;
import pl.swidurski.gp.operators.math.Subtraction;

/**
 * Author: Krystian Świdurski
 */
public class DefaultInstructionSet extends InstructionSet {


    public DefaultInstructionSet(Operator... operators) {
        addAll(operators);
    }

    public DefaultInstructionSet() {
    }
}
