package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getCanonicalName();
    EditText phoneText;
    FirebaseAuth mAuth;
    Spinner countryCodeSpinner;
    ImageView textBg;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        Spinner spinner = (Spinner) findViewById(R.id.countrySpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_codes_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

    }

    public void onSubmit(View view){
        //Set up variables:
        phoneText = findViewById(R.id.editTextPhone);
        textBg = findViewById(R.id.phoneBackground);
        countryCodeSpinner = findViewById(R.id.countrySpinner);

        //TODO cover case where user doesn't enter in any number.
        String phoneNumber = phoneText.getText().toString();
        String country = countryCodeSpinner.getSelectedItem().toString();
        String code = country.substring(0, country.indexOf(','));
        String fullNumber = code + phoneNumber;

        //Weird organization but what happens after the code is sent comes before actually sending the code.

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //No need to verify code when they verification is already completed
                //They go to choose their display name.
                Intent i = new Intent (view.getContext(), DisplayNameActivity.class);
                startActivity(i);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                //Display error to the user.
                textBg.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.rectangle_round_corners_error));
                phoneText.setError(e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Intent i = new Intent(view.getContext(), CodeVerificationActivity.class);
                i.putExtra("phone", fullNumber);
                i.putExtra("verificationId", verificationId);
                Log.d(TAG, verificationId);
                startActivity(i);
            }
        };

        if(isValidNumber(phoneNumber)){
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(fullNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();

            PhoneAuthProvider.verifyPhoneNumber(options); //TODO generate options
            
            } else { //Prompt the user to update Google Play Services
                textBg.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_round_corners_error));
                phoneText.setError("Phone number must follow the only number format and be 10 digits long, ie 0123456789");
            }

        }

    public static boolean isValidNumber(String phoneNumber){
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher("2055550125");
        return  matcher.matches() && phoneNumber.length() == 10;
    }

}