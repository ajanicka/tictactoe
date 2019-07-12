package com.kodilla;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateService {

    public static boolean playable = false;
    public static boolean playerTurn = false;
    public List<GameLayout.Tile> tilesList = new ArrayList<>();
    ActionListener taskPerformer;

    public static void newGame(List<GameLayout.Tile> tilesList) {
        clearBoard(tilesList);
        playable = false;
        playerTurn = false;
        GameLayout.doneMovesIterator = 0;
        GameLayout.whoStartsBtn.setDisable(false);
        GameLayout.winnerLabel.setText("");
        GameLayout.infoLabel.setText("");
    }

    public static void saveGame(List<GameLayout.Tile> tilesList) {
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
                    GameLayout.infoLabel.setText("Game saved");
                }

                writer.write("" + GameLayout.doneMovesIterator);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void loadGame(List<GameLayout.Tile> tilesList) {
        FileReader fr = null;
        ArrayList<Character> savedGame = new ArrayList<>();
        GameStateService.newGame(tilesList);

        try {
            fr = new FileReader("TicTacToeSavedGame.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Loading File Warning: There is no file TicTacToeSavedGame.txt");
            GameLayout.infoLabel.setText("There is no saved game");
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

            GameLayout.doneMovesIterator = savedGame.get(9);
            playerTurn = true;
            GameLayout.whoStartsBtn.setDisable(true);
            GameLayout.infoLabel.setText("Game loaded");
        } else {
            if (GameLayout.infoLabel.getText().equals("")) {
                GameLayout.infoLabel.setText("Problem with loading saved game");
            }
        }
    }

    private static void clearBoard(List<GameLayout.Tile> tilesList) {
        for (GameLayout.Tile tile : tilesList)
            tile.text.setText("");
    }

}
