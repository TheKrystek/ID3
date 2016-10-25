package pl.swidurski.gui.tree;

/**
 * Created by Krystek on 2016-10-25.
 */
public interface CellContent<T extends CellContent<T>> {
    void setCell(Cell<T> cell);

    String getLabel();
}
