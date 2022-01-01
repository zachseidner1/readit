package com.example.readit;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        //fillPostList();
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        ArrayList<Post> postList  = myApplication.getPostList();
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.lv_postList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(postList, getContext());
        recyclerView.setAdapter(myAdapter);
        return view;
    }


    //This is just a temporary way to add posts to make sure that the feed is working, eventually this will be on firebase unless it's not supposed to be then it won't be lol
//    private void fillPostList() {
//
//        Post p0 = new Post("Super epic math tip", "Hey guys here is my tip, get gud at math kid or uninstall idk what else to tell ya.", "https://www.pngkit.com/png/detail/403-4037364_6848425-integral-symbol.png");
//        Post p1 = new Post("How to get the same grade on all of Mr. Hillman's assignments", "Step 1: Do nothing", "https://www.adazing.com/wp-content/uploads/2019/02/open-book-clipart-03.png");
//        Post p2 = new Post("How to not fall asleep in Mr. Pring's class", "I have no idea I just made this post to get thanks.", "https://media.istockphoto.com/vectors/courthouse-icon-flat-vector-template-design-trendy-vector-id1222068323?k=20&m=1222068323&s=612x612&w=0&h=XcravWfpjswLGbJr4lRuvvFdp1IXHwvw8p-3dGXKh8Q=");
//        Post p3 = new Post("Quizlet for new Spanish Verbs", "Hey, here is a quizlet for the verbs we just learned", "https://cdn.britannica.com/36/4336-004-6BD81071/Flag-Spain.jpg");
//        Post p4 = new Post("Physics equations to know for the test", "Do you think I know physics lol", "https://media.istockphoto.com/vectors/physics-illustration-vector-id615915320?s=612x612");
//        Post p5 = new Post("Bio tip: I ran out of creativity", "bio post", "https://process.filepicker.io/APHE465sSbqvbOIStdwTyz/rotate=deg:exif/resize=fit:crop,height:283,width:472/output=quality:80,compress:true,strip:true,format:jpg/cache=expiry:max/https://cdn.filestackcontent.com/IUWL051KTdSg5UDGaV1h");
//        Post p6 = new Post("Stat tip", "Mr. Morgan here", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Standard_Normal_Distribution.png/1200px-Standard_Normal_Distribution.png" );
//        Post p7 = new Post("Chem tip", "chemistry", "https://www.thoughtco.com/thmb/EpsKarnLz-v0VXY0KKnS_fTX5-U=/2121x1414/filters:fill(auto,1)/GettyImages-545286316-433dd345105e4c6ebe4cdd8d2317fdaa.jpg");
//
//        postList.addAll(Arrays.asList(new Post []{p0, p1, p2, p3, p4, p5, p6, p7}));
//    }



}