package com.example.jose.mymavmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StudentCenterFragment extends Fragment {

    private MyMavDataSource mymavdata;
    private TextView textActiveSchedule;
    private TextView textTotalSchedules;
    private View rootview;
    private TextView name, email;
    private final String TAG = "TKT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_student_center, container, false);

        mymavdata = new MyMavDataSource(rootview.getContext());
        mymavdata.open();

        textTotalSchedules = (TextView) rootview.findViewById(R.id.textTotalSchedulesNumber);
        try {
            textTotalSchedules.setText(String.format("%d", mymavdata.getScheduleNames().size()));
        } catch (Exception e) {

        }
        textActiveSchedule = (TextView) rootview.findViewById(R.id.textActiveScheduleNameSC);
        String active = mymavdata.getActiveSchedule();
        if (active != null) {
            textActiveSchedule.setText(active);
        }

        name = (TextView) rootview.findViewById(R.id.fullNameView);
        name.setText(mymavdata.getName());
        email = (TextView) rootview.findViewById(R.id.emailView);
        email.setText(mymavdata.getEmail());

        //TODO: get students current schedule w/ lists of classes
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
