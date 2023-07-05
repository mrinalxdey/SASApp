package com.example.sasapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;

public class AddUserInfo extends AppCompatActivity {
    EditText edName, edDOB, edDis;
    ImageView userphoto;
    Button buSubmit;
    ProgressBar progressBar;
    String userID, imgUrl;
    Uri uri;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        edName = findViewById(R.id.userName);
        edDOB = findViewById(R.id.userDOB);
        edDis = findViewById(R.id.userDisability);
        buSubmit = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressbar);
        userphoto = findViewById(R.id.loginpfp);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            userphoto.setImageURI(uri);

                        }
                        else {
                            Toast.makeText(AddUserInfo.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photopicker = new Intent(Intent.ACTION_PICK);
                photopicker.setType("image/*");
                activityResultLauncher.launch(photopicker);
            }
        });
        buSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }
    public void saveData() {
        changeInProgress(true);
        String name = edName.getText().toString();
        String dob = edDOB.getText().toString();
        String disab = edDis.getText().toString();

        DataClass dataClass = new DataClass(name, dob, disab);
        uploadData(dataClass);
    }
    public void uploadData(DataClass dataClass) {
        DocumentReference documentReference;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        documentReference = Utility.getCollectionReferenceForID().document(currentUser.getUid());

        documentReference.set(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    //image upload

                    Utility.showToast(AddUserInfo.this, "Saved");
                    changeInProgress(false);
                    startActivity(new Intent(AddUserInfo.this, MainActivity.class));
                    finish();
                }
                else {
                    Utility.showToast(AddUserInfo.this, "Failed");
                    changeInProgress(false);
                }
            }
        });
    }
    void changeInProgress(boolean inProgress) {
        if(inProgress==true) {
            progressBar.setVisibility(View.VISIBLE);
            buSubmit.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            buSubmit.setVisibility(View.VISIBLE);
        }
    }
}