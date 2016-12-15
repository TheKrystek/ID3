package pl.swidurski.services;

import pl.swidurski.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Krystek on 2016-11-06.
 */
public class RuleBuilder {

    private TreeNode root;
    private List<Attribute> attributes;
    List<Rule> rules = new ArrayList<>();


    public RuleBuilder(TreeNode root, List<Attribute> attributes) {
        this.root = root;
        this.attributes = attributes;
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
            if (hasLeaf(node)) {
                Attribute attribute = getAttribute(node.getValue().getName());
                node.getValue().setAttributeDiscretizer(attribute.getAttributeDiscretizer());
                List<String> ranges = getRanges(node.getValue());
                for (String range : ranges) {
                    List<TreeNode> collect = node.getChildren().stream().filter(p -> p.getEdge().equals(range)).collect(Collectors.toList());
                    if (collect.isEmpty()) {
                        buildRules(nodes, range);
                    } else {
                        for (TreeNode child : collect) {
                            traverseTree(child, nodes);
                        }
                    }
                }
            } else {
                for (TreeNode child : node.getChildren()) {
                    traverseTree(child, nodes);
                }
            }
        }
        nodes.remove(node);
    }

    private boolean hasLeaf(TreeNode node) {
        boolean hasLeaf = false;
        for (TreeNode child : node.getChildren()) {
            if (child.getChildren().isEmpty()) {
                hasLeaf = true;
                break;
            }
        }
        return hasLeaf;
    }


    public Attribute getAttribute(String name) {
        Optional<Attribute> first = attributes.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (first.isPresent() && first.get().isDiscrete()) {
            return first.get();
        }
        return null;
    }

    public List<String> getRanges(Attribute attribute) {
        Attribute attr = getAttribute(attribute.getName());
        if (attr != null && attr.isDiscrete()) {
            return attr.getAttributeDiscretizer().getRanges();
        }
        return new ArrayList<>();
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
        System.out.println(rule);
    }

    private void buildRules(List<TreeNode> nodes, String range) {
        Rule rule = new Rule();

        rule.setResult(new Result("NULL"));
        TreeNode prev = null;
        for (TreeNode node : nodes) {
            if (prev != null) {
                rule.append(createCondition(node, prev));
            }
            prev = node;
        }

        TreeNode lastNode = nodes.get(nodes.size() - 1);
        rule.append(new RangeCondition(lastNode.getValue(), range));

        rules.add(rule);
        System.out.println(rule);
    }


    private Condition createCondition(TreeNode node, TreeNode prev) {
        if (node.getValue() != null && node.getValue().isDiscrete())
            return new RangeCondition(prev.getValue(), node.getEdge());
        return new EqualityCondition(prev.getValue(), node.getEdge());
    }
}
