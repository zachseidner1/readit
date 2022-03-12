package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment {

    EditText commentEditText;
    Button submitButton;
    ListView commentList;
    FirebaseFirestore db;
    int postId;
    boolean canThank= true;
    FirebaseAuth auth;
    ArrayAdapter adapter;
    CollectionReference commentRef;
    CollectionReference userDataRef;
    ArrayList<Comment> comments = new ArrayList<>();
    ArrayList<String> commentsTitle = new ArrayList<>();
    private static final String TAG = "CommentFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        commentEditText = view.findViewById(R.id.et_comment);
        commentList = view.findViewById(R.id.lv_comment);
        auth = FirebaseAuth.getInstance();
        Intent intent = getActivity().getIntent();
        postId = intent.getIntExtra("postPicked", 0);
        submitButton = view.findViewById(R.id.submitCommentButton);
        db = FirebaseFirestore.getInstance();
        commentRef = db.collection("Comments");
        userDataRef = db.collection("UserData");
        submitButton.setVisibility(View.VISIBLE);





        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (canThank) {
                    DocumentReference docRef = db.collection("UserData").document(comments.get(i).getUsername());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData data = documentSnapshot.toObject(UserData.class);
                            db.collection("UserData")
                                    .document(comments.get(i).getUsername())
                                    .update("thanks", data.getThanks() + 1);
                        }
                    });
                    Toast.makeText(getContext(), "thanks given \uD83E\uDD83", Toast.LENGTH_LONG).show();
                    canThank = false;
                }
                else{
                    DocumentReference docRef = db.collection("UserData").document(comments.get(i).getUsername());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData data = documentSnapshot.toObject(UserData.class);
                            db.collection("UserData")
                                    .document(comments.get(i).getUsername())
                                    .update("thanks", data.getThanks() - 1);
                        }
                    });
                    Toast.makeText(getContext(), "thanks removed \uD83D\uDE1E", Toast.LENGTH_LONG).show();
                    canThank = true;
                }
            }
        });

        //make this update the list real time
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(commentEditText.getText())) {
                    Toast.makeText(getContext(), "You can not submit an empty comment", Toast.LENGTH_LONG).show(); //this doesn't work anymore for some reason
                }
                else{
                    commentEditText.getText().clear();
                    Comment comment = new Comment(commentEditText.getText().toString(), postId, auth.getCurrentUser().getUid(), 0);
                    db.collection("Comments")
                            .add(comment)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getContext(), "Comment successfully submitted.", Toast.LENGTH_LONG).show();
                                    submitButton.setVisibility(View.INVISIBLE);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        commentRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Comment comment1 = documentSnapshot.toObject(Comment.class);
                                if (comment1.getPostID() == postId) {
                                    comments.add(documentSnapshot.toObject(Comment.class));
                                    for(Comment comment : comments)
                                    {
                                        DocumentReference docRef = db.collection("UserData").document(comment.getUsername());
                                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                UserData data = documentSnapshot.toObject(UserData.class);
                                                if(!commentsTitle.contains("\"" + comment.getComment() + "\"" + " - " + data.getUsername())){
                                                    commentsTitle.add("\"" + comment.getComment() + "\"" + " - " + data.getUsername());
                                                    adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, commentsTitle);
                                                    commentList.setAdapter(adapter);
                                                    Log.d(TAG, "comment added");
                                                }
                                            }
                                        });

                                    }

                                }
                            }
                            }
                        }
                    });
                }
    }

