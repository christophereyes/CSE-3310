package com.example.jose.mymavmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TabDropClass extends Fragment {
    private View rootView;
    private MyMavDataSource mymav;
    private ArrayList<ClassObject> classes = new ArrayList<>();
    private Button deleteClass;
    private ListView listView;
    private CustomListAdapter adapter;
    private TextView schedule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_drop_classes, container, false);
        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        deleteClass = (Button) rootView.findViewById(R.id.buttonDropClass);
        schedule = (TextView) rootView.findViewById(R.id.active_schedule_textview);
        String active = mymav.getActiveSchedule();
        if (active == null) {
            schedule.setText("N/A");
        } else {
            schedule.setText(active);
        }

        listView = (ListView) rootView.findViewById(R.id.listDropClasses);
        adapter = new CustomListAdapter(rootView.getContext(), classes);
        listView.setAdapter(adapter);
        try {
            classes = mymav.getScheduleNamesToDelete();
            adapter.addAll(classes);
        } catch (Exception e) {
        }
        if (classes.size() == 0) {
            Toast.makeText(rootView.getContext(), "No classes found", Toast.LENGTH_SHORT).show();
        }
        adapter.clear();
        adapter.addAll(classes);

        //action for drop class button
        deleteClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int position;
                    ClassObject deleteClass;

                    position = listView.getCheckedItemPosition();
                    deleteClass = classes.get(position);
                    mymav.deleteFromSchedule(deleteClass.getClassID());
                    classes.remove(deleteClass);
                    listView.setItemChecked(position, false);
                    adapter.clear();
                    adapter.addAll(classes);
                    Toast.makeText(rootView.getContext(), "Class deleted from schedule", Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    dialogbox("Please select a class.");
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