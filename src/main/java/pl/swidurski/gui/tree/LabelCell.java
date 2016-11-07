package pl.swidurski.gui.tree;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Krystek on 2016-10-23.
 */
public class LabelCell<T extends CellContent<T>> extends Cell<T> {

    public LabelCell(T value) {
        super(value);
        String text = value.getLabel();
        if (value.getDescription() != null){
            text += String.format(" (%s)", value.getDescription());
        }
        Label label = new Label(text);
        label.getStyleClass().add("label-cell");
        setView(new HBox(label));
    }
}
