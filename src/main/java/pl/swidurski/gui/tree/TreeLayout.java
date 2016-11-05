package pl.swidurski.gui.tree;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TreeLayout<T extends CellContent<T>> extends Layout {
    Graph<T> graph;

    @Getter
    private SimpleDoubleProperty limit = new SimpleDoubleProperty(5);
    @Getter
    private SimpleDoubleProperty nodeSize = new SimpleDoubleProperty(1);
    @Getter
    private SimpleDoubleProperty siblingDistance = new SimpleDoubleProperty(1);
    @Getter
    private SimpleDoubleProperty treeDistance = new SimpleDoubleProperty(2);
    @Getter
    private SimpleDoubleProperty step = new SimpleDoubleProperty(150);

    public TreeLayout(Graph graph) {
        this.graph = graph;

        ChangeListener<Number> listener = (observable, oldValue, newValue) -> execute();

        limit.addListener(listener);
        nodeSize.addListener(listener);
        siblingDistance.addListener(listener);
        treeDistance.addListener(listener);
        step.addListener(listener);
    }


    public void execute() {
        Cell<T> root = getRoot(graph.getModel().getAllCells());
        initializeNodes(root, 1);
        calculateInitialX(root);
        checkAllChildrenOnScreen(root);
        calculateFinalPositions(root, 0);
        draw(root);
    }

    private void calculateFinalPositions(Cell<T> node, int modSum) {
        node.X += modSum;
        modSum += node.Mod;

        for (Cell<T> child : node.getChildrenCells()) {
            calculateFinalPositions(child, modSum);
        }
    }

    private void checkAllChildrenOnScreen(Cell<T> node) {
        Map<Integer, Double> nodeContour = new HashMap<>();
        getLeftContour(node, 0, nodeContour, 0);

        double shiftAmount = 0;
        for (Integer y : nodeContour.keySet()) {
            if (nodeContour.get(y) + shiftAmount < 0)
                shiftAmount = (nodeContour.get(y) * -1);
        }

        if (shiftAmount > 0) {
            node.X += shiftAmount;
            node.Mod += shiftAmount;
        }
    }

    private void calculateInitialX(Cell<T> node) {

        node.getChildrenCells().forEach(this::calculateInitialX);

        if (node.isLeaf()) {
            if (!node.isMostLeft()) {
                node.setX(node.getPrevSibling().getX() + nodeSize.get() + siblingDistance.get());
            } else {
                node.setX(3);
            }
        } else if (node.getChildrenCells().size() == 1) {
            if (node.isMostLeft()) {
                node.setX(node.getChildrenCells().get(0).getX());
            } else {
                node.setX(node.getPrevSibling().getX() + nodeSize.get() + siblingDistance.get());
                node.setMod(node.getX() - node.getChildrenCells().get(0).getX());
            }
        } else {
            Cell leftChild = node.getFirstChild();
            Cell rightChild = node.getLastChild();

            if (leftChild != null && rightChild != null) {
                double mid = (leftChild.getX() + rightChild.getX()) / 2;

                if (node.isMostLeft()) {
                    node.setX(mid);
                } else {
                    node.setX(node.getPrevSibling().getX() + nodeSize.get() + siblingDistance.get());
                    node.setMod(node.getX() - mid);
                }
            }
        }

        if (node.getChildrenCells().size() > 0 && !node.isMostLeft()) {
            checkForConflicts(node, 0);
        }
    }

    private void checkForConflicts(Cell<T> node, int depth) {
        double minDistance = treeDistance.get() + nodeSize.get();
        double shiftValue = 0.0;

        Map<Integer, Double> nodeContour = new HashMap<>();
        getLeftContour(node, 0, nodeContour, depth + 1);

        Cell sibling = node.getFirstSibling();
        while (sibling != null && sibling != node) {
            Map<Integer, Double> siblingContour = new HashMap<>();
            getRightContour(sibling, 0, siblingContour, depth + 1);

            for (double level = node.getY() + 1; level <= Math.min(siblingContour.keySet().stream().mapToDouble(Integer::intValue).max().orElse(0), nodeContour.keySet().stream().mapToDouble(Integer::intValue).max().orElse(0)); level++) {

                Double a = nodeContour.get(level);
                Double b = siblingContour.get(level);
                double distance = a == null || b == null ? 0.0 : a - b;
                if (distance + shiftValue < minDistance) {
                    shiftValue = minDistance - distance;
                }
            }

            if (shiftValue > 0) {
                node.setX(node.getX() + shiftValue);
                node.setMod(node.getMod() + shiftValue);

                centerNodesBetween(node, sibling, depth + 1);

                shiftValue = 0;
            }

            sibling = sibling.getNextSibling();
        }
    }

    private void centerNodesBetween(Cell<T> leftNode, Cell<T> rightNode, int depth) {
        int leftIndex = leftNode.getParentCell().getChildrenCells().indexOf(rightNode);
        int rightIndex = leftNode.getParentCell().getChildrenCells().indexOf(leftNode);

        int numNodesBetween = (rightIndex - leftIndex) - 1;

        if (numNodesBetween > 0) {
            double distanceBetweenNodes = (leftNode.X - rightNode.X) / (numNodesBetween + 1);

            int count = 1;
            for (int i = leftIndex + 1; i < rightIndex; i++) {
                Cell<T> middleNode = leftNode.getParentCell().getChildrenCells().get(i);

                double desiredX = rightNode.X + (distanceBetweenNodes * count);
                double offset = desiredX - middleNode.X;
                middleNode.X += offset;
                middleNode.Mod += offset;
                count++;
            }

            checkForConflicts(leftNode, depth + 1);
        }
    }

    private void getRightContour(Cell<T> node, int modSum, Map<Integer, Double> values, int depth) {
        if (depth > limit.intValue())
            return;

        if (!values.containsKey((int) node.Y)) {
            values.put((int) node.Y, node.X + modSum);
        } else {
            values.put((int) node.Y, Math.max(values.get((int) node.Y), node.X + modSum));
        }

        modSum += node.Mod;

        for (Cell<T> child : node.getChildrenCells()) {
            getRightContour(child, modSum, values, depth + 1);
        }
    }

    private void getLeftContour(Cell<T> node, int modSum, Map<Integer, Double> values, int depth) {
        if (depth > limit.intValue())
            return;

        if (!values.containsKey((int) node.Y)) {
            values.put((int) node.Y, node.X + modSum);
        } else {
            values.put((int) node.Y, Math.min(values.get((int) node.Y), node.X + modSum));
        }

        modSum += node.Mod;
        for (Cell<T> child : node.getChildrenCells()) {
            getLeftContour(child, modSum, values, depth + 1);
        }
    }

    private void initializeNodes(Cell<T> node, int depth) {
        node.setX(-1);
        node.setY(depth);
        node.setMod(0);

        for (Cell<T> cell : node.getChildrenCells()) {
            initializeNodes(cell, depth + 1);
        }
    }


    private void draw(Cell<T> cell) {
        for (Cell child : cell.getChildrenCells()) {
            draw(child);
        }
        cell.relocate(cell.getX() * step.get(), cell.getY() * step.get());
    }


    public Cell<T> getRoot(List<Cell<T>> cells) {
        Optional<Cell<T>> cell = cells.stream().filter(p -> p.getParentCell() == null).findAny();
        if (cell.isPresent())
            return cell.get();
        return null;
    }
}