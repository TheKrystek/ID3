package pl.swidurski.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Getter;

import java.util.Optional;

/**
 * Created by student on 06.11.2016.
 */
public class InfoDialog {

    Alert alert;

    @Getter
    ButtonType ok;


    public InfoDialog() {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Wrong value");
        alert.setHeaderText("Entered value is not valid number");
        ok = new ButtonType("Ok");
        alert.getButtonTypes().setAll(ok);
    }


    public Optional<ButtonType> show() {
        return alert.showAndWait();
    }
}
