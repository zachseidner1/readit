package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;

public class CodeVerificationActivity extends AppCompatActivity {
    private static final String TAG = CodeVerificationActivity.class.getCanonicalName();
    TextView verificationText;
    TextView codeError;
    FirebaseAuth mAuth;
    EditText numBox1;
    EditText numBox2;
    EditText numBox3;
    EditText numBox4;
    EditText numBox5;
    EditText numBox6;

    boolean ignoreNextTextChange = false;

    ArrayList<EditText> numBoxes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        mAuth = FirebaseAuth.getInstance();
        Intent i = getIntent();


        numBox1 = findViewById(R.id.numBox1);
        numBox2 = findViewById(R.id.numBox2);
        numBox3 = findViewById(R.id.numBox3);
        numBox4 = findViewById(R.id.numBox4);
        numBox5 = findViewById(R.id.numBox5);
        numBox6 = findViewById(R.id.numBox6);

        codeError = findViewById(R.id.codeError);

        //Having all these in a list makes for a lot less code later on.
        numBoxes.add(numBox1);
        numBoxes.add(numBox2);
        numBoxes.add(numBox3);
        numBoxes.add(numBox4);
        numBoxes.add(numBox5);
        numBoxes.add(numBox6);


        verificationText = findViewById(R.id.verificationDescription);
        String verifText = verificationText.getText().toString() +  " " + i.getStringExtra("phone");
        verificationText.setText(verifText);

        numBox1.requestFocus();

        //Add all the listeners to each text box...
        numBox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numBox2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        numBox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numBox3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        numBox3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numBox4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        numBox4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numBox5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        numBox5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                numBox6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //The last listener is also responsible for submitting and checking the code.
        numBox6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ignoreNextTextChange) {
                    ignoreNextTextChange = false;
                    return;
                }
                //Now that they typed the last part of the code we verify it.
                StringBuilder codeBuilder = new StringBuilder();
                //First set up code:
                for(EditText num: numBoxes){
                    codeBuilder.append(num.getText().toString());
                }

                String inputCode = codeBuilder.toString();

                Intent intent = getIntent();
                String verificationId = intent.getStringExtra("verificationId");
                Log.d(TAG, "Verification ID" + verificationId);
                Log.d(TAG, inputCode);

                //Verify the code with phoneAuth credential:
                try{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, inputCode);
                    signInWithPhoneAuthCredential(credential);
                } catch (IllegalArgumentException e) {
                    for(EditText numBox: numBoxes){
                        ignoreNextTextChange = true;
                        numBox.setText("");
                        numBox.setBackground(ContextCompat.getDrawable(numBox1.getContext(), R.drawable.rectangle_round_corners_error));
                    }
                    numBox1.requestFocus();
                    codeError.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //We have to have the sign in on a separate method.
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*
                            If this was a sign in to delete the user's account, we want to delete
                            their account and not just sign them in.
                             */
                            FirebaseUser user = task.getResult().getUser();

                            if(getIntent().hasExtra("delete")) {
                                user.delete();
                                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                // Sign in success, update UI with the signed-in user's information

                                if(user.getDisplayName() == null || user.getDisplayName().isEmpty()){
                                    Intent i = new Intent(numBox1.getContext(), DisplayNameActivity.class);
                                    startActivity(i);
                                } else { //If the user already has a display name they already have a profile.
                                    Intent i = new Intent(numBox1.getContext(), MainActivity.class);
                                    startActivity(i);
                                }
                                //I don't like doing this but I don't have a view to get the context of so I'm using numBox1.
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                for(EditText numBox: numBoxes){
                                    numBox.setBackground(ContextCompat.getDrawable(numBox1.getContext(), R.drawable.rectangle_round_corners_error));
                                    numBox.setText("");
                                }
                                numBox1.requestFocus();
                                codeError.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
}