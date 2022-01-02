package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        /*if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
        }*/


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FeedFragment()).commit();

        bottomNavigationView.setSelectedItemId(R.id.navigation_feed);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.navigation_feed:
                        fragment = new FeedFragment();
                        break;
                    case R.id.navigation_new:
                        fragment = new CreateFragment();
                        break;
                    case R.id.navigation_notifications:
                        fragment = new NotificationsFragment();
                        break;
                    case R.id.navigation_settings:
                        fragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

    }

}