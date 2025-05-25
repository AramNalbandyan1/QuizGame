package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private int correctAnswers;
    private int totalQuestions;
    private String totalTime;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = findViewById(R.id.resultText);
        TextView leftEmoji = findViewById(R.id.leftEmoji);
        TextView rightEmoji = findViewById(R.id.rightEmoji);
        Button goHomeButton = findViewById(R.id.goHomeButton);


        correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        totalQuestions = getIntent().getIntExtra("totalQuestions", 10);
        totalTime = getIntent().getStringExtra("totalTime"); // например: "00:42"
        category = getIntent().getStringExtra("category");

        resultText.setText("Правильных ответов: " + correctAnswers + " из " + totalQuestions + "\n" +
                "Время: " + totalTime);


        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        leftEmoji.startAnimation(bounce);
        rightEmoji.startAnimation(bounce);


        goHomeButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });


        saveResultToLeaderboard(category, correctAnswers, parseTimeToSeconds(totalTime));
    }


    private double parseTimeToSeconds(String timeStr) {
        try {
            String[] parts = timeStr.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return minutes * 60 + seconds;
        } catch (Exception e) {
            return 9999.0;
        }
    }


    private void saveResultToLeaderboard(String category, int score, double timeInSeconds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();


        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");

                        Map<String, Object> result = new HashMap<>();
                        result.put("username", username);
                        result.put("score", score);
                        result.put("time", timeInSeconds);


                        db.collection("LEADERBOARD")
                                .document(category)
                                .collection("RESULTS")
                                .add(result)
                                .addOnSuccessListener(docRef ->
                                        Log.d("LEADERBOARD", "Результат сохранён"))
                                .addOnFailureListener(e ->
                                        Log.e("LEADERBOARD", "Ошибка при сохранении", e));
                    } else {
                        Log.e("LEADERBOARD", "Документ пользователя не найден");
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("LEADERBOARD", "Ошибка при получении username", e));
    }
}