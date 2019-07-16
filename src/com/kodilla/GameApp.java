package com.kodilla;


import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.REPEAT;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class GameApp extends Application {

    private List<Tile> tilesList = new ArrayList<>();
    private Label winnerLabel = new Label();
    private Label infoLabel = new Label();
    private int doneMovesIterator = 0;
    private Button whoStartsBtn = new Button();
    private MenuItem newGameItem = new MenuItem("New Game");
    private MenuItem saveGameItem = new MenuItem("Save Game");
    private MenuItem loadGameItem = new MenuItem("Load Game");

    private Game game = new Game(tilesList, infoLabel, winnerLabel, doneMovesIterator);
    private GameStateService gameState = new GameStateService(tilesList, winnerLabel, infoLabel, doneMovesIterator, whoStartsBtn);

    @Override
    public void start(Stage primaryStage) {
        Background background = setUpBackground();
        Pane root = setUpMenu(primaryStage);
        Scene scene = new Scene(root, 800, 800, Color.BLACK);
        root.setBackground(background);
        createTiles(root);
        setUpWhoStartsButton(root);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setUpWhoStartsButton(Pane root) {
        infoLabel.setFont(Font.font("DialogInput", FontWeight.BOLD, 28));
        winnerLabel.setFont(Font.font("DialogInput", FontWeight.BOLD, 40));
        HBox topPropertiesBox = new HBox(3);
        topPropertiesBox.setLayoutY(40);
        topPropertiesBox.setLayoutX(100);

        whoStartsBtn.setText("Who starts?");
        whoStartsBtn.setFont(Font.font("DialogInput", 20));
        topPropertiesBox.getChildren().addAll(whoStartsBtn, infoLabel);

        whoStartsBtn.setOnMouseClicked(event -> game.whoStarts(whoStartsBtn));

        topPropertiesBox.getChildren().add(winnerLabel);
        root.getChildren().add(topPropertiesBox);
    }

    private void createTiles(Pane root) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(100 + i * 200);
                tile.setTranslateY(100 + j * 200);
                root.getChildren().add(tile);
                tilesList.add(tile);
            }
        }
    }

    private Pane setUpMenu(Stage primaryStage) {
        Menu menu = new Menu("Menu");
        menu.getItems().add(newGameItem);
        menu.getItems().add(saveGameItem);
        menu.getItems().add(loadGameItem);

        MenuBar menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);
        Pane root = new Pane(vBox);

        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        menuBar.getMenus().add(menu);

        newGameItem.setOnAction(event -> gameState.newGame());
        saveGameItem.setOnAction(event -> {
            if (!game.isWeHaveWinner() && doneMovesIterator != 9 && doneMovesIterator > 0) {
                gameState.saveGame();
            } else {
                winnerLabel.setText("");
                infoLabel.setText("Saving is not possible now..");
            }
        });
        loadGameItem.setOnAction(event -> gameState.loadGame(tilesList, whoStartsBtn));
        return root;
    }

    private Background setUpBackground() {
        Image imageback = new Image("file:resources/background.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(800, 800, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, REPEAT, REPEAT, CENTER, backgroundSize);
        return new Background(backgroundImage);
    }

    public static void main(String[] args) {

        launch(args);
    }

    class Tile extends StackPane {
        Text text = new Text();

        Tile() {
            Rectangle field = new Rectangle(200, 200);
            field.setFill(null);
            field.setStroke(Color.ANTIQUEWHITE);
            field.setStrokeWidth(10);

            text.setFont(Font.font("Verdana", FontWeight.BOLD, 100));
            text.setFill(Color.WHEAT);

            setAlignment(Pos.CENTER);
            getChildren().addAll(field, text);

            setOnMouseClicked(event -> {
                if (!game.isPlayerTurn()) return;

                if ((event.getButton() == MouseButton.PRIMARY)) {
                    if (game.isPlayable() && text.getText().length() == 0) {
                        setX();
                        game.checkGameStatus();
                        game.computerMove();
                    }
                }
            });
        }

        Text getText() {
            return text;
        }

        private void setX() {
            if (text.getText().length() == 0) {
                text.setText("X");
                doneMovesIterator++;
                game.setPlayerTurn(!game.isPlayerTurn());

            }
        }
    }
}
