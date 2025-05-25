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
import com.google.firebase.firestore.DocumentReference;
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
        String email = auth.getCurrentUser().getEmail();
        String uid = auth.getCurrentUser().getUid();

        if (email.equals("individualproject2025@gmail.com")) {
            Map<String, Object> result = new HashMap<>();
            result.put("username", "Guest");
            result.put("score", score);
            result.put("time", timeInSeconds);

            db.collection("LEADERBOARD")
                    .document(category)
                    .collection("RESULTS")
                    .add(result)
                    .addOnSuccessListener(docRef ->
                            Log.d("LEADERBOARD", "Результат сохранён для гостя"))
                    .addOnFailureListener(e ->
                            Log.e("LEADERBOARD", "Ошибка при сохранении гостевого результата", e));
            return;
        }

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username;


                        if (auth.getCurrentUser().getEmail().equals("individualproject2025@gmail.com")) {
                            username = "Guest";
                        } else {
                            username = documentSnapshot.getString("username");

                            if (username == null) {
                                Log.e("LEADERBOARD", "Имя пользователя не найдено");
                                return;
                            }
                        }


                        DocumentReference resultRef = db.collection("LEADERBOARD")
                                .document(category)
                                .collection("RESULTS")
                                .document(uid);

                        resultRef.get().addOnSuccessListener(existingSnapshot -> {
                            boolean shouldUpdate = true;

                            if (existingSnapshot.exists()) {
                                Long existingScore = existingSnapshot.getLong("score");
                                Double existingTime = existingSnapshot.getDouble("time");

                                if (existingScore != null && existingTime != null) {

                                    shouldUpdate = score > existingScore || (score == existingScore && timeInSeconds < existingTime);
                                }
                            }

                            if (shouldUpdate) {
                                Map<String, Object> result = new HashMap<>();
                                result.put("username", username);
                                result.put("score", score);
                                result.put("time", timeInSeconds);

                                resultRef.set(result)
                                        .addOnSuccessListener(aVoid ->
                                                Log.d("LEADERBOARD", "Лучший результат обновлён"))
                                        .addOnFailureListener(e ->
                                                Log.e("LEADERBOARD", "Ошибка при сохранении", e));
                            }
                        });
                    } else {
                        Log.e("LEADERBOARD", "Документ пользователя не найден");
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("LEADERBOARD", "Ошибка при получении username", e));
    }
}