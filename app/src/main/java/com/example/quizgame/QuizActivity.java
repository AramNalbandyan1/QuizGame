package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class QuizActivity extends AppCompatActivity {

    private TextView questions, question;
    private int correctAnswersCount = 0;
    private AppCompatButton option1, option2, option3, option4, next;
    private String correctAnswer;
    private int currentQuestionIndex = 1;
    private final int totalQuestions = 10;
    private String getSelectedStrJson;
    private List<JSONObject> shuffledQuestions;
    private TextView timerTextView;
    private long startTime;
    private CountDownTimer globalTimer;

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
        question = findViewById(R.id.question);
        questions = findViewById(R.id.questions);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        next = findViewById(R.id.next);
        timerTextView = findViewById(R.id.timer);
        startTime = System.currentTimeMillis();

        globalTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                int seconds = (int) (elapsedMillis / 1000);
                timerTextView.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {

            }
        }.start();

        topicName.setText(getSelectedTopic);
        questions.setText(currentQuestionIndex + "/" + totalQuestions);

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(getSelectedStrJson);
            JSONArray questionsArray = (JSONArray) jsonObject.get("questions");

            shuffledQuestions = new ArrayList<>();
            for (Object obj : questionsArray) {
                shuffledQuestions.add((JSONObject) obj);
            }
            Collections.shuffle(shuffledQuestions);

            loadQuestion(currentQuestionIndex - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        next.setOnClickListener(v -> {
            if (currentQuestionIndex < totalQuestions) {
                currentQuestionIndex++;
                questions.setText(currentQuestionIndex + "/" + totalQuestions);
                loadQuestion(currentQuestionIndex - 1);
            } else {
                if (globalTimer != null) globalTimer.cancel();
                long totalTimeMillis = System.currentTimeMillis() - startTime;
                int totalSeconds = (int) (totalTimeMillis / 1000);
                String timeSpent = String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
                Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
                resultIntent.putExtra("correctAnswers", correctAnswersCount);
                resultIntent.putExtra("totalQuestions", totalQuestions);
                resultIntent.putExtra("totalTime", timeSpent);
                resultIntent.putExtra("category", getIntent().getStringExtra("category"));
                startActivity(resultIntent);
                finish();

            }
        });

        exitBtn.setOnClickListener(v -> {
            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finish();
        });
    }

    public void loadQuestion(int index) {
        if (index >= shuffledQuestions.size()) return;

        JSONObject questionObject = shuffledQuestions.get(index);

        String questionText = (String) questionObject.get("question");
        correctAnswer = (String) questionObject.get("correctAnswer");
        JSONArray optionsArray = (JSONArray) questionObject.get("options");

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

        resetOptions();

        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatButton clickedButton = (AppCompatButton) v;
                String selectedAnswer = clickedButton.getText().toString();

                if (selectedAnswer.equals(correctAnswer)) {
                    clickedButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark));
                    correctAnswersCount++;
                } else {
                    clickedButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                }

                disableOptions();
            }
        };

        option1.setOnClickListener(answerClickListener);
        option2.setOnClickListener(answerClickListener);
        option3.setOnClickListener(answerClickListener);
        option4.setOnClickListener(answerClickListener);
    }

    private void resetOptions() {
        option1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
        option2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
        option3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));
        option4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.darker_gray));

        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);
        option4.setEnabled(true);
    }

    private void disableOptions() {
        option1.setEnabled(false);
        option2.setEnabled(false);
        option3.setEnabled(false);
        option4.setEnabled(false);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finish();
    }

}