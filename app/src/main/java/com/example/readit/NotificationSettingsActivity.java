package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;

public class NotificationSettingsActivity extends AppCompatActivity {

    //Create variables
    Switch thanksSwitch, repliesSwitch;
    boolean showThanks, showReplies;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SHOW_THANKS = "showThanks";
    public static final String SHOW_REPLIES = "showReplies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        //initialize views
        thanksSwitch = findViewById(R.id.switch1);
        repliesSwitch = findViewById(R.id.switch2);

        //load data from shared preferences to see the user's preferences
        SharedPreferences mPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        showThanks = mPrefs.getBoolean(SHOW_THANKS, true); //default value is true.
        showReplies = mPrefs.getBoolean(SHOW_REPLIES, true);

        //update views based on preferences.
        thanksSwitch.setChecked(showThanks);
        repliesSwitch.setChecked(showReplies);

        //change preferences when views are checked or unchecked.
        thanksSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData(SHOW_THANKS, b);
            }
        });
        repliesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveData(SHOW_REPLIES, b);
            }
        });

    }

    /** This function saves the settings data to the android phone when it is changed.
     * This is done using the shared preferences feature.
     * A name of the value and the value itself is passed into this function to be saved in the shared preferences.
     *
     * @param name stores the name of the data that will be saved.
     * @param value stores the value of the data that will be saved
     * @return Nothing
     */
    public void saveData(String name, boolean value) {
        SharedPreferences mPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }
}