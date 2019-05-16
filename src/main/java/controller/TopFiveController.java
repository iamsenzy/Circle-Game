package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
import lombok.extern.slf4j.Slf4j;
import circlegame.results.GameResult;
import circlegame.results.GameResultDao;
import util.guice.PersistenceModule;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TopFiveController {

    @FXML
    private TableView topfiveTable;

    @FXML
    private TableColumn player;

    @FXML
    private TableColumn winGames;



    private GameResultDao gameResultDao;

    public void back(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Loading launch scene.");
    }


    @FXML
    public void initialize() {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        gameResultDao = injector.getInstance(GameResultDao.class);

        List<Object[]> topfiveList = gameResultDao.findBest(5);

        player.setCellValueFactory(new PropertyValueFactory("player"));
        winGames.setCellValueFactory(new PropertyValueFactory("wins"));

        for(Object[] o : topfiveList){
            String player;
            Long wins;
            player =(String) o[0];
            wins = (long) o[1];
            GameResult game = new GameResult(player,wins);
            topfiveTable.getItems().add(game);
        }

    }

}
