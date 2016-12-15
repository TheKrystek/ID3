package pl.swidurski.gp;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Krystian Świdurski
 */
public class CycleDetector {

    public boolean hasCycle(TreeNode<?> root) {
        Set<TreeNode<?>> nodes = new HashSet<>();
        return detectCycle(root, nodes);
    }

    private boolean detectCycle(TreeNode<?> root, Set<TreeNode<?>> nodes) {
        if (nodes.contains(root)) {
            return true;
        }
        nodes.add(root);

        boolean result = false;
        for (TreeNode node : root.getChildren()) {
            result |= detectCycle(node, nodes);
        }
        return result;
    }
}
