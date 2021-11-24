package com.example.readit;

import android.content.Context;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

public class ExtendedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private ArrayList<TextWatcher> mWatchers = null;

    public ExtendedEditText(Context context) {
        super(context);
    }

    public void addTextChangedListener(TextWatcher watcher){
        if (mWatchers == null){
            mWatchers = new ArrayList<>();
        }
        mWatchers.add(watcher);
    }

    public void clearTextChangedListeners(){
        if(mWatchers != null){
            for(TextWatcher watcher: mWatchers){
                super.removeTextChangedListener(watcher);
            }

            mWatchers.clear();
            mWatchers = null;
        }
    }

}
