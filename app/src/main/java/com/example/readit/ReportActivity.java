package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.readit.Test.getContext;

public class ReportActivity extends AppCompatActivity {
    TextView textView;
    Intent intent;
    String school;
    Button button;
    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = getIntent();
        school = intent.getStringExtra("school");
        textView = findViewById(R.id.reportTextView);
        String schoolText ="\"" + school + "\"" + textView.getText();
        textView.setText(schoolText);
        button = findViewById(R.id.reviewButton);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HighSchool hs = new HighSchool (school);
                db.collection("High Schools")
                        .add(hs)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "School successfully reported", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), ChooseHighSchoolActivity.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ReportActivity.this, "Please try again. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });
    }
}



