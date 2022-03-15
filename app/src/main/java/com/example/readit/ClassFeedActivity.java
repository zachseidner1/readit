package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
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

import static com.example.readit.Test.getContext;

public class ClassFeedActivity extends AppCompatActivity {

    RadioButton tip, question;
    Intent intent;
    String course;
    ListView listView;
    CheckBox highschoolBox;
    String highschool;
    //The following booleans initialize to false:
    boolean isQuestion, isHighschool, isTip;
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
        uid = auth.getCurrentUser().getUid();
        Log.d(TAG, "User's ID: " + uid);


        //Only one is allowed to be selected, so if one is selected deselect the other:
        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isQuestion = false;
                isTip = true;
                if (question.isChecked()) {
                    question.setChecked(false);
                }
                filterFeed(isHighschool, isTip, isQuestion);
            }
        });


        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isQuestion = true;
                isTip = false;
                if (tip.isChecked()) {
                    tip.setChecked(false);
                }
                filterFeed(isHighschool, isTip, isQuestion);
            }
        });


        highschoolBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged: here");
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





//okay so when you add the onclick listener and are switching activities to the viewpost one just send the entire object in the intent and then you can access it there. The post id will be used to match it to the comment id to get the right comments for each post.


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

}