package com.example.sasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText edPassword, edEmail;
    Button buLogin;
    ProgressBar progressBar;
    TextView registerRedirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        buLogin = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressbar);
        registerRedirect = findViewById(R.id.registerRedirect);

        buLogin.setOnClickListener(v -> loginUser());
        registerRedirect.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
    void loginUser() {
        String password = edPassword.getText().toString();
        String email = edEmail.getText().toString();

        boolean isValidated = validateData(email,password);
        if(!isValidated) {
            return;
        }
        loginAccountInFirebase(email, password);
    }
    void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()) {
                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else {
                        Utility.showToast(LoginActivity.this,"Email not verified, please verify email");
                    }
                }
                else {
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }
    void changeInProgress(boolean inProgress) {
        if(inProgress==true) {
            progressBar.setVisibility(View.VISIBLE);
            buLogin.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            buLogin.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String password) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Email is invalid");
            return false;
        }
        if(password.length()<6) {
            edPassword.setError("Password is short");
            return false;
        }
        return true;
    }
}