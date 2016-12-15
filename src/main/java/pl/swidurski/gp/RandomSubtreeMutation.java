package pl.swidurski.gp;

import pl.swidurski.gp.initialization.RampedHalfAndHalf;

import static pl.swidurski.gp.PopulationHelper.getRandomIndividual;
import static pl.swidurski.gp.PopulationHelper.getRandomNode;

/**
 * Author: Krystian Świdurski
 */
public class RandomSubtreeMutation implements Mutation {
    private final int maxDepth;
    private GeneticAlgorithm algorithm;

    public RandomSubtreeMutation() {
        this(5);
    }

    public RandomSubtreeMutation(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public Population mutate(GeneticAlgorithm algorithm, Population first, Population second) {
        this.algorithm = algorithm;
        return new Population(
                createRandomIndividual(first),
                createRandomIndividual(second));
    }

    private Individual createRandomIndividual(Population population) {
        Individual individual = getRandomIndividual(population);
        OperatorNode node = getRandomNode(individual);
        OperatorNode parent = node.getParent();

        // If root then create completely new tree
        if (parent == null) {
            return createRandomIndividual(maxDepth);
        }

        parent.remove(node);
        parent.add(getRandomOperatorTree(maxDepth - node.getDepth()));
        return individual;
    }

    private Individual createRandomIndividual(int depth) {
        if (depth < 1) {
            depth = 1;
        }
        RampedHalfAndHalf rampedHalfAndHalf = new RampedHalfAndHalf(depth, 50);
        rampedHalfAndHalf.setInstructionSet(algorithm.getInstructionSet());
        return rampedHalfAndHalf.create();
    }

    private OperatorNode getRandomOperatorTree(int depth) {
        return createRandomIndividual(depth).getRoot();
    }

}
