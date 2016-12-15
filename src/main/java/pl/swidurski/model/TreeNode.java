package pl.swidurski.model;

import lombok.Getter;
import lombok.Setter;
import pl.swidurski.gui.tree.Cell;
import pl.swidurski.gui.tree.CellContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Krystian Świdurski
 */
public class TreeNode implements CellContent<TreeNode> {
    private String label;

    @Getter
    private String edge;

    @Getter
    @Setter
    protected Attribute value;

    @Getter
    protected List<TreeNode> children = new ArrayList<>();

    @Getter
    @Setter
    TreeNode parent;

    @Getter
    @Setter
    private Info info;

    @Getter
    private Cell cell;

    @Setter
    Double possibility = 1.0;

    public TreeNode(Attribute value, String label, String edge) {
        this.value = value;
        this.label = label;
        this.edge = edge;
    }

    @Override
    public String toString() {
        return String.format("L:%s, E:%s", label, edge);
    }


    public void addChild(TreeNode node) {
        children.add(node);
        node.setParent(this);
    }

    @Override
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return possibility.equals(1.0) ? null : getPossibility();
    }

    public String getPossibility() {
        return String.format("%.1f%%", possibility * 100, 0);
    }
}
