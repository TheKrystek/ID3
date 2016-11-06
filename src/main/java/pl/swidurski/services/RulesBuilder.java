package pl.swidurski.services;

import pl.swidurski.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-11-06.
 */
public class RulesBuilder {

    private TreeNode root;
    List<Rule> rules = new ArrayList<>();

    public RulesBuilder(TreeNode root) {
        this.root = root;
    }

    public List<Rule> build() {
        rules.clear();
        traverseTree(root, new ArrayList<>());
        return rules;
    }


    public void traverseTree(TreeNode node, List<TreeNode> nodes) {
        if (node == null) {
            return;
        }

        nodes.add(node);
        if (node.getChildren().isEmpty()) {
            buildRules(nodes);
        } else {
            for (TreeNode child : node.getChildren()) {
                traverseTree(child, nodes);
            }
        }
        nodes.remove(node);
    }

    private void buildRules(List<TreeNode> nodes) {
        Rule rule = new Rule();

        TreeNode prev = null;
        for (TreeNode node : nodes) {
            // if leaf
            if (node.getChildren().isEmpty()) {
                rule.setResult(new Result(node.getLabel()));
            }
            if (prev != null) {
                rule.append(createCondition(node, prev));
            }
            prev = node;
        }
        rules.add(rule);
    }


    private Condition createCondition(TreeNode node, TreeNode prev) {
        if (node.getValue() != null && node.getValue().isDiscrete())
            return new RangeCondition(prev.getValue(), node.getEdge());
        return new EqualityCondition(prev.getValue(), node.getEdge());
    }
}
