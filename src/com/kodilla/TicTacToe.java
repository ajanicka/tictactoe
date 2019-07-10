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


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends Application {

    private Image imageback = new Image("file:resources/background.jpg");
    private boolean playable = false;
    private boolean playerTurn = false;
    private List<Tile> tilesList = new ArrayList<>();
    private Label winnerLabel = new Label();
    private Label infoLabel = new Label();
    private int doneMovesIterator = 0;
    private Button whoStartsBtn = new Button();
    boolean weHaveWinner = false;


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

        menuItem1.setOnAction(event -> newGame());
        menuItem2.setOnAction(event -> saveGame());
        menuItem3.setOnAction(event -> loadGame());

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

        Random booleanRandom = new Random();

        whoStartsBtn.setOnMouseClicked(event ->
        {
            playerTurn = booleanRandom.nextBoolean();

            if (playerTurn) {
                infoLabel.setText("YOU!!");
            } else {
                infoLabel.setText("YOUR COMPUTER :)");
            }

            whoStartsBtn.setDisable(true);
            playable = true;

            if (!playerTurn) {
                computerMove();
            }
        });

        topPropertiesBox.getChildren().add(winnerLabel);
        root.getChildren().add(topPropertiesBox);

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadGame() {
        FileReader fr = null;
        ArrayList<Character> savedGame = new ArrayList<>();
        newGame();

        try {
            fr = new FileReader("TicTacToeSavedGame.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Loading File Warning: There is no file TicTacToeSavedGame.txt");
            infoLabel.setText("There is no saved game");
        }

        int i;
        if (fr != null) {
            while (true) {
                try {
                    if (!((i = fr.read()) != -1)) {
                        break;
                    }

                    savedGame.add((char) i);
                } catch (IOException e) {
                    System.out.println("Loading File Warning");
                }
            }
        }

        if (!savedGame.isEmpty()) {
            playable = true;

            for (int j = 0; j < 9; j++) {
                if (!(savedGame.get(j).toString().equals(" "))) {
                    tilesList.get(j).text.setText(savedGame.get(j).toString());
                }
            }

            doneMovesIterator = savedGame.get(9);
            playerTurn = true;
            whoStartsBtn.setDisable(true);
            infoLabel.setText("Game loaded");
        } else {
            if (infoLabel.getText().equals("")) {
                infoLabel.setText("Problem with loading saved game");
            }
        }
    }

    private void saveGame() {
        if (!weHaveWinner && doneMovesIterator != 9 && doneMovesIterator > 0) {
            File file = new File("TicTacToeSavedGame.txt");
            FileWriter writer = null;

            try {
                writer = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String tileText;

            try {
                for (int i = 0; i < 9; i++) {
                    tileText = tilesList.get(i).text.getText();

                    if (tileText.equals("")) {
                        writer.write(" ");
                    } else {
                        writer.write(tileText);
                    }
                    infoLabel.setText("Game saved");
                }

                writer.write("" + doneMovesIterator);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            winnerLabel.setText("");
            infoLabel.setText("Saving is not possible now..");
        }
    }

    private void newGame() {
        clearBoard();
        playable = false;
        playerTurn = false;
        doneMovesIterator = 0;
        whoStartsBtn.setDisable(false);
        winnerLabel.setText("");
        infoLabel.setText("");
    }

    private void clearBoard() {
        for (Tile tile : tilesList)
            tile.text.setText("");
    }

    public void computerMove() {
        if (playable && !playerTurn) {
            while (!playerTurn) {
                Random random = new Random();
                tilesList.get(random.nextInt(9)).setO();
            }
            checkGameStatus();
        }
    }

    private void checkGameStatus() {
        String winner = "";
        int index = 0;

        for (int i = 0; i < 3; i++) {
            index = i * 3;

            if (!(tilesList.get(index).text.getText().isEmpty())) {
                weHaveWinner = tilesList.get(index).text.getText().equals(tilesList.get((index + 1)).text.getText())
                        && tilesList.get(index).text.getText().equals(tilesList.get((index + 2)).text.getText());

                if (weHaveWinner) {
                    winner = tilesList.get(index).text.getText();
                    break;
                }
            }
        }

        if (!weHaveWinner) {
            for (int i = 0; i < 3; i++) {
                index = i;
                if (!(tilesList.get(index).text.getText().isEmpty())) {
                    weHaveWinner = tilesList.get(index).text.getText().equals(tilesList.get(index + 3).text.getText())
                            && tilesList.get(index).text.getText().equals(tilesList.get(index + 6).text.getText());

                    if (weHaveWinner) {
                        winner = tilesList.get(index).text.getText();
                        break;
                    }
                }
            }
        }

        if (!weHaveWinner && !tilesList.get(0).text.getText().isEmpty()) {
            weHaveWinner = tilesList.get(0).text.getText().equals(tilesList.get(4).text.getText())
                    && tilesList.get(0).text.getText().equals(tilesList.get(8).text.getText());

            if (weHaveWinner) {
                winner = tilesList.get(0).text.getText();
            }
        }

        if (!weHaveWinner && !tilesList.get(2).text.getText().isEmpty()) {
            weHaveWinner = tilesList.get(2).text.getText().equals(tilesList.get(4).text.getText())
                    && tilesList.get(2).text.getText().equals(tilesList.get(6).text.getText());

            if (weHaveWinner) {
                winner = tilesList.get(2).text.getText();
            }
        }

        if (weHaveWinner) {
            infoLabel.setText("");

            playable = false;
            if (winner.equals("X")) {
                winnerLabel.setText("CONGRATULATIONS!!");
            } else if (winner.equals("O")) {
                winnerLabel.setText("Not this time :(");
            }
        } else if (doneMovesIterator == 9) {
            infoLabel.setText("");
            winnerLabel.setText("Where is the winner?");
            playable = false;
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

    private class Tile extends StackPane {
        private Text text = new Text();

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
                        checkGameStatus();
                        computerMove();
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

        private boolean setO() {
            if (text.getText().length() == 0) {
                text.setText("O");
                doneMovesIterator++;
                playerTurn = !playerTurn;
                return true;
            } else {
                return false;
            }
        }
    }
}
