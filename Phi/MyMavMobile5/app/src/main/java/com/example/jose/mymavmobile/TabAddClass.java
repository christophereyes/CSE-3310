package com.example.jose.mymavmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TabAddClass extends Fragment {

    private View rootView;
    private MyMavDataSource mymav;
    private ArrayList<ClassObject> classes = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;
    public int selectedItem = -1;
    TextView schedule;
    private Button search;
    private Button addClassButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_add_class, container, false);

        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        search = (Button) rootView.findViewById(R.id.buttonAddSearchClass);
        addClassButton = (Button) rootView.findViewById(R.id.buttonAddClass);
        schedule = (TextView) rootView.findViewById(R.id.activeClassTextvew);

        listView = (ListView) rootView.findViewById(R.id.listAddClass);
        adapter = new CustomListAdapter(rootView.getContext(), classes);
        listView.setAdapter(adapter);

        String activeSchedule = mymav.getActiveSchedule();
        if (activeSchedule == null) {
            schedule.setText("N/A");
        } else {
            schedule.setText(activeSchedule);
        }

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialogbox(mymav.getActiveSchedule() != null);
                if (!(mymav.getActiveSchedule() == null)) {
                    try {
                        int position = listView.getCheckedItemPosition();
                        ClassObject addClass = classes.get(position);
                        listView.setItemChecked(position, false);
                        try {

                            mymav.addToSchedule(addClass.getClassID());
                            Toast.makeText(rootView.getContext(), "Added class to schedule.", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            dialogbox("Class already in schedule!");
                        }
                    } catch (Exception e) {
                        dialogbox("Please select a class");
                    }
                } else {
                    dialogbox("Please select a schedule to be active.");
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchCourseIDNumber = (EditText) rootView.findViewById(R.id.editAddClassID);
                EditText searchCourseDepartment = (EditText) rootView.findViewById(R.id.editAddSubject);
                EditText searchCourseNumber = (EditText) rootView.findViewById(R.id.editAddCourseNumber);

                String ScourseIDNumber = searchCourseIDNumber.getText().toString();
                int courseIDNumber = 0;

                if (!ScourseIDNumber.isEmpty()) {
                    courseIDNumber = Integer.parseInt(ScourseIDNumber);
                }
                String courseDepartment = searchCourseDepartment.getText().toString();
                String ScourseNumber = searchCourseNumber.getText().toString();
                int courseNumber = 0;
                if (!ScourseNumber.isEmpty()) {
                    courseNumber = Integer.parseInt(ScourseNumber);
                }

                try {
                    classes = mymav.search(courseIDNumber, courseDepartment, courseNumber);
                } catch (Exception e) {
                }

                if (classes.size() == 0) {
                    Toast.makeText(rootView.getContext(), "No classes found", Toast.LENGTH_SHORT).show();
                }

                adapter.clear();
                adapter.addAll(classes);
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
