package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private TextView questions, question;
    private AppCompatButton option1, option2, option3, option4, next;
    private String correctAnswer;
    private int currentQuestionIndex = 1;
    private final int totalQuestions = 10;
    private String getSelectedStrJson;
    public void loadRandomQuestion(){
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(getSelectedStrJson);
            JSONArray questionsArray = (JSONArray) jsonObject.get("questions");

            Random random = new Random();
            JSONObject randomQuestion = (JSONObject) questionsArray.get(random.nextInt(questionsArray.size()));

            String questionText = (String) randomQuestion.get("question");
            correctAnswer = (String) randomQuestion.get("correctAnswer");
            JSONArray optionsArray = (JSONArray) randomQuestion.get("options");

            List<String> options = new ArrayList<>();
            for (Object option : optionsArray) {
                options.add((String) option);
            }
            Collections.shuffle(options);


            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));


            option1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
            option2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
            option3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
            option4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));

            option1.setEnabled(true);
            option2.setEnabled(true);
            option3.setEnabled(true);
            option4.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final String getSelectedTopic = getIntent().getStringExtra("selectedTopic");
        getSelectedStrJson = getIntent().getStringExtra("strJson");
        final ImageView exitBtn = findViewById(R.id.exitBtn);
        final TextView topicName = findViewById(R.id.topicName);
        final TextView question = findViewById(R.id.question);
        questions = findViewById(R.id.questions);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        next = findViewById(R.id.next);
        topicName.setText(getSelectedTopic);



        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(getSelectedStrJson);
            JSONArray questionsArray = (JSONArray) jsonObject.get("questions");


            Random random = new Random();
            JSONObject randomQuestion = (JSONObject) questionsArray.get(random.nextInt(questionsArray.size()));

            String questionText = (String) randomQuestion.get("question");
            correctAnswer = (String) randomQuestion.get("correctAnswer");
            JSONArray optionsArray = (JSONArray) randomQuestion.get("options");


            List<String> options = new ArrayList<>();
            for (Object option : optionsArray) {
                options.add((String) option);
            }
            Collections.shuffle(options);


            question.setText(questionText);
            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));


            View.OnClickListener answerClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatButton clickedButton = (AppCompatButton) v;
                    String selectedAnswer = clickedButton.getText().toString();

                    if (selectedAnswer.equals(correctAnswer)) {

                        clickedButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
                    } else {

                        clickedButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                    }


                    option1.setEnabled(false);
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);
                }
            };


            option1.setOnClickListener(answerClickListener);
            option2.setOnClickListener(answerClickListener);
            option3.setOnClickListener(answerClickListener);
            option4.setOnClickListener(answerClickListener);



        } catch (Exception e) {
            e.printStackTrace();
        }



        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < totalQuestions) {
                    currentQuestionIndex++;


                    question.setText(currentQuestionIndex + "/" + totalQuestions);


                    loadRandomQuestion();
                } else {

                    Toast.makeText(QuizActivity.this, "Викторина завершена!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finish();
    }

}