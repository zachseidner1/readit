package com.example.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ImageButton submitBtn;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View view){ //When the text is clicked, send them to sign up activity.
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void submit(View view){

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            //TODO go to home page.
        } else {
            //TODO login user.
        }
    }
}