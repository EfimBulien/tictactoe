package com.example.myapplication2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private String[] gameBoard;
    private boolean isPlayerOneTurn = true;
    private int playerOneWins = 0, playerTwoWins = 0, draws = 0;

    private TicTacToe game;
    private GameLogic gameLogic;
    private SharedPreferences sharedPreferences;
    private boolean isNightMode;
    private final Button[][] buttons = new Button[3][3];
    private TextView statusText;
    private TextView statsView;
    private boolean isBotMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("theme_pref", MODE_PRIVATE);
        isNightMode = sharedPreferences.getBoolean("night_mode", false);
        setCurrentTheme();
        setContentView(R.layout.activity_main);
        game = new TicTacToe();
        gameLogic = new GameLogic(this);
        initializeViews();
        loadGameStatistics();
        loadGameBoard();
        updateStats();
    }

    private void toggleTheme() {
        saveGameBoard();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        isNightMode = !isNightMode;
        editor.putBoolean("night_mode", isNightMode);
        editor.apply();
        recreate();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("gameBoard", game.getBoardAsStringArray());
        outState.putBoolean("isPlayerOneTurn", isPlayerOneTurn);
        outState.putInt("playerOneWins", playerOneWins);
        outState.putInt("playerTwoWins", playerTwoWins);
        outState.putInt("draws", draws);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameBoard = savedInstanceState.getStringArray("gameBoard");
        isPlayerOneTurn = savedInstanceState.getBoolean("isPlayerOneTurn");
        playerOneWins = savedInstanceState.getInt("playerOneWins");
        playerTwoWins = savedInstanceState.getInt("playerTwoWins");
        draws = savedInstanceState.getInt("draws");
        updateGameBoardUI();
    }

    private void updateGameBoardUI() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(gameBoard[i * 3 + j]);
            }
        }
    }

    private void setCurrentTheme() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initializeViews() {
        statusText = findViewById(R.id.statusText);
        statsView = findViewById(R.id.statsView);

        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> onPlayerMove(row, col));
            }
        }

        Button themeToggleButton = findViewById(R.id.themeToggleButton);
        themeToggleButton.setOnClickListener(v -> toggleTheme());

        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());

        Button botButton = findViewById(R.id.botButton);
        botButton.setOnClickListener(v -> {
            isBotMode = !isBotMode;
            resetGame();
        });
    }

    private void onPlayerMove(int row, int col) {
        if (game.makeMove(row, col)) {
            buttons[row][col].setText(String.valueOf(game.getCurrentPlayer()));
            if (game.checkWinner() != '\0') {
                handleGameEnd(game.checkWinner() == 'X' ? "win" : "loss");
            } else if (game.isDraw()) {
                handleGameEnd("draw");
            } else if (isBotMode) {
                botMove();
            }
        }
    }

    private void botMove() {
        game.botMove();
        updateBoard();
        if (game.checkWinner() != '\0') {
            handleGameEnd(game.checkWinner() == 'X' ? "win" : "loss");
        } else if (game.isDraw()) {
            handleGameEnd("draw");
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(String.valueOf(game.getBoard()[i][j]));
            }
        }
    }

    private void handleGameEnd(String result) {
        gameLogic.updateStats(result);
        statusText.setText(result.equals("win") ? "Player X Wins!" :
                result.equals("loss") ? "Player O Wins!" : "It's a Draw!");
        updateStats();
        disableBoard();
    }

    private void updateStats() {
        statsView.setText(gameLogic.getStats());
    }

    private void disableBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        game.resetBoard();
        statusText.setText("");
        updateBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(true);
            }
        }
    }

    private void loadGameStatistics() {
        SharedPreferences preferences = getSharedPreferences("gameStats", MODE_PRIVATE);
        playerOneWins = preferences.getInt("playerOneWins", 0);
        playerTwoWins = preferences.getInt("playerTwoWins", 0);
        draws = preferences.getInt("draws", 0);
    }

    private void saveGameBoard() {
        SharedPreferences preferences = getSharedPreferences("gameBoardState", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                editor.putString("cell_" + i + "_" + j, String.valueOf(game.getBoard()[i][j]));
            }
        }
        editor.apply();
    }

    private void loadGameBoard() {
        SharedPreferences preferences = getSharedPreferences("gameBoardState", MODE_PRIVATE);
        gameBoard = new String[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameBoard[i * 3 + j] = preferences.getString("cell_" + i + "_" + j, "");
            }
        }
        updateGameBoardUI();
    }
}
