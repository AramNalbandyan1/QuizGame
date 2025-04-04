package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String selectedTopic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        final LinearLayout ancient1 = findViewById(R.id.ancient1Layout);
        final LinearLayout medival = findViewById(R.id.medivalLayout);
        final LinearLayout newTimes = findViewById(R.id.newTimesLay);
        final LinearLayout daVinchi = findViewById(R.id.davinchiLayout);
        final LinearLayout nowadays = findViewById(R.id.nowadaysLayout);
        final LinearLayout ancient2 = findViewById(R.id.ancient2Layout);
        final Button startQuiz = findViewById(R.id.startQuizBtn);

        ancient1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Античность";
                ancient1.setBackgroundResource(R.drawable.round_back_white_stroke10);

            }
        });

        medival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Средневековье";
                medival.setBackgroundResource(R.drawable.round_back_white_stroke10);
            }
        });

        daVinchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Возрождение";
                daVinchi.setBackgroundResource(R.drawable.round_back_white_stroke10);
            }
        });

        newTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Новые века";
                newTimes.setBackgroundResource(R.drawable.round_back_white_stroke10);
            }
        });

        nowadays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Современность";
                nowadays.setBackgroundResource(R.drawable.round_back_white_stroke10);
            }
        });

        ancient2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Древний восток";
                ancient2.setBackgroundResource(R.drawable.round_back_white_stroke10);
            }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTopic.isEmpty()){
                    Toast.makeText(MainActivity.this, "Выберите викторину", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("selectedTopic", selectedTopic);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}