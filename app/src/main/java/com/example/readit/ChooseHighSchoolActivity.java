package com.example.readit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseHighSchoolActivity extends AppCompatActivity {
    ListView listView;
    SearchView searchView;
    ArrayAdapter adapter;
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
        listView = findViewById(R.id.highSchoolList);
        searchView = findViewById(R.id.searchBar);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //Create an adapter with the class list:
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.highSchools));
        if(listView != null) {
            listView.setAdapter(adapter);
        }

        //This handles the searching in the list
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                listView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                listView.setAdapter(adapter);
                return false;
            }
        });
        //When an item in the list is clicked, store their school in the user data.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String school = (String) adapterView.getItemAtPosition(i);
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