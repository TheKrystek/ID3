package pl.swidurski.gui.tree;


import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
/**
 * Author: Krystian Świdurski
 */
public class Cell<T extends CellContent<T>> extends Pane {
    @Getter
    @Setter
    protected double X, Y, Mod;

    @Getter
    protected Integer cellId;

    @Getter
    protected T value;

    @Getter
    protected Node view;

    @Getter
    protected List<Cell<T>> childrenCells = new ArrayList<>();

    @Getter
    protected List<Cell<T>> parentCells = new ArrayList<>();

    @Getter
    @Setter
    protected Action<T> onAction;

    public Cell(T value) {
        this.value = value;

        if (value == null) {
            this.cellId = -123456;
        } else {
            this.cellId = value.hashCode();
            this.value.setCell(this);
            setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    if (onAction != null) {
                        onAction.run(value);
                    }
                }
            });
        }
    }


    public void addCellChild(Cell<T> cell) {
        childrenCells.add(cell);
    }


    public void addCellParent(Cell<T> cell) {
        parentCells.add(cell);
    }

    public void removeCellChild(Cell<T> cell) {
        childrenCells.remove(cell);
    }

    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);
    }

    public Cell<T> getParentCell() {
        if (getParentCells().isEmpty())
            return null;
        return getParentCells().get(0);
    }

    public boolean isMostLeft() {
        if (getParentCells().isEmpty())
            return true;
        return getParentCell().getChildrenCells().get(0) == this;
    }

    public boolean isMostRight() {
        if (getParentCells().isEmpty())
            return true;
        List<Cell<T>> children = getParentCell().getChildrenCells();
        return children.get(children.size() - 1) == this;
    }

    public Cell<T> getPrevSibling() {
        if (getParentCell() == null || isMostLeft())
            return null;
        List<Cell<T>> children = getParentCell().getChildrenCells();
        return children.get(children.indexOf(this) - 1);
    }

    public Cell<T> getNextSibling() {
        if (getParentCell() == null || isMostRight())
            return null;
        return getParentCell().getChildrenCells().get(getParentCell().getChildrenCells().indexOf(this) + 1);
    }


    public Cell<T> getFirstSibling() {
        if (getParentCell() == null) {
            return null;
        }

        if (isMostLeft()) {
            return null;
        }

        return getParentCell().getChildrenCells().get(0);
    }

    public boolean isLeaf() {
        return getChildrenCells().isEmpty();
    }

    public Cell<T> getLastChild() {
        if (isLeaf()) {
            return null;
        }
        return getChildrenCells().get(getChildrenCells().size() - 1);
    }

    public Cell<T> getFirstChild() {
        if (isLeaf()) {
            return null;
        }
        return getChildrenCells().get(0);
    }

    public interface Action<T> {
        void run(T value);
    }

    private final static String SELECTED = "selected";

    public void select(boolean select) {
        if (select) {
            getStyleClass().setAll(SELECTED);
        } else {
            getStyleClass().clear();
        }
    }
}