package com.example.readit;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassBrowserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassBrowserFragment extends Fragment {

    ListView listView;
    SearchView searchView;
    ArrayAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    public static RecyclerViewAdapter myAdapter;


    public ClassBrowserFragment() {
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
    public static ClassBrowserFragment newInstance(String param1, String param2) {
        ClassBrowserFragment fragment = new ClassBrowserFragment();
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
        return inflater.inflate(R.layout.fragment_class_browser, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView = getActivity().findViewById(R.id.classList);
        searchView = getActivity().findViewById(R.id.searchBar);

        //Create an adapter with the class list:
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.classes));
        if(listView != null) {
            listView.setAdapter(adapter);
        }

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
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, mathClasses);
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
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, englishClasses);
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
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, socialStudiesClasses);
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
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, scienceClasses);
                listView.setAdapter(adapter);
                break;
            default:
                //Debug statement below:
//                Log.d(TAG, "applyCustomFilter: " + s.toLowerCase().trim());
                adapter.getFilter().filter(s);
                listView.setAdapter(adapter);
        }
    }
}