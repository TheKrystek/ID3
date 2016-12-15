package pl.swidurski.gui.dialogs;

import javafx.scene.control.TextInputDialog;
import pl.swidurski.model.Attribute;

import java.util.Optional;

/**
 * Created by student on 06.11.2016.
 */
public class AskForRangeDialog {

    private TextInputDialog dialog;

    public AskForRangeDialog(Attribute attribute) {
        dialog = new TextInputDialog("5");
        dialog.setTitle("Number of ranges");
        dialog.setHeaderText("Enter number of ranges for attributeName \"" + attribute.getName() + "\"");
        dialog.setContentText("Please enter number of ranges:");
    }

    public int show() {
        int defaultValue = 5;
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return Integer.parseInt(result.get());
        }
        return defaultValue;
    }
}
