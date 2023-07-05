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

public class RegisterActivity extends AppCompatActivity {
    EditText edPassword, edConfPass, edEmail;
    Button buRegister;
    ProgressBar progressBar;
    TextView loginredirect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        edConfPass = findViewById(R.id.editTextConfirmPassword);
        buRegister = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressbar);
        loginredirect = findViewById(R.id.loginredirect);

        buRegister.setOnClickListener(v -> createAccount());
        loginredirect.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

    }
    void createAccount() {

        String password = edPassword.getText().toString();
        String email = edEmail.getText().toString();
        String confpass = edConfPass.getText().toString();

        boolean isValidated = validateData(email,password,confpass);
        if(!isValidated) {
            return;
        }
        createAccountInFireBase(email, password);
    }
    void createAccountInFireBase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()) {
                            Utility.showToast(RegisterActivity.this,"Account Created, check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                        }
                        else {
                            Utility.showToast(RegisterActivity.this,task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
    void changeInProgress(boolean inProgress) {
        if(inProgress==true) {
            progressBar.setVisibility(View.VISIBLE);
            buRegister.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            buRegister.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email, String password, String confpass) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Email is invalid");
            return false;
        }
        if(password.length()<6) {
            edPassword.setError("Password is short");
            return false;
        }
        if(!password.equals(confpass)) {
            edConfPass.setError("Password doesn't match");
            return false;
        }
        return true;
    }
}