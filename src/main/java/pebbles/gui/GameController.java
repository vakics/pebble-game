package pebbles.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;
import pebbles.database.Database;
import pebbles.datapass.Datapass;
import pebbles.state.GameState;

import java.io.File;
import java.io.IOException;

/**
 * The controller which is responsible for the graphic interface of the game.
 */
public class GameController {
    @FXML
    private GridPane grid;
    @FXML
    private Label label=new Label();
    @FXML
    Button leaderboardButton;
    private GameState game=new GameState();
    private String player1;
    private String player2;
    private Database database=new Database();

    @FXML
    private void initialize(){
        File result=new File("result.json");
        if (result.exists()) leaderboardButton.setDisable(false);
        populateGrid();
        Logger.info("Have fun {} and {}!",player1,player2);
    }

    @FXML
    private void populateGrid(){
        for (var i = 0; i < grid.getRowCount(); i++) {
            for (var j = 0; j < grid.getColumnCount(); j++) {
                var square = createSquare();
                grid.add(square, j, i);
            }
        }
        this.player1= Datapass.player1;
        this.player2=Datapass.player2;
        game.setPlayersName(player1,player2);
        label.setText(game.now_plays+"'s turn");
    }


    private void handleGameOver(){
        if (game.isOver()){
            database.writeResultToFile("result.json",game);
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Over");
            alert.setContentText("Congratulations, "+game.whoWins()+" has won the game!");
            alert.showAndWait();
            resetGame();
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.debug("Click on square ({},{})", row, col);

        if (!game.take(row,col)){
            if (!game.isNextToEachOtherCol(row) || !game.isNextToEachOtherRow(col)){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid move");
                alert.setContentText("You have to take pebbles without space between them!");
                alert.showAndWait();
                return;
            }
            else if (!(game.pickFromRow(row) && !game.pickFromColumn(col)) ||
                    !(!game.pickFromRow(row) && game.pickFromColumn(col))){
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid move");
                alert.setContentText("You have to take pebbles from the same row or column!");
                alert.showAndWait();
                return;
            }

        }
        else {
            var pebble = (Circle) square.getChildren().get(0);
            pebble.setFill(Color.TRANSPARENT);
        }
        handleGameOver();
        label.setText(game.now_plays+"'s turn");
    }

    @FXML
    private void resetGame(){
        game=new GameState();
        initialize();
        Logger.debug("The game has been reset");
    }

    @FXML
    private void turnIsOverButton(){
        if (game.taken<1){
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No moves");
            alert.setContentText("You have to take at least one pebble!");
            alert.showAndWait();
        }
        game.turnIsOver();
        label.setText(game.now_plays+"'s turn");
    }

    @FXML
    private StackPane createSquare() {
        var square = new StackPane();
        var pebble=new Circle(40);
        pebble.setFill(Color.GRAY);
        square.getChildren().add(pebble);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
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
