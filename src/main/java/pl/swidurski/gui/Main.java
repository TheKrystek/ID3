package pl.swidurski.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.swidurski.gui.gui.Controller;

import java.io.IOException;

public class Main extends Application {

    public static final String PATHNAME = "contact-lenses.csv";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.init();
        stage.setTitle("ID3 - K.Swidurski");
        stage.setScene(new Scene(root, 1000, 1000));
        stage.getScene().getStylesheets().add("main.css");
        stage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
