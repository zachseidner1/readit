package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.readit.Test.getContext;

public class ClassFeedActivity extends AppCompatActivity {

    RadioButton tip, question;
    Intent intent;
    String course;
    ListView listView;
    CheckBox highschoolBox;
    String highschool;
    SearchView searchView;
    //The following booleans initialize to false:
    boolean isQuestion, isHighschool, isTip, isSearched;
    ArrayAdapter adapter;
    ArrayList<Post> postList = new ArrayList<>();
    ArrayList<Post> tempList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference postRef;
    FirebaseAuth auth;
    String uid;
    private static final String TAG = "ClassFeedActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_feed);

        tip = findViewById(R.id.tipButtonFeed);
        question = findViewById(R.id.questionButtonFeed);
        highschoolBox = findViewById(R.id.checkBoxHighSchool);
        intent = getIntent();
        course = intent.getStringExtra("classPicked");
        listView = findViewById(R.id.classFeed);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        postRef = db.collection("Posts");
        searchView = findViewById(R.id.searchBar2);
        uid = auth.getCurrentUser().getUid();
        Log.d(TAG, "User's ID: " + uid);


        //Only one is allowed to be selected, so if one is selected deselect the other:
        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSearched) {
                    tip.setChecked(false);
                    Toast.makeText(ClassFeedActivity.this, "You can't use these filters while making a search.", Toast.LENGTH_SHORT).show();
                } else {
                    isQuestion = false;
                    isTip = true;
                    if (question.isChecked()) {
                        question.setChecked(false);
                    }
                    filterFeed(isHighschool, isTip, isQuestion);
                }
            }
        });


        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSearched) {
                    question.setChecked(false);
                    Toast.makeText(ClassFeedActivity.this, "You can't use these filters while making a search.", Toast.LENGTH_SHORT).show();
                } else {
                    isQuestion = true;
                    isTip = false;
                    if (tip.isChecked()) {
                        tip.setChecked(false);
                    }
                    filterFeed(isHighschool, isTip, isQuestion);
                }
            }
        });


        highschoolBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged: here");
                if(isSearched) {
                    highschoolBox.setChecked(false);
                    Toast.makeText(ClassFeedActivity.this, "You can't use these filters while making a search.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (highschoolBox.isChecked()) {
                        Log.d(TAG, "onCheckedChanged: user data " + uid);
                        db.collection("UserData").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Log.d(TAG, "onSuccess: was success");
                                UserData data = documentSnapshot.toObject(UserData.class);
                                highschool = data.getHighSchool();
                                Log.d(TAG, "onSuccess: " + highschool);
                                if (highschool == null) {
                                    Toast.makeText(getBaseContext(), "Please select your high school in settings.", Toast.LENGTH_SHORT).show();
                                    highschoolBox.setChecked(false);
                                } else {
                                    isHighschool = true;
                                    filterFeed(true, isTip, isQuestion);
                                }
                            }
                        });
                    }
                    else {
                        Log.d(TAG, "onCheckedChanged: not filtering by hs");
                        Log.d(TAG, "onCheckedChanged: is tip " + isTip );
                        Log.d(TAG, "onCheckedChanged: is question " + isQuestion);
                        isHighschool = false;
                        filterFeed(false, isTip, isQuestion);
                    }
                }
            }
        });



        postRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "got here " + course);
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            if (documentSnapshot.exists()) {

                                Post post = documentSnapshot.toObject(Post.class);
                                Log.d(TAG, "got here 2 " + post.getCourse());
                                if (post.getCourse().equals(course)) {
                                    postList.add(documentSnapshot.toObject(Post.class));
                                }
                            }
                        }
                        tempList.addAll(postList);
                        for(Post post : postList)
                        {
                            titleList.add(post.getTitle());
                        }
                        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, titleList);
                        listView.setAdapter(adapter);
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = listView.getItemAtPosition(i).toString();
                int id = -1;
                for(Post post : postList) {
                    if(post.getTitle().equals(name))
                    {
                        id = post.getPostId();
                    }
                }
                Intent intent = new Intent(Test.getContext(), ViewPostActivity.class);
                intent.putExtra("postPicked", id);
                startActivity(intent);
            }
        });

        //When the SearchView is focused, uncheck all other filters and only focus on search.
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d(TAG, "onClick: Search clicked");
                highschoolBox.setChecked(false);
                tip.setChecked(false);
                isTip = false;
                question.setChecked(false);
                isQuestion = false;
                filterFeed(false,false,false);
            }
        });
        //When the search view is closed, clear the search filter.
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isSearched = false;
                for (Post post :
                        postList) {
                    titleList.add(post.getTitle());
                }
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                return false;
            }
        });
        //When they begin to make a search:
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //When the search is submitted, update the post list.
            public boolean onQueryTextSubmit(String query) {
                isSearched = true;
                question.setChecked(false);
                highschoolBox.setChecked(false);
                tip.setChecked(false);
                ArrayList<Post> filteredPosts = filteredPosts(postList, query);
                titleList.clear();
                for (Post post :
                        filteredPosts) {
                    titleList.add(post.getTitle());
                }
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                return false;
            }
            //When the text is changed, check if they deleted their search.
            //If they did, update the post list.
            @Override
            public boolean onQueryTextChange(String newText) {
                titleList.clear();
                Log.d(TAG, "onQueryTextChange: Text changed");
                if(TextUtils.isEmpty(newText.trim())) {
                    isSearched = false;
                    for (Post post :
                            postList) {
                        titleList.add(post.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    public void filterFeed(boolean hs, boolean t, boolean q){
        titleList.clear();
        tempList.clear();
        for(Post post : postList){
            if(hs){
                if(post.getHighSchool() == null || !post.getHighSchool().equals(highschool))    //not match also check if it's null
                    continue;
            }
            if(q){
                if(!post.getQuestion()) //not match
                    continue;

            }
            if(t){
                if(post.getQuestion()) //not match
                    continue;
            }
            tempList.add(post);
        }
        for(Post post : tempList){
            String title = post.getTitle();
            titleList.add(title);
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter); //update the list view
    }

    /**
     * This is a function made to filter posts specifically based on user input.
     * It uses a custom filtering algorithm to sort posts based on how similar they were to the user's query.
     * @param posts  the possible posts that the query could show.
     * @param query  from the user's inputted query
     * @return  an ArrayList containing the ordered, filtered posts as a result of the algorithm.
     */
    public static ArrayList<Post> filteredPosts (ArrayList<Post> posts, String query){
        ArrayList<Post> postsCopy = (ArrayList<Post>) posts.clone();
        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<Map.Entry<Integer, Post>> postScorePairs = new ArrayList<>();
        Iterator<Post> itr = postsCopy.iterator();

        while(itr.hasNext()) {
//            System.out.println("here");
            Post post = itr.next();
            int score = 0;
            List<String> contents = Arrays.asList((post.getTitle().toLowerCase() + " " + post.getPost().toLowerCase()).split(" "));
            String[] words = query.split(" ");
            for (int i = 0; i < words.length; i++) {
                if(contents.contains(words[i].toLowerCase())) {
                    System.out.println("TRUE: " +contents + " WORD: " + words[i]  );
                    score++;
                }
            }
            if(score < 1) {
                itr.remove();
            }
            else {
                int finalScore = score;
                System.out.println("FINAL SCORE OF " + contents + " :"  + score);
                postScorePairs.add(new Map.Entry<Integer, Post>() {
                    @Override
                    public Integer getKey() {
                        return finalScore;
                    }

                    @Override
                    public Post getValue() {
                        return post;
                    }

                    @Override
                    public Post setValue(Post value) {
                        return null;
                    }
                });
            }
        }

        postScorePairs.sort(new Comparator<Map.Entry<Integer, Post>>() {
            @Override
            public int compare(Map.Entry<Integer, Post> o1, Map.Entry<Integer, Post> o2) {
                return Integer.compare(o1.getKey(), o2.getKey());
            }
        });
        ArrayList<Post> filteredPosts = new ArrayList<>();
        for (Map.Entry<Integer, Post> entry :
                postScorePairs) {
            filteredPosts.add(entry.getValue());
        }
        Collections.reverse(filteredPosts);
        return filteredPosts;
    }
}