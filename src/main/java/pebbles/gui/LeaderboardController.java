package pebbles.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.tinylog.Logger;
import pebbles.database.Database;
import pebbles.database.HighScore;
import pebbles.database.Results;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * The controller of the leaderboard.
 */
public class LeaderboardController {
    @FXML
    private TableView<HighScore> tableView;
    @FXML
    private TableColumn<HighScore,String> player;
    @FXML
    private TableColumn<HighScore,String> wins;
    private Database database=new Database();

    @FXML
    private void initialize() throws IOException {
        player.setCellValueFactory(new PropertyValueFactory<HighScore,String>("player"));
        wins.setCellValueFactory(new PropertyValueFactory<HighScore,String>("wins"));
        var list=database.getResultFromFile("result.json");
        var map=list.stream().collect(Collectors.groupingBy(Results::getWinner, Collectors.counting()));
        var list2=map.entrySet().stream().map(entry->new HighScore(entry.getKey(),entry.getValue()))
                .sorted(Comparator.comparingLong(HighScore::getWins).reversed()).toList();
        Logger.debug(list2);
        for (int i=0;i< list2.size();i++){
            tableView.getItems().add(list2.get(i));
        }
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/main_menu.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
