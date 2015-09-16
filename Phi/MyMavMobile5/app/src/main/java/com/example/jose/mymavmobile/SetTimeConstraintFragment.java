package com.example.jose.mymavmobile;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;


public class SetTimeConstraintFragment extends Fragment {

    private MyMavDataSource mymavdata;
    private View rootView;
    private TextView editStartTime, editEndTime;
    private Time startTime, endTime;
    private Button buttonSetTime, buttonClear;
    private int startHour, startMinute, endHour, endMinute;
    private TimePickerDialog startPicker, endPicker;
    private RadioGroup radioGroup;
    private RadioButton classesOnlyBetween, classesNotBetween;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstantState) {

        rootView = inflater.inflate(R.layout.fragment_set_time_constraint, container, false);

        //Call for backthread to keep database open
        mymavdata = new MyMavDataSource(rootView.getContext());
        mymavdata.open();

        editStartTime = (TextView) rootView.findViewById(R.id.setStartTime);
        editEndTime = (TextView) rootView.findViewById(R.id.setEndTime);
        buttonSetTime = (Button) rootView.findViewById(R.id.buttonSetTime);
        buttonClear = (Button) rootView.findViewById(R.id.buttonClearTime);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.setTimeRadioGroup);
        classesOnlyBetween = (RadioButton) rootView.findViewById(R.id.radioButtonOnlyClassesBetween);
        classesNotBetween = (RadioButton) rootView.findViewById(R.id.radioButtonNoClassesBetween);

        startPicker = new TimePickerDialog(rootView.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startHour= hourOfDay;
                        startMinute= minute;

                        if(hourOfDay == 0)
                            editStartTime.setText(String.format("%2d:%02d AM", hourOfDay+12, minute));
                        else if(hourOfDay == 12)
                            editStartTime.setText(String.format("%2d:%02d PM", hourOfDay, minute));
                        else if(hourOfDay > 12)
                            editStartTime.setText(String.format("%2d:%02d PM", hourOfDay-12, minute));
                        else
                            editStartTime.setText(String.format("%2d:%02d AM", hourOfDay, minute));
                    }
                }, startHour, startMinute, false);

        endPicker = new TimePickerDialog(rootView.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endHour = hourOfDay;
                        endMinute = minute;

                        if(hourOfDay == 0)
                            editEndTime.setText(String.format("%2d:%02d AM", hourOfDay+12, minute));
                        else if(hourOfDay == 12)
                            editEndTime.setText(String.format("%2d:%02d PM", hourOfDay, minute));
                        else if(hourOfDay > 12)
                            editEndTime.setText(String.format("%2d:%02d PM", hourOfDay-12, minute));
                        else
                            editEndTime.setText(String.format("%2d:%02d AM", hourOfDay, minute));
                    }
                }, endHour, endMinute, false);


        //Create Time Picker for Start Time
        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPicker.show();
            }
        });

        //Create Time Picker for End Time
        editEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endPicker.show();
            }
        });

        //Set Time Button
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dim down button
//                buttonSetTime.setAlpha((float) 0.3);

                //TODO: set time button functionality
                if (classesNotBetween.isChecked() &&
                        !editStartTime.getText().toString().isEmpty() &&
                        !editEndTime.getText().toString().isEmpty()) {
                    Toast.makeText(rootView.getContext(), "Set Time Constraint", Toast.LENGTH_SHORT).show();
                    //TODO: set time constraint for NO CLASSES BETWEEN
                }
                else if (classesOnlyBetween.isChecked() &&
                        !editStartTime.getText().toString().isEmpty() &&
                        !editEndTime.getText().toString().isEmpty()) {
                    Toast.makeText(rootView.getContext(), "Set Time Constraint", Toast.LENGTH_SHORT).show();
                    //TODO: set time constraint for ONLY CLASSES BETWEEN
                }
                else{
                    dialogbox("Must select an option and a time frame.");
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioGroup.clearCheck();
                editStartTime.setText("");
                editEndTime.setText("");
            }
        });

        return rootView;
    }

    //displays an alert message w/ back button
    public void dialogbox(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setMessage(message).setCancelable(true);
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }
}