package com.example.quizgame;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private List<LeaderboardEntry> entryList;
    private FirebaseFirestore db;
    private String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = findViewById(R.id.leaderboardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        entryList = new ArrayList<>();
        adapter = new LeaderboardAdapter(entryList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();


        category = getIntent().getStringExtra("category");
        if (category == null) {
            Log.e("LeaderboardActivity", "Category is null!");
            return;
        }


        loadLeaderboard();


        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> finish());
    }

    private void loadLeaderboard() {
        db.collection("LEADERBOARD")
                .document(category)
                .collection("RESULTS")
                .orderBy("score", Query.Direction.DESCENDING)
                .orderBy("time", Query.Direction.ASCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    entryList.clear();
                    for (DocumentSnapshot doc : querySnapshots) {
                        String username = doc.getString("username");
                        Long score = doc.getLong("score");
                        Double time = doc.getDouble("time");

                        if (username != null && score != null && time != null) {
                            entryList.add(new LeaderboardEntry(username, score.intValue(), time));
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.e("LeaderboardActivity", "Ошибка загрузки таблицы лидеров", e));
    }
}