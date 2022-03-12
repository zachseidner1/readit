package com.example.readit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    CollectionReference userDataRef;
    FirebaseFirestore db;
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
        userDataRef = db.collection("UserData");





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
                                usernameTextView.setText(data.getUsername());
                            }
                        });
                        if(post.getQuestion()){
                            thanksButton.setVisibility(View.INVISIBLE);
                        }

                    }
                });


//this still doesn't update the user's thanks it only does the post.
        thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thanksButton.getText().equals("give thanks 🙏")) {
                    thanksButton.setText("thanks given \uD83E\uDD83");
                    post.setThanks(post.getThanks()+1);
                    DocumentReference docRef = db.collection("UserData").document(post.getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData data = documentSnapshot.toObject(UserData.class);
                            db.collection("UserData")
                                    .document(post.getUid())
                                    .update("thanks", data.getThanks()+1);
                        }
                    });
                } else {
                    thanksButton.setText("give thanks 🙏");
                    post.setThanks(post.getThanks()-1);
                    DocumentReference docRef = db.collection("UserData").document(post.getUid());
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserData data = documentSnapshot.toObject(UserData.class);
                            db.collection("UserData")
                                    .document(post.getUid())
                                    .update("thanks", data.getThanks()-1);
                        }
                    });
                }
            }
        });


return view;
    }
}