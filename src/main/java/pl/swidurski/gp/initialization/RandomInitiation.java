package pl.swidurski.gp.initialization;

import lombok.Setter;
import pl.swidurski.gp.*;
import pl.swidurski.gp.operators.Constant;
import pl.swidurski.gp.operators.Operator;

import java.util.Random;

/**
 * Author: Krystian Świdurski
 */
public abstract class RandomInitiation implements InitializationMethod {

    protected InstructionSetHelper instructionSetHelper;
    protected final int constantProbability;
    protected final int maxDepth;
    protected final Random random;

    public RandomInitiation(int maxDepth, int constantProbability) {
        this.maxDepth = maxDepth;
        this.constantProbability = constantProbability;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public Population initialize(GeneticAlgorithm algorithm) {
        instructionSetHelper = new InstructionSetHelper(algorithm.getInstructionSet());
        Population population = new Population();
        while (population.size() < algorithm.getPopulationSize()) {
            population.add(create());
        }
        return population;
    }

    @Override
    public Individual create() {
        return new Individual(createRandomNode(0));
    }

    @Override
    public void setInstructionSet(InstructionSet instructionSet) {
        instructionSetHelper = new InstructionSetHelper(instructionSet);
    }

    protected OperatorNode createRandomTerminal() {
        return new OperatorNode(getRandomTerminal());
    }

    protected Operator getRandomTerminal() {
        if (getRandomInt() < constantProbability) {
            return new Constant(random.nextDouble() * (random.nextInt() % 100));
        }
        return instructionSetHelper.getRandomTerminal();
    }

    protected int getRandomInt() {
        return Math.abs(random.nextInt()) % 100;
    }

    protected abstract OperatorNode createRandomNode(int depth);
}
