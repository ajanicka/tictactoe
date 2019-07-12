package com.kodilla;

import java.util.Random;

import static com.kodilla.GameLayout.*;

public class Game {

    public static boolean playable = false;
    public static boolean playerTurn = false;
    static int index;
    static boolean weHaveWinner = false;

    static Random booleanRandom = new Random();

    public static void whoStarts() {
        playerTurn = booleanRandom.nextBoolean();

        if (playerTurn) {
            infoLabel.setText("YOU!!");
        } else {
            infoLabel.setText("YOUR COMPUTER :)");
        }

        whoStartsBtn.setDisable(true);
        playable = true;

        if (!playerTurn) {
            Game.computerMove();
        }
    }

    public static void computerMove() {
        if (playable && !playerTurn) {
            while (!playerTurn) {
                Random random = new Random();
                index = random.nextInt(9);
                setO(index);
            }
            checkGameStatus();
        }
    }

    private static boolean setO(int index) {
        if (tilesList.get(index).text.getText().length() == 0) {
            tilesList.get(index).text.setText("O");
            doneMovesIterator++;
            playerTurn = !playerTurn;
            return true;
        } else {
            return false;
        }
    }

    public static void checkGameStatus() {
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
        } else if (GameLayout.doneMovesIterator == 9) {
            infoLabel.setText("");
            winnerLabel.setText("Where is the winner?");
            playable = false;
        }
    }
}
