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
    private String category = "";
    JsonParser strMaker = new JsonParser();
    private String strJson;

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
        final LinearLayout newTimes = findViewById(R.id.newTimesLayout);
        final LinearLayout daVinchi = findViewById(R.id.davinchiLayout);
        final LinearLayout nowadays = findViewById(R.id.nowadaysLayout);
        final LinearLayout ancient2 = findViewById(R.id.ancient2Layout);
        final Button startQuiz = findViewById(R.id.startQuizBtn);
        final Button goToLeaderboard = findViewById(R.id.leaderboardBtn);

        ancient1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Античность";
                category = "Ancient";
                ancient1.setBackgroundResource(R.drawable.round_back_white_stroke10);
                medival.setBackgroundResource(R.drawable.round_back_white10);
                daVinchi.setBackgroundResource(R.drawable.round_back_white10);
                newTimes.setBackgroundResource(R.drawable.round_back_white10);
                nowadays.setBackgroundResource(R.drawable.round_back_white10);
                ancient2.setBackgroundResource(R.drawable.round_back_white10);
                strJson = strMaker.getJSONFromFile(MainActivity.this,"questions.json");


            }
        });

        medival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Средневековье";
                category = "Medival";
                medival.setBackgroundResource(R.drawable.round_back_white_stroke10);
                ancient1.setBackgroundResource(R.drawable.round_back_white10);
                daVinchi.setBackgroundResource(R.drawable.round_back_white10);
                newTimes.setBackgroundResource(R.drawable.round_back_white10);
                nowadays.setBackgroundResource(R.drawable.round_back_white10);
                ancient2.setBackgroundResource(R.drawable.round_back_white10);
                strJson =  strMaker.getJSONFromFile(MainActivity.this,"question_medival.json");
            }
        });

        daVinchi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Возрождение";
                category = "DaVinchi";
                daVinchi.setBackgroundResource(R.drawable.round_back_white_stroke10);
                ancient1.setBackgroundResource(R.drawable.round_back_white10);
                medival.setBackgroundResource(R.drawable.round_back_white10);
                newTimes.setBackgroundResource(R.drawable.round_back_white10);
                nowadays.setBackgroundResource(R.drawable.round_back_white10);
                ancient2.setBackgroundResource(R.drawable.round_back_white10);
                strJson =  strMaker.getJSONFromFile(MainActivity.this,"questions_da_vinchi.json");
            }
        });

        newTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Новые века";
                category = "NewTimes";
                newTimes.setBackgroundResource(R.drawable.round_back_white_stroke10);
                ancient1.setBackgroundResource(R.drawable.round_back_white10);
                medival.setBackgroundResource(R.drawable.round_back_white10);
                daVinchi.setBackgroundResource(R.drawable.round_back_white10);
                nowadays.setBackgroundResource(R.drawable.round_back_white10);
                ancient2.setBackgroundResource(R.drawable.round_back_white10);
                strJson =  strMaker.getJSONFromFile(MainActivity.this,"questions_new_times.json");
            }
        });

        nowadays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Современность";
                category = "Nowadays";
                nowadays.setBackgroundResource(R.drawable.round_back_white_stroke10);
                ancient1.setBackgroundResource(R.drawable.round_back_white10);
                medival.setBackgroundResource(R.drawable.round_back_white10);
                daVinchi.setBackgroundResource(R.drawable.round_back_white10);
                newTimes.setBackgroundResource(R.drawable.round_back_white10);
                ancient2.setBackgroundResource(R.drawable.round_back_white10);
                strJson =  strMaker.getJSONFromFile(MainActivity.this,"questions_nowadays.json");
            }
        });

        ancient2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic = "Древний восток";
                category = "Ancient2";
                ancient2.setBackgroundResource(R.drawable.round_back_white_stroke10);
                ancient1.setBackgroundResource(R.drawable.round_back_white10);
                medival.setBackgroundResource(R.drawable.round_back_white10);
                daVinchi.setBackgroundResource(R.drawable.round_back_white10);
                newTimes.setBackgroundResource(R.drawable.round_back_white10);
                nowadays.setBackgroundResource(R.drawable.round_back_white10);
                strJson =  strMaker.getJSONFromFile(MainActivity.this,"questions_ancient_east.json");
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
                    intent.putExtra("category", category);
                    intent.putExtra("strJson", strJson);
                    startActivity(intent);
                    finish();
                }
            }
        });

        goToLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTopic.isEmpty()){
                    Toast.makeText(MainActivity.this, "Выберите викторину", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                    intent.putExtra("selectedTopic", selectedTopic);
                    intent.putExtra("category", category);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}