package com.example.jose.mymavmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TabViewClass extends Fragment {

    private MyMavDataSource mymav;
    private ArrayList<ClassObject> classes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_view_classes, container, false);

        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        TextView textActiveSchedule = (TextView) rootView.findViewById(R.id.editSetCurrentActiveSchedule);
        String active = mymav.getActiveSchedule();
        if (active != null) {
            textActiveSchedule.setText(active);
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listViewCurrentClassSchedule);
        CustomListAdapter adapter = new CustomListAdapter(rootView.getContext(), classes);
        listView.setAdapter(adapter);

        classes = mymav.getScheduleNamesToDelete();
        if (classes.size() == 0) { Toast.makeText(rootView.getContext(), "No classes found", Toast.LENGTH_SHORT).show(); }
        adapter.clear();
        adapter.addAll(classes);
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
    public void onStop(){
        mymav.close();
        super.onStop();
    }

    @Override
    public void onDestroy(){
        mymav.close();
        super.onDestroy();
    }
}
