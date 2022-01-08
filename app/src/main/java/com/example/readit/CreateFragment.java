package com.example.readit;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {

    EditText titleText;
    EditText postText;
    Spinner classSpinner;
    TextView counterText;
    TextView counterText2;
    Button submitButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
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
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        titleText = getActivity().findViewById(R.id.newPostTitleText);
        postText = getActivity().findViewById(R.id.newPostText);
        counterText = getActivity().findViewById(R.id.counterText);
        counterText2 = getActivity().findViewById(R.id.counterText2);
        submitButton = getActivity().findViewById(R.id.submitButton);
        submitButton.setBackgroundColor(Color.GRAY);

        //Update post text counter and change clickable of "next" button
        postText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = charSequence.length();
                if(length > 0  && titleText.getText().length() > 0) {
                    submitButton.setClickable(true);
                    submitButton.setBackgroundColor(Color.parseColor("#4285F4"));
                }
                else {
                    submitButton.setClickable(false);
                    submitButton.setBackgroundColor(Color.GRAY);
                }
                String lengthLimit = length + "/1000";
                counterText2.setText(lengthLimit);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Update title text counter and change clickable of "next" button
        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = charSequence.length();
                if(length > 0  && postText.getText().length() > 0) {
                    submitButton.setClickable(true);
                    submitButton.setBackgroundColor(Color.parseColor("#4285F4"));
                }
                else {
                    submitButton.setClickable(false);
                    submitButton.setBackgroundColor(Color.GRAY);
                }
                String lengthLimit = length + "/200";
                counterText.setText(lengthLimit);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submitButton.isClickable()) {
                    //TODO switch to post submission activity
                }
            }
        });
    }
}