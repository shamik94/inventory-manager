package com.meaty.meatyselleradmin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.meaty.meatyselleradmin.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore database;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseFirestore.getInstance();
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        if (isLoggedIn()) {
            goToHomeScreenActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            return;
        }

        database.collection("admin").document("admin")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (username.equals(documentSnapshot.get("username")) && password.equals(documentSnapshot.get("password"))) {
                            saveUserPreferences();
                            goToHomeScreenActivity();
                        } else {
                            showMessage("Invalid Credentials");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("login failed");
                    }
                });
    }

    private void goToHomeScreenActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isLoggedIn() {
        SharedPreferences userSharedPreference = getApplicationContext().getSharedPreferences("login_state", Context.MODE_PRIVATE);
        return userSharedPreference.getBoolean("login", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void saveUserPreferences() {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("login_state", Context.MODE_PRIVATE).edit();
        editor.putBoolean("login", true);
        editor.apply();
    }
}
