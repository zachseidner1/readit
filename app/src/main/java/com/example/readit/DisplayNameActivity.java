package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

public class DisplayNameActivity extends AppCompatActivity {
    EditText displayNameText;
    TextView prompt, thanksPlaceHolder;
    FirebaseFirestore db;


    private static final String TAG = SignUpActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        //Initialize variables
        setContentView(R.layout.activity_display_name);
        displayNameText = findViewById(R.id.displayNameText);
        prompt = findViewById(R.id.displayNamePrompt);


        if(getIntent().hasExtra("change")) {
            prompt.setText(R.string.change_name_colon);
        }
        displayNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Drawable blueAccountCircle = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_blue_account_circle);
                blueAccountCircle.setBounds(0, 0, 60, 60);
                Drawable greyAccountCircle = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_account_circle_24);
                greyAccountCircle.setBounds(0, 0, 60, 60);
                if(TextUtils.isEmpty(displayNameText.getText().toString())){
                    displayNameText.setCompoundDrawables(greyAccountCircle, null, null, null);
                } else{
                    displayNameText.setCompoundDrawables(blueAccountCircle, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void onSubmit(View view){
        String name = displayNameText.getText().toString().trim();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Store username in UserData:
        DocumentReference docRef = db.collection("UserData").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData userData = documentSnapshot.toObject(UserData.class);
                userData.setUsername(name);
                db.collection("UserData").document(user.getUid()).set(userData);
            }
        });


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");

                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(i);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}