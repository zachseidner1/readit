package com.example.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class PostConfirmationActivity extends AppCompatActivity {
    RadioButton tip, question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_confirmation);

        tip = findViewById(R.id.tipButton);
        question = findViewById(R.id.questionButton);

        //Only one is allowed to be selected, so if one is selected deselect the other:

        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.isChecked()){
                    question.setChecked(false);
                }
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tip.isChecked()){
                    tip.setChecked(false);
                }
            }
        });
    }
}