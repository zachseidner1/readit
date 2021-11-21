package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    EditText email;
    EditText displayName;
    EditText password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClick(View view){ //When the text is clicked, send them back to login activity
        startActivity(new Intent(this, MainActivity.class));
    }

    /*
     * Submit method.
     * Runs when the user hits the submit icon.
     * Creates the user's account and logs them in.
     */

    public void submit(View view){
        email = findViewById(R.id.editTextTextEmailAddress);
        displayName = findViewById(R.id.editTextTextDisplayName);
        password = findViewById(R.id.editTextTextPassword);

        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString();
        String displayNameText = displayName.getText().toString();

        //Create the user:
        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayNameText)
                                    .build();
                            assert user != null;
                            user.updateProfile(profileUpdates);
                            //TODO go to home page.
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Sign up failed. Try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}