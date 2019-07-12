package com.kodilla;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.kodilla.Game.*;

public class GameLayout extends Application {

    public static List<Tile> tilesList = new ArrayList<>();
    public static Label winnerLabel = new Label();
    public static Label infoLabel = new Label();
    public static int doneMovesIterator = 0;
    public static Button whoStartsBtn = new Button();

    ActionListener taskPerformer;
    private Image imageback = new Image("file:resources/background.jpg");

    @Override
    public void start(Stage primaryStage) {
        BackgroundSize backgroundSize = new BackgroundSize(800, 800, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        Menu menu = new Menu("Menu");
        MenuItem menuItem1 = new MenuItem("New Game");
        MenuItem menuItem2 = new MenuItem("Save Game");
        MenuItem menuItem3 = new MenuItem("Load Game");
        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);
        menu.getItems().add(menuItem3);

        MenuBar menuBar = new MenuBar();
        VBox vBox = new VBox(menuBar);
        Pane root = new Pane(vBox);

        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        menuBar.getMenus().add(menu);

        menuItem1.setOnAction(event -> GameStateService.newGame(tilesList));
        menuItem2.setOnAction(event -> {
            if (!weHaveWinner && doneMovesIterator != 9 && doneMovesIterator > 0) {
                GameStateService.saveGame(tilesList);
            } else {
                winnerLabel.setText("");
                infoLabel.setText("Saving is not possible now..");
            }
        });
        menuItem3.setOnAction(event -> GameStateService.loadGame(tilesList));

        Scene scene = new Scene(root, 800, 800, Color.BLACK);
        root.setBackground(background);

        infoLabel.setFont(Font.font("DialogInput", FontWeight.BOLD, 28));
        winnerLabel.setFont(Font.font("DialogInput", FontWeight.BOLD, 40));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(100 + i * 200);
                tile.setTranslateY(100 + j * 200);
                root.getChildren().add(tile);
                tilesList.add(tile);
            }
        }

        HBox topPropertiesBox = new HBox(3);
        topPropertiesBox.setLayoutY(40);
        topPropertiesBox.setLayoutX(100);
        whoStartsBtn.setText("Who starts?");
        whoStartsBtn.setFont(Font.font("DialogInput", 20));
        topPropertiesBox.getChildren().addAll(whoStartsBtn, infoLabel);

        whoStartsBtn.setOnMouseClicked(event ->
        {whoStarts(); });

        topPropertiesBox.getChildren().add(winnerLabel);
        root.getChildren().add(topPropertiesBox);

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

    public class Tile extends StackPane {
        public Text text = new Text();

        public Tile() {
            Rectangle field = new Rectangle(200, 200);
            field.setFill(null);
            field.setStroke(Color.ANTIQUEWHITE);
            field.setStrokeWidth(10);

            text.setFont(Font.font("Verdana", FontWeight.BOLD, 100));
            text.setFill(Color.WHEAT);

            setAlignment(Pos.CENTER);
            getChildren().addAll(field, text);

            setOnMouseClicked(event -> {
                if (!playerTurn) return;

                if ((event.getButton() == MouseButton.PRIMARY)) {
                    if (playable && text.getText().length() == 0) {
                        setX();
                        Game.checkGameStatus();
                        Game.computerMove();
                    }
                }
            });
        }

        private boolean setX() {
            if (text.getText().length() == 0) {
                text.setText("X");
                doneMovesIterator++;
                playerTurn = !playerTurn;
                return true;

            } else {
                return false;
            }
        }


    }
}
