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
import java.util.List;


public class SearchClassFragment extends Fragment{

    private MyMavDataSource mymav;
    private View rootView;
    private ArrayList<ClassObject> classes = new ArrayList<>();
    private CustomListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstantState){
        super.onCreateView(inflater, container, savedInstantState);
        rootView = inflater.inflate(R.layout.fragment_search_class, container, false);

        // Call for backthread to keep database open
        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        Button search = (Button)rootView.findViewById(R.id.searchClassButton);

        ListView listView = (ListView) rootView.findViewById(R.id.listSearch);
        adapter = new CustomListAdapter(rootView.getContext(), classes);
        listView.setAdapter(adapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchCourseIDNumber = (EditText)rootView.findViewById(R.id.searchClassID);
                EditText searchCourseDepartment = (EditText)rootView.findViewById(R.id.searchSubject);
                EditText searchCourseNumber = (EditText)rootView.findViewById(R.id.searchCourseNumber);

                String ScourseIDNumber = searchCourseIDNumber.getText().toString();
                int courseIDNumber = 0;

                if (!ScourseIDNumber.isEmpty()) { courseIDNumber = Integer.parseInt(ScourseIDNumber); }
                String courseDepartment = searchCourseDepartment.getText().toString();
                String ScourseNumber = searchCourseNumber.getText().toString();
                int courseNumber = 0;
                if (!ScourseNumber.isEmpty()) { courseNumber = Integer.parseInt(ScourseNumber); }

                try {classes = mymav.search(courseIDNumber, courseDepartment, courseNumber);
                } catch (Exception e) {}

                if (classes.size() == 0) { Toast.makeText(rootView.getContext(), "No classes found", Toast.LENGTH_SHORT).show(); }

                adapter.clear();
                adapter.addAll(classes);
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
