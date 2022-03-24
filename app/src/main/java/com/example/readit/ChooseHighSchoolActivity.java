package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.model.Place;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class ChooseHighSchoolActivity extends AppCompatActivity {
    SearchView searchView;
    ArrayAdapter adapter;
    Button submitButton;
    String highschool;
    AutocompleteSupportFragment autocompleteFragment;
    FirebaseFirestore db;
    FirebaseAuth auth;
    UserData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_high_school);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Places.initialize(getApplicationContext(), "AIzaSyCdh_u_JcrvTd7rG-Nz0ZQVk002tEO0EQE");
        PlacesClient placesclient = Places.createClient(this);
        searchView = findViewById(R.id.searchBar);
        submitButton = findViewById(R.id.submitSchoolButton);
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //This is the onclick listener for the places search bar
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                                                            @Override
                                                            public void onError(@NonNull Status status) {
                                                                Log.d(TAG, "there was a problem " + status.toString());
                                                            }

                                                            @Override
                                                            public void onPlaceSelected(@NonNull Place place) {
                                                                highschool = place.getName();
                                                                // TODO: Get info about the selected place.
                                                            }
                                                        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (highschool != null) {
                    String originalHsText = highschool;
                    highschool = highschool.toLowerCase();


                    if (highschool.contains("school") || highschool.contains("college") || highschool.contains("university") || highschool.contains("academy")) {
                        if (highschool.contains("elementary") || highschool.contains("middle")) {
                            Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                            i.putExtra("school", highschool);
                            startActivity(i);
                        } else {
                            String uid = auth.getCurrentUser().getUid();
                            DocumentReference docRef = db.collection("UserData").document(uid);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    data = documentSnapshot.toObject(UserData.class);
                                    data.setHighSchool(highschool);
                                    db.collection("UserData").document(uid).set(data);
                                    Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                                    i.putExtra("message", "High school successfully set to " + originalHsText);
                                    startActivity(i);
                                }
                            });
                        }
                    } else {
                        Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                        i.putExtra("school", highschool);
                        startActivity(i);
                    }
                }
                else{
                    Toast.makeText(ChooseHighSchoolActivity.this, "Please select a highschool first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}