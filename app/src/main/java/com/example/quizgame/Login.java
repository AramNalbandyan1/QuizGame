package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizgame.R;
import com.example.quizgame.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button login;
    private Button register;
    private Button guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        guest = findViewById(R.id.guest);

        login.setOnClickListener(v -> {
            String txt_email = email.getText().toString().trim();
            String txt_password = password.getText().toString().trim();

            if (txt_email.isEmpty() || txt_password.isEmpty()) {
                Toast.makeText(Login.this, "Введите адрес почты и пароль", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(txt_email, txt_password);
            }
        });

        guest.setOnClickListener(v -> {
            String email = "individualproject2025@gmail.com";
            String password = "Samsung2025";

            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(this, "Вход как гость выполнен", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {

                            Toast.makeText(this, "Ошибка входа: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        register.setOnClickListener(v -> {
            Intent registerIntent = new Intent(Login.this, Register.class);
            startActivity(registerIntent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
                            Toast.makeText(Login.this, "Вы успешно вошли!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Пожалуйста верифицируете адрес почты.", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        Toast.makeText(Login.this, "Аутентификация не удалась. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("LoginActivity", "Error: ", task.getException());
                    }
                });
    }
}