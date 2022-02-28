package com.example.readit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.example.readit.NotificationSettingsActivity.SHARED_PREFS;
import static com.example.readit.NotificationSettingsActivity.SHOW_REPLIES;
import static com.example.readit.NotificationSettingsActivity.SHOW_THANKS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore db;
    ArrayList<String> notifications;
    CollectionReference postRef, commentRef;
    ListView listView;
    SearchView searchView;
    String uid;
    ArrayAdapter adapter;
    HashMap<String, Integer> postPostIdHashMap; //Will be used to easily access postId from post title.
    SharedPreferences mPrefs;
    boolean showThanks, showReplies;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "NotificationsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = getView().findViewById(R.id.searchBar);

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
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        db =  FirebaseFirestore.getInstance();
        notifications = new ArrayList<>();
        postRef = db.collection("Posts");
        commentRef = db.collection("Comments");
        uid = auth.getCurrentUser().getUid();
        listView = getActivity().findViewById(R.id.notificationList);
        postPostIdHashMap = new HashMap<>();

        //load notification settings
        mPrefs = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        showThanks = mPrefs.getBoolean(SHOW_THANKS, true);
        showReplies = mPrefs.getBoolean(SHOW_REPLIES, true);

        //First check for any significant number of likes to be displayed:
        postRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Post post = documentSnapshot.toObject(Post.class);
                                if (post.getUid().equals(uid)) {

                                    if (showThanks && post.getThanks() >= 10) {
                                        notifications.add("Your tip titled \" " + post.getTitle() + " \" hit " + post.getThanks() + " thanks 🙏");
                                        postPostIdHashMap.put(post.getTitle(), post.getPostId());
                                    }
                                    if (showReplies && post.getComments() > 0) {
                                        notifications.add("Your question titled \" " + post.getTitle() + " \" has " + post.getComments() + " replies 😯");
                                        postPostIdHashMap.put(post.getTitle(), post.getPostId());
                                    }
                                }
                            }
                        }
                        if (notifications.isEmpty()) {
                            notifications.add("You have no notifications right now 😐");
                        } else {
                            notifications.sort(String::compareTo);
                        }
                        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, notifications);
                        if(listView != null) {
                            listView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "onSuccess: list view is null");
                        }

                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO send user to that post.
                //Gets clicked on notification text:
                String notification = (String) listView.getItemAtPosition(i);
                //Extracts postTitle from notification text:
                String postTitle = notification.substring(notification.indexOf("\"")+1, notification.lastIndexOf("\"")).trim();
                //Get postId from title:
                int postId = postPostIdHashMap.getOrDefault(postTitle, 0);

                Log.d(TAG, "onItemClick: " + postId);
                if(postId == 0) {
                    Toast.makeText(getContext(), "Well, this is awkward. An error occurred and we couldn't find the post associated with this notification.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getContext(), ViewPostActivity.class);
                    intent.putExtra("postPicked", postId);
                    startActivity(intent);
                }

            }
        });
    }
}

