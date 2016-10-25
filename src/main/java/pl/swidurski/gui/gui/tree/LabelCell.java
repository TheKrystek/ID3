package pl.swidurski.gui.gui.tree;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Krystek on 2016-10-23.
 */
public class LabelCell<T extends CellContent<T>> extends Cell<T> {

    public LabelCell(T value) {
        super(value);
        Label label = new Label(value.getLabel());
        label.getStyleClass().add("label-cell");
        setView(new HBox(label));
    }
}
