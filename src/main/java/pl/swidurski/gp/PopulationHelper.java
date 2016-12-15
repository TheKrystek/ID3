package pl.swidurski.gp;

import java.util.List;
import java.util.stream.Collectors;

import static pl.swidurski.gp.RandomHelper.getRandomIntLowerThan;

/**
 * Author: Krystian Świdurski
 */
public class PopulationHelper {

    public static Individual getRandomIndividual(Population population) {
        return population.get(getRandomIntLowerThan(population.size()));
    }


    public static List<OperatorNode> getTreeNodes(Individual individual) {
        return getTreeNodes(individual, true);
    }

    public static List<OperatorNode> getTreeNodes(Individual individual, boolean withRoot) {
        List<OperatorNode> nodes = individual.getRoot().flattened().collect(Collectors.toList());
        if (!withRoot){
            nodes.remove(individual.getRoot());
        }
        return nodes;
    }

    public static OperatorNode getRandomNode(List<OperatorNode> nodes) {
        return nodes.get(getRandomIntLowerThan(nodes.size()));
    }

    public static OperatorNode getRandomNode(Individual individual) {
        return getRandomNode(getTreeNodes(individual));
    }
}
