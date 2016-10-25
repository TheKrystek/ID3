package pl.swidurski.gui.model;

import lombok.Getter;
import lombok.Setter;
import pl.swidurski.gui.gui.tree.Cell;
import pl.swidurski.gui.gui.tree.CellContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-10-23.
 */
public class TreeNode implements CellContent<TreeNode> {
    private String label;

    @Getter
    private String edge;

    @Getter
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
}
