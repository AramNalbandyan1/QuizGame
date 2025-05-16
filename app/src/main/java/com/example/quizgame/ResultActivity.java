package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = findViewById(R.id.resultText);
        TextView leftEmoji = findViewById(R.id.leftEmoji);
        TextView rightEmoji = findViewById(R.id.rightEmoji);
        Button goHomeButton = findViewById(R.id.goHomeButton);

        int correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 10);
        String totalTime = getIntent().getStringExtra("totalTime");

        resultText.setText("Правильных ответов: " + correctAnswers + " из " + totalQuestions + "\n" +
                "Время: " + totalTime);

        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        leftEmoji.startAnimation(bounce);
        rightEmoji.startAnimation(bounce);

        goHomeButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        });
    }
}