package controller;

import circlegame.state.Direction;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import circlegame.results.GameResult;
import circlegame.results.GameResultDao;
import circlegame.state.CircleGameState;
import util.guice.PersistenceModule;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class GameController {

    private CircleGameState gameState;
    private String playerOneName;
    private String playerTwoName;
    private int stepCount;
    private List<Image> cubeImages;
    private Instant beginGame;
    private GameResultDao gameResultDao;
    private boolean selectedCircle;
    private int clickedX , clickedY;

    @FXML
    private Label usernameLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label stepLabel;

    @FXML
    private Label solvedLabel;

    @FXML
    private Button doneButton;

    @FXML
    private RadioButton UP;

    @FXML
    private RadioButton DOWN;

    @FXML
    private RadioButton LEFT;

    @FXML
    private RadioButton RIGHT;


    private void drawGameState() {
        stepLabel.setText(String.valueOf(stepCount));

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                ImageView view = (ImageView) gameGrid.getChildren().get(i * 5 + j);
                view.setImage(cubeImages.get(gameState.getTray()[i][j].getValue()));
            }
        }
    }

    public void initdata(String userNameOne, String userNameTwo) {
        this.playerOneName = userNameOne;
        this.playerTwoName = userNameTwo;
        usernameLabel.setText("Red Player: " + this.playerOneName +"    Blue Player: "+ this.playerTwoName);
    }

    @FXML
    public void initialize() {

        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        gameResultDao = injector.getInstance(GameResultDao.class);

        gameState = new CircleGameState();
        
        stepCount = 0;

        selectedCircle = false;

        beginGame = Instant.now();

        cubeImages = Arrays.asList(
                new Image(getClass().getResource("/pictures/cube0.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube1.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube2.png").toExternalForm()),
                new Image(getClass().getResource("/pictures/cube3.png").toExternalForm())

        );

        drawGameState();
    }


    public Direction getDirection(){
            if(UP.isSelected()){
                return Direction.UP;
            }else if(DOWN.isSelected()){
                return Direction.DOWN;
            }else if(LEFT.isSelected()){
                return Direction.LEFT;
            }else {
                return Direction.RIGHT;
            }
    }

    public void circleClick(MouseEvent mouseEvent) {

        if(selectedCircle == false){
            clickedY = GridPane.getColumnIndex((Node)mouseEvent.getSource());
            clickedX = GridPane.getRowIndex((Node)mouseEvent.getSource());
            log.info("Selected Circle({},{}) {}", clickedX, clickedY, gameState.getTray()[clickedX][clickedY]);
            selectedCircle = true;
        }
        else{
            int clickedY2 = GridPane.getColumnIndex((Node)mouseEvent.getSource());
            int clickedX2 = GridPane.getRowIndex((Node)mouseEvent.getSource());
            log.info("Selected Circle({},{}) {}", clickedX2, clickedY2, gameState.getTray()[clickedX2][clickedY2]);
            selectedCircle = false;

            Direction direction = getDirection();
            if (!gameState.isSolved() && gameState.isValidStep(clickedX,clickedY,clickedX2,clickedY2,direction)) {
                if(gameState.isPlayer()){
                    stepCount++;
                }

                gameState.pushCircles(clickedX,clickedY,clickedX2,clickedY2, direction);
                if (gameState.isSolved()) {
                    if(gameState.isPlayer()){
                        log.info("Player {} solved the game in {} steps.", playerOneName, stepCount);
                        solvedLabel.setText(playerOneName+" nyert!");
                    }
                    else{
                        log.info("Player {} solved the game in {} steps.", playerTwoName, stepCount);
                        solvedLabel.setText(playerTwoName+" nyert!");
                        playerOneName = playerTwoName;
                    }

                    doneButton.setText("Finish");
                    gameResultDao.persist(getResult());
                }
            }

            drawGameState();
        }



    }

    public void resetGame(ActionEvent actionEvent) {
        gameState = new CircleGameState();
        stepCount = 0;
        solvedLabel.setText("");
        selectedCircle = false;
        drawGameState();
        beginGame = Instant.now();
        log.info("Game reset.");
    }

    private GameResult getResult() {

        GameResult result = GameResult.builder()
                                    .player(playerOneName)
                                    .solved(gameState.isSolved())
                                    .duration(Duration.between(beginGame, Instant.now()))
                                    .steps(stepCount)
                                    .build();
        return result;
    }

    public void finishGame(ActionEvent actionEvent) throws IOException {
        if (!gameState.isSolved()) {
            gameResultDao.persist(getResult());
        }

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/topfive.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Finished game, loading Top Five scene.");
    }
}
