package com.kodilla;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateService {

    private Button whoStartsBtn;
    private int doneMovesIterator;
    private Label infoLabel;
    private Label winnerLabel;
    private boolean playable = false;
    private boolean playerTurn = false;
    private List<GameApp.Tile> tilesList = new ArrayList<>();
    ActionListener taskPerformer;

    public GameStateService(List<GameApp.Tile> tilesList, Label winnerLabel, Label infoLabel, int doneMovesIterator, Button whoStartsBtn) {
        this.tilesList = tilesList;
        this.winnerLabel = winnerLabel;
        this.infoLabel = infoLabel;
        this.doneMovesIterator = doneMovesIterator;
        this.whoStartsBtn = whoStartsBtn;
    }

    public void newGame() {
        clearBoard();
        playable = false;
        playerTurn = false;
        doneMovesIterator = 0;
        whoStartsBtn.setDisable(false);
        winnerLabel.setText("");
        infoLabel.setText("");
    }

    public void saveGame() {
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
    }

    private void clearBoard() {
        for (GameApp.Tile tile : tilesList)
            tile.text.setText("");
    }

    public void loadGame(List<GameApp.Tile> tilesList, Button whoStartsBtn) {
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
}
