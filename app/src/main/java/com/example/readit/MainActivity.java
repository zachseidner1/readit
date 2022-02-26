package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

public class MainActivity extends AppCompatActivity  {

    BottomNavigationView bottomNavigationView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().hasExtra("message")) {
            Toast.makeText(this, getIntent().getStringExtra("message"), Toast.LENGTH_SHORT).show();
        }

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ClassBrowserFragment()).commit();

        bottomNavigationView.setSelectedItemId(R.id.navigation_feed);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.navigation_feed:
                        fragment = new ClassBrowserFragment();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch(item.getItemId()){
//            case R.id.menu_aToz:
//                //sort a to z
//                Collections.sort(MyApplication.postList, Post.PostNameAZComparator);
////                ClassBrowserFragment.myAdapter.notifyDataSetChanged();
//                Toast.makeText(MainActivity.this, "Sort A to Z", Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.menu_zToa:
//                //sort z to a
//                Collections.sort(MyApplication.postList, Post.PostNameZAComparator);
////                ClassBrowserFragment.myAdapter.notifyDataSetChanged();
//                Toast.makeText(MainActivity.this, "Sort Z to A", Toast.LENGTH_LONG).show();
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}