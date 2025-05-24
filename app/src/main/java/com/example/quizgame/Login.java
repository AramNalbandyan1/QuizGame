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

        login.setOnClickListener(v -> {
            String txt_email = email.getText().toString().trim();
            String txt_password = password.getText().toString().trim();

            if (txt_email.isEmpty() || txt_password.isEmpty()) {
                Toast.makeText(Login.this, "Введите адрес почты и пароль", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(txt_email, txt_password);
            }
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