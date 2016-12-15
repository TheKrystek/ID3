package pl.swidurski.gp.initialization;

import pl.swidurski.gp.OperatorNode;
import pl.swidurski.gp.operators.Operator;

/**
 * Author: Krystian Świdurski
 */
public class FullMethod extends RandomInitiation {

    public FullMethod() {
        this(3, 50);
    }

    public FullMethod(int maxDepth, int constantProbability) {
        super(maxDepth, constantProbability);
    }

    protected OperatorNode createRandomNode(int depth) {
        if (depth < maxDepth) {
            return createRandomNonTerminal(depth + 1);
        }
        return createRandomTerminal();
    }

    private OperatorNode createRandomNonTerminal(int depth) {
        Operator operator = instructionSetHelper.getRandomNonTerminal();
        OperatorNode node = new OperatorNode(operator);
        for (int i = 0; i < operator.getArity(); i++) {
            node.add(createRandomNode(depth));
        }
        return node;
    }
}
