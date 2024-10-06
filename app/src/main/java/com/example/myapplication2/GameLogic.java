package com.example.myapplication2;

import android.content.Context;
import android.content.SharedPreferences;

public class GameLogic {

    SharedPreferences statsPreferences;
    int wins, losses, draws;

    public GameLogic(Context context) {
        statsPreferences = context.getSharedPreferences("stats_pref", Context.MODE_PRIVATE);
        wins = statsPreferences.getInt("wins", 0);
        losses = statsPreferences.getInt("losses", 0);
        draws = statsPreferences.getInt("draws", 0);
    }

    public void updateStats(String result) {
        SharedPreferences.Editor editor = statsPreferences.edit();

        switch (result) {
            case "win":
                wins++;
                editor.putInt("wins", wins);
                break;
            case "loss":
                losses++;
                editor.putInt("losses", losses);
                break;
            case "draw":
                draws++;
                editor.putInt("draws", draws);
                break;
        }
        editor.apply();
    }

    public String getStats() {
        return "Wins: " + wins + "\nLosses: " + losses + "\nDraws: " + draws;
    }
}

