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
        dialog = new TextInputDialog("2");
        dialog.setTitle("Number of ranges");
        dialog.setHeaderText("Enter number of ranges for attributeName \"" + attribute.getName() + "\"");
        dialog.setContentText("Please enter number of ranges:");
    }

    public int show() {
        int defaultValue = 2;
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());
            } catch (Exception e) {
                System.err.println("Cannot parse using default value =" + defaultValue);
            }
        }
        return defaultValue;
    }
}
