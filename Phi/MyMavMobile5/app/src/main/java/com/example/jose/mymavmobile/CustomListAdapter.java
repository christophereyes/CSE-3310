package com.example.jose.mymavmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<ClassObject> {
    private LayoutInflater inflater;
    private ArrayList<ClassObject> classes;

    public CustomListAdapter(Context context, ArrayList<ClassObject> classlist) {
        super(context, 0, classlist);
        classes = classlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassObject c = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout, parent, false);
        }

        TextView courseDepartment_textview = (TextView) convertView.findViewById(R.id.courseDepartment_textview);
        TextView courseNumber_textview = (TextView) convertView.findViewById(R.id.courseNumber_textview);
        TextView course_classdays_textview = (TextView) convertView.findViewById(R.id.course_classdays_textview);
        TextView course_classID_textview = (TextView) convertView.findViewById(R.id.course_id_textview);
        TextView course_time_start_textview = (TextView) convertView.findViewById(R.id.course_time_start_textview);
        TextView course_time_end_textvew = (TextView) convertView.findViewById(R.id.course_time_end_textview);
        TextView courese_enrolled_textview = (TextView) convertView.findViewById(R.id.course_enrolled_textview);
        TextView course_capacity_textview = (TextView) convertView.findViewById(R.id.course_capacity_textview);
        TextView course_room_textview = (TextView) convertView.findViewById(R.id.course_room_textview);
        TextView course_start_date_textview = (TextView) convertView.findViewById(R.id.course_start_date_textview);
        TextView course_end_date_textview = (TextView) convertView.findViewById(R.id.course_end_date_textview);

        ClassObject m = classes.get(position);

        courseDepartment_textview.setText(m.getCourseDept());
        courseNumber_textview.setText(Integer.toString(m.getCourseNumber()));
        course_classID_textview.setText(Integer.toString(m.getClassID()));
        course_classdays_textview.setText(m.getClassDays());
        course_time_start_textview.setText(m.getTimeStart());
        course_time_end_textvew.setText(m.getTimeEnd());
        courese_enrolled_textview.setText(Integer.toString(m.getEnrolled()));
        course_capacity_textview.setText(Integer.toString(m.getCapacity()));
        course_room_textview.setText(m.getRoom());
        course_start_date_textview.setText(m.getDateStart());
        course_end_date_textview.setText(m.getDateEnd());
        return convertView;
    }
}
