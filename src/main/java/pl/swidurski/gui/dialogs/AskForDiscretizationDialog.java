package pl.swidurski.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import lombok.Getter;

import java.util.Optional;

/**
 * Created by student on 06.11.2016.
 */
public class AskForDiscretizationDialog {

    Alert alert;

    @Getter
    ButtonType yes, no;


    public AskForDiscretizationDialog() {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Discretization");
        alert.setHeaderText("Some of arguments are numerical. Do you want to discretize them?");

        yes = new ButtonType("YES");
        no = new ButtonType("NO");
        alert.getButtonTypes().setAll(yes, no);
    }


    public Optional<ButtonType> show() {
        return alert.showAndWait();
    }
}
