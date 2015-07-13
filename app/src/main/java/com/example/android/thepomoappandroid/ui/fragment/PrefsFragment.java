package com.example.android.thepomoappandroid.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.thepomoappandroid.R;
import com.example.android.thepomoappandroid.db.DBHandler;

/**
 * Created by hanric on 6/7/15.
 */
public class PrefsFragment extends Fragment
    implements View.OnClickListener {

    private DBHandler dbHandler;

    public static PrefsFragment newInstance() {
        PrefsFragment fragment = new PrefsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prefs, container, false);

        findViews(view);
        return view;
    }

    private void findViews(View view) {

    }

    @Override
    public void onClick(View view) {

    }
}
