package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//This is the activity for actually viewing the posts


public class ViewPostActivity extends AppCompatActivity {

    TextView textView;
    boolean canComment;
    FirebaseAuth auth;
    ViewPager2 viewPager2;
    Post post;
    CollectionReference commentRef;
    PagerAdapter pagerAdapter;
    ArrayAdapter adapter;
    FirebaseFirestore db;
    PostFragment fragment = new PostFragment();
    int postId;
    TabLayout tabLayout;
    CollectionReference postRef;
    ArrayList<Comment> comments = new ArrayList<>();
    ArrayList<String> commentsTitle = new ArrayList<>();
    private static final String TAG = "ViewPostActivity";


    //just like move all of this stuff over to the individual fragments and use this tutorial to help with tying it all together, i don't feel like working on this anymore but I think I have a solid plan moving forward.
    //tutorial link https://www.youtube.com/watch?v=SUvzMjDYg80

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        auth = FirebaseAuth.getInstance();
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        canComment = true;
        FragmentManager fm = getSupportFragmentManager();
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        postId = intent.getIntExtra("postPicked", 0);
        postRef = db.collection("Posts");


        postRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot.get("postId").toString().equals("" + postId)) {
                                    post = documentSnapshot.toObject(Post.class);
                                }
                            }
                        }
                        if (post.getQuestion()) {


                            pagerAdapter = new PagerAdapter(fm, getLifecycle());
                            viewPager2.setAdapter(pagerAdapter);
                            tabLayout.addTab(tabLayout.newTab().setText("post"));
                            tabLayout.addTab(tabLayout.newTab().setText("comments"));

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    viewPager2.setCurrentItem(tab.getPosition());

                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });


                            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                @Override
                                public void onPageSelected(int position){
                                    tabLayout.selectTab(tabLayout.getTabAt(position));
                                }

                            });
                        } else {
                            pagerAdapter = new PagerAdapter(fm, getLifecycle());
                            viewPager2.setAdapter(pagerAdapter);
                            tabLayout.addTab(tabLayout.newTab().setText("Post"));

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    viewPager2.setCurrentItem(tab.getPosition());
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });
                            tabLayout.setVisibility(View.INVISIBLE);
                            viewPager2.setUserInputEnabled(false);
                        }
                    }
                });


    }
}