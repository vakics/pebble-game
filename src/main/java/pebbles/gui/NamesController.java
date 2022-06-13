package pebbles.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;
import pebbles.datapass.Datapass;

import java.io.IOException;

/**
 * The controller of the name giving window.
 */
public class NamesController {
    @FXML
    private TextField player1;

    @FXML
    private TextField player2;


    @FXML
    private void switchScene(ActionEvent event) throws IOException {
        if (player1.getText().equals("") || player2.getText().equals("")){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missing name");
            alert.setContentText("You have to give both player a name!");
            alert.showAndWait();
            throw new  IOException();
        }
        else if (player1.getText().equals(player2.getText())){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Same name");
            alert.setContentText("The two name have to be different");
            alert.showAndWait();
            throw new  IOException();
        }
        else{
            Datapass.player1=player1.getText();
            Datapass.player2=player2.getText();
        }
        Logger.info("Names entered: {}, {}", player1.getText(),player2.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
