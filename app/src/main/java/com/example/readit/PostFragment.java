package com.example.readit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    TextView titleTextView;
    TextView postTextView;
    TextView usernameTextView;
    Button thanksButton;
    Post post;
    CollectionReference postRef;
    DocumentReference userDataRef;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    int postId;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        titleTextView = view.findViewById(R.id.tv_postTitle);
        postTextView = view.findViewById(R.id.tv_post);
        usernameTextView = view.findViewById(R.id.tv_username);
        thanksButton = view.findViewById(R.id.thanksButton);
        Intent intent = getActivity().getIntent();
        postId = intent.getIntExtra("postPicked", 0);
        db = FirebaseFirestore.getInstance();
        postRef = db.collection("Posts");
        mAuth = FirebaseAuth.getInstance();
        userDataRef = db.collection("UserData").document(mAuth.getCurrentUser().getUid());


        //Get the post:
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

                        titleTextView.setText(post.getTitle());
                        postTextView.setText(post.getPost());
                        DocumentReference docRef = db.collection("UserData").document(post.getUid());
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserData data = documentSnapshot.toObject(UserData.class);
                                if(data != null) {
                                    usernameTextView.setText(data.getUsername());
                                } else {
                                    usernameTextView.setText("[deleted]");
                                }
                            }
                        });
                        if(post.getQuestion()){
                            thanksButton.setVisibility(View.INVISIBLE);
                        }

                    }
                });

        //After we get the post we have to know whether or not the user has already liked this post:
        userDataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserData data = documentSnapshot.toObject(UserData.class);
                Log.d(TAG, "onSuccess: User data gotten");
                Log.d(TAG, "onSuccess: " + data.getLikedPostIds().toString());
                if(data.getLikedPostIds().contains(postId)) {
                    //The user has already liked the post so turn the thanks button to "thanks given"
                    thanksButton.setText("thanks given \uD83E\uDD83");
                    thanksButton.setBackgroundColor(Color.GRAY);
                }
            }
        });

        //When the thanks button is clicked:
        thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if the button is in the status to give thanks:
                if (thanksButton.getText().equals("give thanks 🙏")) {

                    //Give the post thanks:
                    thanksButton.setText("thanks given \uD83E\uDD83");
                    thanksButton.setBackgroundColor(Color.GRAY);
                    post.setThanks(post.getThanks()+1);
                    userDataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData data = documentSnapshot.toObject(UserData.class);
                            //So we know the user has liked the post:
                            ArrayList<Integer> likedPostIds = data.getLikedPostIds();
                            likedPostIds.add(postId);
                            db.collection("UserData")
                                .document(post.getUid())
                                .update("thanks", data.getThanks()+1);
                            //Update the user data so we know that the user has thanked the post already:
                            db.collection("UserData")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .update("likedPostIds", likedPostIds);
                        }
                    });
                } else {
                    //Remove the thanks:
                    thanksButton.setText("give thanks 🙏");
                    thanksButton.setBackgroundColor(Color.parseColor("#4285F4"));
                    post.setThanks(post.getThanks()-1);
                    userDataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData data = documentSnapshot.toObject(UserData.class);
                            Log.d(TAG, "onSuccess: user's data: " + documentSnapshot.getId());
                            ArrayList<Integer> likedPostIds = data.getLikedPostIds();
                            likedPostIds.remove(Integer.valueOf(postId));
                            db.collection("UserData")
                                    .document(post.getUid())
                                    .update("thanks", data.getThanks()-1);
                            //Update the user data so we know the user can thank the post again:
                            db.collection("UserData")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .update("likedPostIds", likedPostIds).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: This was successful.");
                                    Log.d(TAG, "onSuccess: " + likedPostIds);
                                }
                            });
                        }
                    });
                }
            }
        });
    return view;
    }
}