package com.kodilla;

import com.kodilla.GameApp.Tile;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Random;


public class Game {

    private List<Tile> tilesList;
    private int index;
    private int doneMovesIterator;
    private boolean weHaveWinner = false;
    private boolean playable = false;
    private boolean playerTurn = false;
    private Label infoLabel;
    private Label winnerLabel;
    private Random booleanRandom = new Random();

    public Game(List<Tile> tilesList, Label infoLabel, Label winnerLabel, int doneMovesIterator) {
        this.tilesList = tilesList;
        this.infoLabel = infoLabel;
        this.winnerLabel = winnerLabel;
        this.doneMovesIterator = doneMovesIterator;
    }

    public void whoStarts(Button whoStartsBtn) {
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
    }

    public void computerMove() {
        if (playable && !playerTurn) {
            while (!playerTurn) {
                Random random = new Random();
                index = random.nextInt(9);
                setO(index);
            }
            checkGameStatus();
        }
    }

    private boolean setO(int index) {
        if (tilesList.get(index).text.getText().length() == 0) {
            tilesList.get(index).text.setText("O");
            doneMovesIterator++;
            playerTurn = !playerTurn;
            return true;
        } else {
            return false;
        }
    }

    public void checkGameStatus() {
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

    public boolean isWeHaveWinner() {
        return weHaveWinner;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayerTurn(boolean b) {
        playerTurn = b;
    }
}
