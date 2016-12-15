package pl.swidurski.gp.initialization;

import pl.swidurski.gp.GeneticAlgorithm;
import pl.swidurski.gp.InstructionSet;
import pl.swidurski.gp.OperatorNode;
import pl.swidurski.gp.Population;

/**
 * Author: Krystian Świdurski
 */
public class RampedHalfAndHalf extends RandomInitiation {

    private final RandomInitiation full, grow;

    public RampedHalfAndHalf(int maxDepth, int constantProbability) {
        super(maxDepth, constantProbability);
        full = new FullMethod(maxDepth, constantProbability);
        grow = new GrowMethod(maxDepth, constantProbability);
    }

    @Override
    protected OperatorNode createRandomNode(int depth) {
        return getRandomInt() % 2 == 0 ?
                full.createRandomNode(depth + 1) :
                grow.createRandomNode(depth + 1);
    }

    @Override
    public Population initialize(GeneticAlgorithm algorithm) {
        setInstructionSet(algorithm.getInstructionSet());
        return super.initialize(algorithm);
    }

    @Override
    public void setInstructionSet(InstructionSet instructionSet) {
        super.setInstructionSet(instructionSet);
        full.setInstructionSet(instructionSet);
        grow.setInstructionSet(instructionSet);
    }
}
