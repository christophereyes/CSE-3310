package com.example.jose.mymavmobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class TabVerifySchedule extends Fragment {

    private MyMavDataSource mymav;
    private View rootView;
    private ListView listView;
    private List<String> schedules;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<ClassObject> classesToVerify;
    private String schedule;
    private int invalidFlag, closedClass;
    private int timeStartClass, timeEndClass, timeStartCompareClass, timeEndCompareClass;
    private int timeHours, timeMinutes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tab_verify_schedule, container, false);

        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        schedules = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.listVerifySchedules);
        arrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        schedules = mymav.getScheduleNames();

        //populate listView
        arrayAdapter.clear();
        try {
            arrayAdapter.addAll(schedules);
        } catch (Exception e) {
        }

        //TODO: fix to where it verifies all class correctly
        //NOT FUNCTIONING PROPERLY
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schedule = schedules.get(position);
                classesToVerify = mymav.getScheduleToVerify(schedule);
                invalidFlag = 0;
                closedClass = 0;

                for (int n = 0; n < classesToVerify.size(); n++) {

                    //checks for full classes or class changes
                    try {

                        ArrayList<ClassObject> compareClass = mymav.search(classesToVerify.get(n).getClassID(),
                                classesToVerify.get(n).getCourseDept(), classesToVerify.get(n).getCourseNumber());

                        if (compareClass.get(0).getEnrolled() >= compareClass.get(0).getCapacity())
                            closedClass++;

                        if (!compareClass.get(0).getClassDays().toString().matches(classesToVerify.get(n).getClassDays().toString()))
                            invalidFlag++;

                        if (!compareClass.get(0).getTimeStart().toString().matches(classesToVerify.get(n).getTimeStart().toString()))
                            invalidFlag++;

                        if (!compareClass.get(0).getTimeEnd().toString().matches(classesToVerify.get(n).getTimeEnd().toString()))
                            invalidFlag++;

                    } catch (Exception e) {
                    }
                }


                for (int i = 0; i < classesToVerify.size() - 1; i++) {
                    //Times for current class in loop
                    timeStartClass = getTimeInMinutes(classesToVerify.get(i).getTimeStart());
                    timeEndClass = getTimeInMinutes(classesToVerify.get(i).getTimeEnd());

                    for (int n = i + 1; n < classesToVerify.size(); n++) {

                        timeStartCompareClass = getTimeInMinutes(classesToVerify.get(n).getTimeStart());
                        timeEndCompareClass = getTimeInMinutes(classesToVerify.get(n).getTimeEnd());

                        if (classesToVerify.get(i).getClassDays().length() <= classesToVerify.get(n).getClassDays().length()) {
                            if (classesToVerify.get(n).getClassDays().contains(classesToVerify.get(i).getClassDays())) {

                                if (timeStartClass == timeStartCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartClass < timeStartCompareClass && timeStartCompareClass < timeEndClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartClass < timeEndCompareClass && timeEndCompareClass < timeEndClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeEndClass == timeEndCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartCompareClass < timeStartClass && timeStartClass < timeEndCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartCompareClass < timeEndClass && timeEndClass < timeEndCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                }


                            }

                        } else if (classesToVerify.get(i).getClassDays().length() > classesToVerify.get(n).getClassDays().length()) {
                            if (classesToVerify.get(i).getClassDays().contains(classesToVerify.get(n).getClassDays())) {

                                if (timeStartClass == timeStartCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartClass < timeStartCompareClass && timeStartCompareClass < timeEndClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartClass < timeEndCompareClass && timeEndCompareClass < timeEndClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeEndClass == timeEndCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartCompareClass < timeStartClass && timeStartClass < timeEndCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                } else if (timeStartCompareClass < timeEndClass && timeEndClass < timeEndCompareClass) {
                                    invalidFlag = invalidFlag + 1;
                                }

                            }
                        }

                    }
                }


                if (invalidFlag == 0 && closedClass != 0) {
                    if (closedClass == 1) {
                        String invalidText = String.format("Invalid Schedule: %d Class is Full!", closedClass);
                        Toast.makeText(rootView.getContext(), invalidText, Toast.LENGTH_SHORT).show();
                    } else {
                        String invalidText = String.format("Invalid Schedule: %d Classes are Full!", closedClass);
                        Toast.makeText(rootView.getContext(), invalidText, Toast.LENGTH_SHORT).show();
                    }
                } else if (invalidFlag != 0 && closedClass == 0) {
                    String invalidText = String.format("Invalid Schedule: %d Error(s) Present!", invalidFlag);
                    Toast.makeText(rootView.getContext(), invalidText, Toast.LENGTH_SHORT).show();
                } else if (invalidFlag != 0 && closedClass != 0) {
                    if (closedClass == 1) {
                        String invalidText = String.format("Invalid Schedule: %d  Error(s) Present and %d Class is Full!", invalidFlag, closedClass);
                        Toast.makeText(rootView.getContext(), invalidText, Toast.LENGTH_SHORT).show();
                    } else {
                        String invalidText = String.format("Invalid Schedule: %d  Error(s) Present and %d Classes are Full!", invalidFlag, closedClass);
                        Toast.makeText(rootView.getContext(), invalidText, Toast.LENGTH_SHORT).show();
                    }
                } else

                {
                    Toast.makeText(rootView.getContext(), "Valid Schedule!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

    //returns Time from string formated(HH:MM AM) or (H:MM AM)
    public Time getTimeFromString(String stringTime) {
        //set time to military time
        // timeArray[0] = hours
        // timeArray[1] = minutes
        // timeArray[2] = AM / PM
        String[] timeArray = stringTime.split("[: ]");
        Time time;

        //get int hours - military
        if (timeArray[2].contains("PM") && timeArray[0].contains("12")) {
            timeHours = 12;
        } else if (timeArray[2].contains("PM")) {
            timeHours = Integer.parseInt(timeArray[0]) + 12;
        } else if (timeArray[2].contains("AM") && timeArray[0].contains("12")) {
            timeHours = 0;
        }
        //get int minutes
        timeMinutes = Integer.parseInt(timeArray[1]);

        time = new Time(timeHours, timeMinutes, 0);
        return time;
    }

    //returns Time from string formated(HH:MM AM) or (H:MM AM)
    public int getTimeInMinutes(String stringTime) {
        //set time to military time
        // timeArray[0] = hours
        // timeArray[1] = minutes
        // timeArray[2] = AM / PM
        String[] timeArray = stringTime.split("[: ]");
        int time = 0;

        //get int hours - military
        if (timeArray[2].contains("PM") && timeArray[0].contains("12")) {
            timeHours = 12;
        } else if (timeArray[2].contains("PM")) {
            timeHours = Integer.parseInt(timeArray[0]) + 12;
        } else if (timeArray[2].contains("AM") && timeArray[0].contains("12")) {
            timeHours = 0;
        }
        //get int minutes
        timeMinutes = Integer.parseInt(timeArray[1]);
        time = (timeHours * 60) + timeMinutes;

        return time;
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
