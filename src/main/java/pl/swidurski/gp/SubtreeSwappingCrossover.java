package pl.swidurski.gp;

import java.util.List;

import static pl.swidurski.gp.PopulationHelper.*;
import static pl.swidurski.gp.RandomHelper.getRandomIntLowerThan;

/**
 * Author: Krystian Świdurski
 */
public class SubtreeSwappingCrossover implements Crossover {

    CycleDetector detector = new CycleDetector();

    @Override
    public Population crossover(GeneticAlgorithm algorithm, Population first, Population second) {
        Individual individualA = getRandomIndividual(first);
        Individual individualB = getRandomIndividual(second);

        if (individualA.equals(individualB)) {
            return new Population(individualA);
        }

        OperatorNode nodeA = randomNode(individualA);
        OperatorNode nodeB = randomNode(individualB);
        OperatorNode parentA = nodeA.getParent();
        OperatorNode parentB = nodeB.getParent();

        if (parentA == null && !nodeA.getChildren().isEmpty()) {
            nodeA = nodeA.getChild(getRandomIntLowerThan(nodeA.getChildren().size()));
            parentA = nodeA.getParent();
        }
        if (parentB == null && !nodeB.getChildren().isEmpty()) {
            nodeB = nodeB.getChild(getRandomIntLowerThan(nodeB.getChildren().size()));
            parentB = nodeB.getParent();
        }

        if (parentA != null) {
            parentA.add(nodeB);
        }
        if (parentB != null) {
            parentB.add(nodeA);
        }
        return new Population(individualA, individualB);

    }

    private OperatorNode randomNode(Individual individual) {
        List<OperatorNode> nodes = getTreeNodes(individual, true);
        return getRandomNode(nodes);
    }
}
