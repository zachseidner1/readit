package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AccountActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView usernameText;
    private static final String TAG = AccountActivity.class.getCanonicalName();
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        firebaseAuth = FirebaseAuth.getInstance();
        usernameText = findViewById(R.id.usernameText);

        usernameText.setText(firebaseAuth.getCurrentUser().getDisplayName());
    }

    public void changeDisplayName(View view) {
        Intent i = new Intent(this, DisplayNameActivity.class);
        i.putExtra("change", 0);
        startActivity(i);
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setMessage("Are you sure you want to do this? This action cannot be undone.");
        builder.setCancelable(false);

        builder.setPositiveButton (
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //Delete the user:
                        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Account successfully deleted", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                                    startActivity(i);

                                } else {
                                    //If it is not successful it is probably because the user has not recently signed in. We force the user to sign in again:

                                    Toast.makeText(getApplicationContext(), "Deleting your account is a sensitive action. Use the verification code to confirm.", Toast.LENGTH_LONG).show();
                                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            //If they already verified we should be fine to delete the user.
                                            firebaseAuth.getCurrentUser().delete();
                                            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                            startActivity(intent);
                                            Log.d(TAG, "Completed");
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            //Display error to the user.
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "onVerificationFailed: ");
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationId,
                                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                            Intent i = new Intent(view.getContext(), CodeVerificationActivity.class);
                                            i.putExtra("phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                                            i.putExtra("verificationId", verificationId);
                                            i.putExtra("delete", 0); //value is required so I just put 0 :|
                                            Log.d(TAG, verificationId);
                                            startActivity(i);
                                            Log.d(TAG, "onCodeSent: ");
                                        }
                                    };
                                    PhoneAuthOptions options =
                                            PhoneAuthOptions.newBuilder(firebaseAuth)
                                                    .setPhoneNumber(firebaseAuth.getCurrentUser().getPhoneNumber())       // Phone number to verify
                                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                    .setActivity(AccountActivity.this)                 // Activity (for callback binding)
                                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                    .build();

                                    PhoneAuthProvider.verifyPhoneNumber(options);
                                }
                            }
                        });
                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}