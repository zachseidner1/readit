package com.example.readit;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    ListView listView;
    String[] mSettingsTypes = {"account", "notifications", "about", "logout"};
    int[] mIconIds = {R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_notifications_24, R.drawable.ic_baseline_info_24, R.drawable.ic_baseline_exit_to_app_24};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        listView = getActivity().findViewById(R.id.listView);

        MyAdapter adapter = new MyAdapter(getContext(), mSettingsTypes, mIconIds);

        listView.setAdapter(adapter);

        //When a settings item is clicked:
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){ //See which settings option they clicked:
                    case 0:
                        Intent intent1 = new Intent(getContext(), AccountActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        //TODO Switch to Notifications activity.
                        break;
                    case 2:
                        //TODO Switch to about activity
                        break;
                    case 3:
                        //Logout user.
                        FirebaseAuth.getInstance().signOut();
                        Intent intent4 = new Intent(getActivity(), WelcomeActivity.class);
                        startActivity(intent4);
                        break;
                    default:
                        Toast.makeText(getContext(), "An error has occurred. Please try again.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String[] rTitle;
        int[] rIcon;

        MyAdapter (Context context, String title[], int[] icon){
            super(context, R.layout.settings_item, R.id.textViewItem, title);
            rTitle = title;
            rIcon = icon;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.settings_item, parent, false);
            ImageView icons = row.findViewById(R.id.imageViewItem);
            TextView title = row.findViewById(R.id.textViewItem);

            //now set our resources on views
            icons.setImageResource(rIcon[position]);
            title.setText(rTitle[position]);

            if(position == 3) {
                title.setTextColor(Color.parseColor("#ff0000"));
            }

            return row;
        }
    }
}