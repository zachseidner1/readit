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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.concurrent.TimeUnit;

public class AccountActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    TextView usernameText, thanksText;
    private static final String TAG = AccountActivity.class.getCanonicalName();
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //initialize variables
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usernameText = findViewById(R.id.usernameText);
        thanksText = findViewById(R.id.thanksPlaceholder);

        //Sometimes I will want to display a message to the user regarding an action that occurred on a different activity. This checks for that.
        if (getIntent().hasExtra("message")) {
            Toast.makeText(this, getIntent().getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }

        if (firebaseAuth.getCurrentUser() != null) {
            //Set the username text to their username:
            usernameText.setText(firebaseAuth.getCurrentUser().getDisplayName());
            FirebaseUser user = firebaseAuth.getCurrentUser();
            //Set the thanks text to show their thanks:
            DocumentReference docRef = db.collection("UserData").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserData data = document.toObject(UserData.class);
                            int thanks = data.getThanks();
                            String userThanks = getResources().getString(R.string.thanks_placeholder) + thanks;
                            thanksText.setText(userThanks);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        } else {
            usernameText.setText(R.string.display_name);
        }
    }

    public void changeDisplayName(View view) {
        Intent i = new Intent(this, DisplayNameActivity.class);
        i.putExtra("change", 0);
        startActivity(i);
    }

    public void deleteAccount(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
        builder.setMessage("Are you sure you want to do this? This action will delete all user data and cannot be undone.");
        builder.setCancelable(false);

        builder.setPositiveButton(
        "Yes",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    //Delete user data:
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    db.collection("UserData").document(uid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) { //when user data deletion is successful, delete the account.
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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
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

    public void changeHighSchool(View view) {
        Intent i = new Intent(this, ChooseHighSchoolActivity.class);
        startActivity(i);
    }

    public void goBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}