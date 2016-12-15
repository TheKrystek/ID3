package pl.swidurski.gp.initialization;

import pl.swidurski.gp.OperatorNode;
import pl.swidurski.gp.operators.Operator;

/**
 * Author: Krystian Świdurski
 */
public class GrowMethod extends RandomInitiation {

    public GrowMethod() {
        this(3, 50);
    }

    public GrowMethod(int maxDepth, int constantProbability) {
        super(maxDepth, constantProbability);
    }

    @Override
    protected OperatorNode createRandomNode(int depth) {
        if (depth < maxDepth) {
            return createRandomNonTerminal(depth + 1);
        }
        return createRandomTerminal();
    }

    private OperatorNode createRandomNonTerminal(int depth) {
        int rand = getRandomInt();

        Operator operator = (rand < 50) ? getRandomTerminal() : instructionSetHelper.getRandomOperator();
        OperatorNode node = new OperatorNode(operator);
        for (int i = 0; i < operator.getArity(); i++) {
            node.add(createRandomNode(depth));
        }
        return node;
    }
}
