package com.example.jose.mymavmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TabCreateSchedule extends Fragment {

    private MyMavDataSource mymav;
    private View rootView;
    private EditText editScheduleName;
    private String nameSchedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_create_schedule, container, false);

        // Call for backthread to keep database open
        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        Button buttonAddSchedule = (Button) rootView.findViewById(R.id.buttonAddSchedule);
        editScheduleName = (EditText) rootView.findViewById(R.id.editNameSchedule);

        //create new schedule
        buttonAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameSchedule = editScheduleName.getText().toString();
                if (!nameSchedule.isEmpty()) {
                    Toast.makeText(rootView.getContext(), String.format("Created %s schedule", nameSchedule), Toast.LENGTH_SHORT).show();
                    try {
                        mymav.createSchedule(nameSchedule);
                        editScheduleName.setText("");
                    } catch (Exception e) {
                        Toast.makeText(rootView.getContext(), "Schedule name in use!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        mymav.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        mymav.close();
        super.onPause();
    }

    @Override
    public void onStop() {
        mymav.close();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mymav.close();
        super.onDestroy();
    }
}
