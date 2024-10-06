package com.example.myapplication2;

import java.util.Random;

public class TicTacToe {

    private final char[][] board;
    private char currentPlayer;

    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X';
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == '\0') {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return true;
        }
        return false;
    }

    public char getCurrentPlayer() {
        return (currentPlayer == 'X') ? 'O' : 'X';
    }

    public char[][] getBoard() {
        return board;
    }

    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '\0';
            }
        }
        currentPlayer = 'X';
    }

    public char checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '\0') {
                return board[i][0];
            }
        }
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != '\0') {
                return board[0][i];
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '\0') {
            return board[0][0];
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '\0') {
            return board[0][2];
        }
        return '\0';
    }

    public boolean isDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    public void botMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != '\0');

        board[row][col] = currentPlayer;
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    public String[] getBoardAsStringArray() {
        String[] boardArray = new String[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardArray[i * 3 + j] = String.valueOf(board[i][j]); // Convert the board to a string array
            }
        }
        return boardArray;
    }

}
