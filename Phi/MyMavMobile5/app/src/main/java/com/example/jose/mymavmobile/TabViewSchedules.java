package com.example.jose.mymavmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TabViewSchedules extends Fragment {

    private MyMavDataSource mymav;
    private View rootView;
    private TextView textActiveSchedule;
    private ListView listView;
    private List<String> schedules;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_view_schedule, container, false);

        // Call for backthread to keep database open
        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        textActiveSchedule = (TextView) rootView.findViewById(R.id.setActiveSchedule);
        String active = mymav.getActiveSchedule();
        if (active != null) {
            textActiveSchedule.setText(active);
        }


        schedules = new ArrayList<>();
        listView = (ListView) rootView.findViewById(android.R.id.list);
        arrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        schedules = mymav.getScheduleNames();

        //populate listView
        arrayAdapter.clear();
        try {
            arrayAdapter.addAll(schedules);
        } catch (Exception e) {
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String schedule = schedules.get(position);
                mymav.setActiveSchedule(schedule);
                textActiveSchedule.setText(mymav.getActiveSchedule());
                Toast.makeText(rootView.getContext(), String.format("Set %s as active schedule", schedule), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
