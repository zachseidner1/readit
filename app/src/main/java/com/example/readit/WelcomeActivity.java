package com.example.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    //Please keep in mind some of the code may be a bit sus right now.

    FirebaseAuth mAuth;
    private static final String TAG = WelcomeActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }
}