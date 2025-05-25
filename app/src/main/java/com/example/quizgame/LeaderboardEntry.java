package com.example.quizgame;

public class LeaderboardEntry {
    public String username;
    public long score;
    public double time;

    public LeaderboardEntry(String username, long score, double time) {
        this.username = username;
        this.score = score;
        this.time = time;
    }
}
