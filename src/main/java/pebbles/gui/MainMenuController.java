package pebbles.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * The controller of the main menu.
 */
public class MainMenuController {
    @FXML
    Button leaderboardButton;

    @FXML
    private void initialize(){
        File result=new File("result.json");
        if (result.exists()) leaderboardButton.setDisable(false);
    }

    @FXML
    private void switchScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/names.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void exit(ActionEvent event){
        Platform.exit();
    }

    @FXML
    public void switchToLeaderboard(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/leaderboard.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
