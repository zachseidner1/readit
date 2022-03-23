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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class ChooseHighSchoolActivity extends AppCompatActivity {
    ListView listView;
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
        listView = findViewById(R.id.highSchoolList);
        searchView = findViewById(R.id.searchBar);
        submitButton = findViewById(R.id.submitSchoolButton);
        autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //Create an adapter with the class list:
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.highSchools));
        if(listView != null) {
            listView.setAdapter(adapter);
        }

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

        //This handles the searching in the list
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                adapter.getFilter().filter(s);
//                listView.setAdapter(adapter);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapter.getFilter().filter(s);
//                listView.setAdapter(adapter);
//                return false;
//            }
//        });
        //When an item in the list is clicked, store their school in the user data.

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (highschool != null) {
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
                                    i.putExtra("message", "High school successfully set to " + highschool);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String school = adapterView.getItemAtPosition(i).toString().toLowerCase();
                String uid = auth.getCurrentUser().getUid();
                DocumentReference docRef = db.collection("UserData").document(uid);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        data = documentSnapshot.toObject(UserData.class);
                        data.setHighSchool(school);
                        db.collection("UserData").document(uid).set(data);
                        Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                        i.putExtra("message", "High school successfully set to " + school);
                        startActivity(i);
                    }
                });

            }
        });


        //TODO add subtext under user's name to show their high school (?)
    }
}