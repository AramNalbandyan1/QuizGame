package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = findViewById(R.id.resultText);
        Button goHomeButton = findViewById(R.id.goHomeButton);

        // Получаем данные из Intent
        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 10);
        String totalTime = getIntent().getStringExtra("totalTime");

        resultText.setText("Правильных ответов: " + correctAnswers + " из " + totalQuestions + "\n" +
                "Время: " + totalTime);

        goHomeButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });
    }
}