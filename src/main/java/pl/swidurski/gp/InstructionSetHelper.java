package pl.swidurski.gp;

import lombok.Getter;
import pl.swidurski.gp.operators.Constant;
import pl.swidurski.gp.operators.Operator;

import java.util.List;
import java.util.Random;

/**
 * Author: Krystian Świdurski
 */
public class InstructionSetHelper {

    @Getter
    private final InstructionSet instructionSet;

    private final Random random;

    public InstructionSetHelper(InstructionSet instructionSet) {
        this.instructionSet = instructionSet;
        this.random = new Random(System.currentTimeMillis());
    }

    private Operator getRandom(List<Operator> operators) {
        int max = operators.size();
        // If there is no terminals in set then return constant
        if (max == 0) {
            return new Constant(random.nextDouble());
        }
        int rand = Math.abs(random.nextInt());
        return operators.get(rand % max);
    }

    public Operator getRandomOperator() {
        return getRandom(instructionSet.getOperators());
    }

    public Operator getRandomTerminal() {
        return getRandom(instructionSet.getTerminals());
    }

    public Operator getRandomNonTerminal() {
        return getRandom(instructionSet.getNonterminals());
    }
}
