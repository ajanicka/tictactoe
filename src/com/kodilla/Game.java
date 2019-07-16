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
    private boolean weHaveWinner;
    private boolean playable;
    private boolean playerTurn;
    private Label infoLabel;
    private Label winnerLabel;
    private Random random = new Random();

    public Game(List<Tile> tilesList, Label infoLabel, Label winnerLabel, int doneMovesIterator) {
        this.tilesList = tilesList;
        this.infoLabel = infoLabel;
        this.winnerLabel = winnerLabel;
        this.doneMovesIterator = doneMovesIterator;
    }

    public void whoStarts(Button whoStartsBtn) {
        playerTurn = random.nextBoolean();

        if (playerTurn) {
            infoLabel.setText("YOU!!");
        } else {
            infoLabel.setText("YOUR COMPUTER :)");
        }

        whoStartsBtn.setDisable(true);
        playable = true;

        if (!playerTurn) {
            computerMove();
            checkGameStatus();
        }
    }

    public void computerMove() {
        if (playable && !playerTurn) {
            while (!playerTurn) {
                index = random.nextInt(9);
                setO(index);
            }
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

        setWinnerInfo(winner);
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

    private void setO(int index) {
        if (tilesList.get(index).text.getText().length() == 0) {
            tilesList.get(index).text.setText("O");
            doneMovesIterator++;
            playerTurn = !playerTurn;
        }
    }

    private void setWinnerInfo(String winner) {
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
}
