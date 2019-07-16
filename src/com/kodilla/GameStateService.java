package com.kodilla;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateService {

    private Button whoStartsBtn;
    private int doneMovesIterator;
    private Label infoLabel;
    private Label winnerLabel;
    private boolean playable;
    private boolean playerTurn;
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
        FileWriter writer = createNewFile();
        saveDataIntoFile(writer);
        closeFile(writer);
    }

    public void loadGame(List<GameApp.Tile> tilesList, Button whoStartsBtn) {
        int i;

        newGame();
        FileReader fr = loadFile();
        List<Character> savedGame = readFile(fr);

        if (savedGame != null && !savedGame.isEmpty()) {
            setTileText(savedGame);

            playable = true;
            playerTurn = true;
            doneMovesIterator = savedGame.get(9);
            whoStartsBtn.setDisable(true);
            infoLabel.setText("Game loaded");
        } else if (infoLabel.getText().equals("")) {
            infoLabel.setText("Problem with loading saved game");
        }
    }

    private FileWriter createNewFile() {
        File file = new File("TicTacToeSavedGame.txt");

        try {
            return new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveDataIntoFile(FileWriter writer) {
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
    }

    private void closeFile(FileWriter writer) {
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

    private FileReader loadFile() {
        try {
            return new FileReader("TicTacToeSavedGame.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Loading File Warning: There is no file TicTacToeSavedGame.txt");
            infoLabel.setText("There is no saved game");
        }
        return null;
    }

    private List<Character> readFile(FileReader fr) {
        int i = 0;
        ArrayList<Character> savedGame = new ArrayList<>();

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
        return savedGame;
    }

    private void setTileText(List<Character> savedGame) {
        for (int j = 0; j < 9; j++) {
            if (!(savedGame.get(j).toString().equals(" "))) {
                tilesList.get(j).text.setText(savedGame.get(j).toString());
            }
        }
    }
}