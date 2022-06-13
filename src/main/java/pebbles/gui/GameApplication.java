package pebbles.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The class, that loads the main menu.
 */
public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main_menu.fxml"));
        stage.setTitle("16 pebbles");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
