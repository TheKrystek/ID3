package pl.swidurski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.swidurski.gui.Controller;

import java.io.IOException;

public class Main extends Application {
    public static final String GUI_MAIN_FXML = "gui/main.fxml";
    public static final String MAIN_CSS = "main.css";
    public static final String TITLE = "ID3 - K.Swidurski";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(GUI_MAIN_FXML));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.init();
        stage.setTitle(TITLE);
        stage.setScene(new Scene(root, 1000, 1000));
        stage.setMaximized(true);
        stage.getScene().getStylesheets().add(MAIN_CSS);
        stage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
