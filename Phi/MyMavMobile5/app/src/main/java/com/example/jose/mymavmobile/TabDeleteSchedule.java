package com.example.jose.mymavmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TabDeleteSchedule extends Fragment {

    private View rootView;
    private MyMavDataSource mymav;
    private TextView textActiveSchedule;
    private ListView listView;
    private List<String> schedules;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_delete_schedule, container, false);
        Button buttonDeleteSchedule = (Button) rootView.findViewById(R.id.buttonDeleteSchedule);

        // Call for back thread to keep database open
        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        schedules = new ArrayList<>();
        listView = (ListView) rootView.findViewById(android.R.id.list);

        arrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        schedules = mymav.getScheduleNames();

        arrayAdapter.clear();
        try {
            arrayAdapter.addAll(schedules);
        } catch (Exception e) {
        }

        //action for delete schedule button
        buttonDeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int scheduleNum = listView.getCheckedItemPosition();
                    String schedule = schedules.get(scheduleNum);

                    mymav.deleteSchedule(schedule);
                    schedules.remove(schedule);
                    listView.setItemChecked(scheduleNum, false);
                    arrayAdapter.clear();
                    try {
                        arrayAdapter.addAll(schedules);
                    } catch (Exception e) {
                    }

                } catch (Exception e) {
                    dialogbox("Please select a schedule.");
                }
            }
        });
        return rootView;
    }

    //displays an alert message w/ back button
    public void dialogbox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setMessage(message).setCancelable(true);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    @Override
    public void onResume() {
        mymav.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        for (int i = 0; i < listView.getCount(); i++) {
            listView.setItemChecked(i, false);
        }
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
