package com.example.readit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Arrays;

public class PostConfirmationActivity extends AppCompatActivity {
    RadioButton tip, question;
    ListView listView;
    SearchView searchView;
    ArrayAdapter adapter;
    TextView selectedCourse, changeCourse, errorText;
    String course, highSchool, uid;
    ImageView errorIcon;
    CheckBox includeHighSchool;
    boolean isQuestion, typeSelected, courseError, typeError, canSubmit = true;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_confirmation);

        tip = findViewById(R.id.tipButton);
        question = findViewById(R.id.questionButton);
        selectedCourse = findViewById(R.id.selectedClassText);
        changeCourse = findViewById(R.id.changeClassText);
        errorText = findViewById(R.id.errorText);
        errorIcon = findViewById(R.id.errorIcon);
        includeHighSchool = findViewById(R.id.checkbox);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Only one is allowed to be selected, so if one is selected deselect the other:

        tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.isChecked()){
                    question.setChecked(false);
                    isQuestion = false;
                }

                typeSelected = true;
                if(typeError) {
                    errorText.setVisibility(View.INVISIBLE);
                    errorIcon.setVisibility(View.INVISIBLE);
                    typeError = false;
                }
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tip.isChecked()){
                    tip.setChecked(false);
                    isQuestion = true;
                    typeError = false;
                }
                typeSelected = true;
                if(typeError) {
                    errorText.setVisibility(View.INVISIBLE);
                    errorIcon.setVisibility(View.INVISIBLE);
                    typeError = false;
                }
            }
        });

        includeHighSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("UserData").document(uid);

                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserData userData = documentSnapshot.toObject(UserData.class);
                        highSchool = userData.getHighSchool();
                        if(highSchool == null) {
                            Toast.makeText(PostConfirmationActivity.this, "Select your high school in account settings to enable this.", Toast.LENGTH_SHORT).show();
                            includeHighSchool.setChecked(false);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView = findViewById(R.id.classList);
        searchView = findViewById(R.id.searchBar);
        db = FirebaseFirestore.getInstance();

        //Create an adapter with the class list:
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.classes));
        if(listView != null) {
            listView.setAdapter(adapter);
        }

        //This handles the searching in the list using a custom filter.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                applyCustomFilter(adapter, s, listView);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                applyCustomFilter(adapter, s, listView);
                return false;
            }
        });

        //When a list item is clicked we want to show the class that they selected.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                course = (String)adapterView.getItemAtPosition(i); //I know everything in the list is a string so this is fine.
                listView.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                selectedCourse.setVisibility(View.VISIBLE);
                changeCourse.setVisibility(View.VISIBLE);
                String courseText = "selected class: " + course;
                selectedCourse.setText(courseText);
                if(courseError) {
                    errorText.setVisibility(View.INVISIBLE);
                    errorIcon.setVisibility(View.INVISIBLE);
                    courseError = false;
                }
            }
        });

        //If they want to change their selected course we need to show the list view and search bar again.
        changeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course = null;
                listView.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                selectedCourse.setVisibility(View.GONE);
                changeCourse.setVisibility(View.GONE);
            }
        });
    }

    /**
     * This function allows for a custom filter to make searching more convenient for the user.
     * For example when the user types "math", it will show them all the math classes, even if those
     * classes don't have the word "math" in them.
     *
     * @param adapter This is the default adapter before custom filtering
     * @param s This is the user's inputted string that filters the array
     * @param listView This is the listView that the filter gets applied to
     * @return Nothing.
     */
    public void applyCustomFilter(ArrayAdapter adapter, String s, ListView listView){
        switch(s.toLowerCase().trim()) {
            case "math":
                String[] mathClasses = {
                        "Algebra I",
                        "Algebra II",
                        "Geometry",
                        "Trigonometry",
                        "Pre Calculus",
                        "AP Calculus AB",
                        "AP Calculus BC",
                        "Statistics"
                };
                Arrays.sort(mathClasses);
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mathClasses);
                listView.setAdapter(adapter);
                break;
            case "english":
            case "language arts":
                String[] englishClasses = new String[] {
                        "AP language and composition",
                        "AP literature and composition",
                        "American literature",
                        "British literature",
                        "Comparative literature",
                        "Contemporary literature",
                        "World literature",
                        "English (9th grade)",
                        "English (10th grade)",
                        "English (11th grade)",
                        "English (12th grade)",
                        "Creative writing",
                        "Journalism",
                        "Rhetoric",
                        "Composition",
                        "Poetry",
                        "Debate"
                };
                Arrays.sort(englishClasses);
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, englishClasses);
                listView.setAdapter(adapter);
                break;
            case "history":
            case "social studies":
                String[] socialStudiesClasses = new String[] {
                        "AP world history",
                        "AP european history",
                        "AP US government and politics",
                        "AP US history",
                        "US history",
                        "World history",
                        "European history",
                        "Political science"
                };
                Arrays.sort(socialStudiesClasses);
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, socialStudiesClasses);
                listView.setAdapter(adapter);
                break;
            case "science":
                String[] scienceClasses = new String[] {
                        "AP biology",
                        "AP chemistry",
                        "AP Environmental Science",
                        "AP physics C",
                        "AP physics 1",
                        "AP physics 2",
                        "Earth science",
                        "Physical science",
                        "Biology",
                        "Chemistry",
                        "Organic chemistry",
                        "Physics",
                        "Life science",
                        "Environmental science",
                        "Astronomy",
                        "Zoology",
                        "Oceanography",
                        "Forensic science",
                        "Botany",
                        "Political science"
                };
                Arrays.sort(scienceClasses);
                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, scienceClasses);
                listView.setAdapter(adapter);
                break;
            default:
                //Debug statement below:
//                Log.d(TAG, "applyCustomFilter: " + s.toLowerCase().trim());
                adapter.getFilter().filter(s);
                listView.setAdapter(adapter);
        }
    }

    /**
     * This function is responsible for what happens when the user attempts to submit a post.
     * If the post has everything it needs the user will submit the post and be brought back to the main activity.
     *
     * @param view the button that caused the method
     */
    public void onSubmit(View view) {
        if(course != null) {
            if (typeSelected) {
                view.setClickable(false);
                errorText.setVisibility(View.INVISIBLE);
                errorIcon.setVisibility(View.INVISIBLE);
                if (canSubmit) {
                    String title = getIntent().getStringExtra("title");
                    String postText = getIntent().getStringExtra("post");
                    //We have the course from what they selected in the array.

                    //Get the user's high school:
                    if(includeHighSchool.isChecked()) {
                        DocumentReference docRef = db.collection("UserData").document(uid);
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserData userData = documentSnapshot.toObject(UserData.class);
                                highSchool = userData.getHighSchool();
                            }
                        });
                    }

                    //We have whether or not it is a question in isQuestion.
                    Post post = new Post (title, postText, course, highSchool, isQuestion, uid);
                    db.collection("Posts")
                            .add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("message", "Post successfully uploaded.");
                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PostConfirmationActivity.this, "Please try again. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                }
                            });
                }
            } else {
                errorIcon.setVisibility(View.VISIBLE);
                String error = "You need to select whether the post is a tip or a question.";
                errorText.setText(error);
                errorText.setVisibility(View.VISIBLE);
                typeError = true;
            }
        } else {
            errorIcon.setVisibility(View.VISIBLE);
            String error = "You need to select a class for the post.";
            errorText.setText(error);
            errorText.setVisibility(View.VISIBLE);
            courseError = true;
        }
    }
}