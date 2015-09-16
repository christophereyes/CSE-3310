package com.example.jose.mymavmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyMavDataSource {
    private SQLiteDatabase mymav;
    private MySQLiteHelper dbHelper;


    public MyMavDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        mymav = dbHelper.getWritableDatabase();
    }

    public void drop(){
        dbHelper.onDrop(mymav);
    }
    public void close() {
        dbHelper.close();
    }

    public boolean createUser(String mavuser, String name, String password, String repassword, String email) {
        if (password.equals(repassword)) {
            if(getName() != null){
                return false;
            }
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.ID, mavuser);
            values.put(MySQLiteHelper.Name, name);
            values.put(MySQLiteHelper.Email, email);
            values.put(MySQLiteHelper.Password, password);
            values.put(MySQLiteHelper.LoginStatus, 1);

            mymav.insert(MySQLiteHelper.Table_Users, null, values);
            return true;
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        Cursor results = mymav.query(MySQLiteHelper.Table_Users, new String[]{MySQLiteHelper.ID, MySQLiteHelper.Password}, MySQLiteHelper.ID + " = '" + username + "' and " + MySQLiteHelper.Password + " = '" + password + "';", null, null, null, null, null);
        results.moveToFirst();
        String psswd = results.getString(results.getColumnIndexOrThrow(MySQLiteHelper.Password));
        if (psswd.equals(password))
            mymav.execSQL("update Users set LoginStatus = 1 where ID = '" + username + "';");
        results.close();
        return true;
    }

    public String getName() {
        Cursor results = mymav.query(MySQLiteHelper.Table_Users, new String[]{MySQLiteHelper.Name}, MySQLiteHelper.LoginStatus + " = 1", null, null, null, null);
        if(results.moveToFirst()) {
            String usersName = results.getString(results.getColumnIndexOrThrow(MySQLiteHelper.Name));
            results.close();
            return usersName;
        }
        results.close();
        return null;
    }
    //1 is not user, 0 is user
    public int checkIfUser(String user) {
        Cursor results = mymav.rawQuery("select ID from Users where ID = '" + user + "';", null);
        //Cursor results = mymav.query(MySQLiteHelper.Table_Users, new String[]{MySQLiteHelper.ID}, MySQLiteHelper.ID + " = '" + user + "';", null, null, null, null);
        if (results.getCount() == 0){
            results.close();
            return 1;
        }
        results.close();
        return 0;
    }

    public String getEmail() {
        Cursor results = mymav.query(MySQLiteHelper.Table_Users, new String[]{MySQLiteHelper.Email}, MySQLiteHelper.LoginStatus + " = 1", null, null, null, null);
        if(results.moveToFirst()) {
            String usersEmail = results.getString(results.getColumnIndexOrThrow(MySQLiteHelper.Email));
            results.close();
            return usersEmail;
        }
        results.close();
        return null;
    }

    public String getActiveUser() {
        Cursor results = mymav.rawQuery("select ID from Users where LoginStatus = 1;", null);
        if(results.moveToFirst()){
            return results.getString(results.getColumnIndex(MySQLiteHelper.ID));
        }
        results.close();
        return null;
    }

    public String getActiveSchedule() {
        Cursor results = mymav.rawQuery("select ScheduleName from Schedulenames where ActiveStatus = 1;", null);
        if(results.moveToFirst()){
            return results.getString(results.getColumnIndex(MySQLiteHelper.ScheduleName));
        }
        results.close();
        return null;
    }

    public List<String> getScheduleNames() {
        String user = getActiveUser();
        Cursor results = mymav.rawQuery("select distinct * from Schedulenames where UserID = '" + user + "';", null);
        List<String > scheduleNames = new ArrayList<>();
        Log.d("here", Integer.toString(results.getCount()));
        if(results.moveToFirst()){
            while(!results.isAfterLast()){
                String schedule = results.getString(results.getColumnIndex(MySQLiteHelper.ScheduleName));
                Log.d("herererer", schedule);
                scheduleNames.add(schedule);
                results.moveToNext();
            }
            results.close();
            return scheduleNames;
        }
        results.close();
        return scheduleNames;
    }

    public ArrayList<ClassObject> getScheduleToVerify(String activeSchedule) {
        ArrayList<ClassObject> classes = new ArrayList<>();
        Cursor results = mymav.rawQuery("select * from Classes join Schedules on Classes.ClassID = Schedules.ClassID where ScheduleName = '" + activeSchedule + "';", null);
        return iterator(classes, results);
    }

    public ArrayList<ClassObject> getScheduleNamesToDelete() {
        ArrayList<ClassObject> classes = new ArrayList<>();
        String activeSchedule = getActiveSchedule();
        Cursor results = mymav.rawQuery("select * from Classes join Schedules on Classes.ClassID = Schedules.ClassID where ScheduleName = '" + activeSchedule + "';", null);
        return iterator(classes, results);
    }

    public void addToSchedule(int classID) {
        String activeUser = getActiveUser();
        String activeSchedule = getActiveSchedule();
        mymav.execSQL("insert into Schedules values ('" + activeSchedule + "', '" + activeUser + "', " + classID + ");");
    }

    public void deleteFromSchedule(int classID) {
        String activeUser = getActiveUser();
        String activeSchedule = getActiveSchedule();
        mymav.execSQL("delete from Schedules where ScheduleName = '" + activeSchedule + "' and  UserID = '" + activeUser + "' and  ClassID = " + classID + ";");
    }

    public void createSchedule(String newSchedule) {
        String activeUser = getActiveUser();
        mymav.execSQL("update ScheduleNames set ActiveStatus = 0 where ActiveStatus = 1;");
        mymav.execSQL("insert into ScheduleNames values ('" + newSchedule + "', 1, '" + activeUser + "');");
    }

    public void deleteSchedule (String schedule) {
        mymav.execSQL("delete from ScheduleNames where ScheduleName = '" + schedule + "';");
        mymav.execSQL("delete from Schedules where ScheduleName = '" + schedule + "';");
    }

    public void setActiveSchedule(String activeschedule) {
        mymav.execSQL("update ScheduleNames set ActiveStatus = 0 where ActiveStatus = 1;");
        mymav.execSQL("update ScheduleNames set ActiveStatus = 1 where ScheduleName = '" + activeschedule + "';");
    }

    public ArrayList<ClassObject> search(int courseIDNumber, String courseDepartment, int courseNumber) {
        ArrayList<ClassObject> classes = new ArrayList<>();
        Cursor results = null;

        //return all classes
        if (courseIDNumber == 0 && courseDepartment.isEmpty() && courseNumber == 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd}, null, null, null, null, null);
            return iterator(classes, results);

        //search on department and course number
        } else if (courseIDNumber == 0 && !courseDepartment.isEmpty() && courseNumber != 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.CourseDept + " = '" + courseDepartment + "' and " + MySQLiteHelper.CourseNumber + " = '" + courseNumber + "';", null, null, null, null);
            return iterator(classes, results);

        //search on course ID and course number
        } else if (courseIDNumber != 0 && courseDepartment.isEmpty() && courseNumber != 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.ClassID + " = " + courseIDNumber + " and " + MySQLiteHelper.CourseNumber + " " + courseNumber + ";", null, null, null, null);
            return iterator(classes, results);

        //search on course ID and course department
        } else if (courseIDNumber != 0 && !courseDepartment.isEmpty() && courseNumber == 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.ClassID + " = " + courseIDNumber + " and "+ MySQLiteHelper.CourseDept + " = '" + courseDepartment + "';", null, null, null, null);
            return iterator(classes, results);

        //search on course number
        } else if (courseIDNumber == 0 && courseDepartment.isEmpty() && courseNumber != 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.CourseNumber + " = " + courseNumber + ";", null, null, null, null);
            return iterator(classes, results);

        //search on course ID
        } else if (courseIDNumber != 0 && courseDepartment.isEmpty() && courseNumber == 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.ClassID + " = " + courseIDNumber + ";", null, null, null, null);
            return iterator(classes, results);

        //search on course department
        } else if (courseIDNumber == 0 && !courseDepartment.isEmpty() && courseNumber == 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.CourseDept + " = '" + courseDepartment + "';", null, null, null, null);

            return iterator(classes, results);
        //search on course ID, course department, and course number
        } else if (courseIDNumber != 0 && !courseDepartment.isEmpty() && courseNumber != 0) {
            results = mymav.query(MySQLiteHelper.Table_Classes, new String[]{MySQLiteHelper.ClassID, MySQLiteHelper.CourseDept, MySQLiteHelper.CourseNumber, MySQLiteHelper.ClassDays, MySQLiteHelper.TimeStart, MySQLiteHelper.TimeEnd, MySQLiteHelper.Capacity, MySQLiteHelper.Enrolled, MySQLiteHelper.Room, MySQLiteHelper.DateStart, MySQLiteHelper.DateEnd},
                    MySQLiteHelper.ClassID + " = " + courseIDNumber + " and " + MySQLiteHelper.CourseDept + " = '" + courseDepartment + "' and " + MySQLiteHelper.CourseNumber + " = " + courseNumber + ";", null, null, null, null);

            return iterator(classes, results);
        }
        return classes;
    }

    public ArrayList<ClassObject> iterator (ArrayList<ClassObject> classes, Cursor results) {
        if (results == null)
            return classes;
        if (results.moveToFirst()) {
            while (!results.isAfterLast()) {
                ClassObject className = new ClassObject();

                int classid = Integer.parseInt(results.getString(results.getColumnIndex(MySQLiteHelper.ClassID)));
                String courseDept = results.getString(results.getColumnIndex(MySQLiteHelper.CourseDept));
                int courseNum = Integer.parseInt(results.getString(results.getColumnIndex(MySQLiteHelper.CourseNumber)));
                String classDays = results.getString(results.getColumnIndex(MySQLiteHelper.ClassDays));
                String startTime = results.getString(results.getColumnIndex(MySQLiteHelper.TimeStart));
                String endTime = results.getString(results.getColumnIndex(MySQLiteHelper.TimeEnd));
                int capacity = Integer.parseInt(results.getString(results.getColumnIndex(MySQLiteHelper.Capacity)));
                int enrolled = Integer.parseInt(results.getString(results.getColumnIndex(MySQLiteHelper.Enrolled)));
                String room = results.getString(results.getColumnIndex(MySQLiteHelper.Room));
                String startdate = results.getString(results.getColumnIndex(MySQLiteHelper.DateStart));
                String enddate = results.getString(results.getColumnIndex(MySQLiteHelper.DateEnd));

                className.setClassID(classid);
                className.setCourseDept(courseDept);
                className.setCourseNumber(courseNum);
                className.setClassDays(classDays);
                className.setTimeStart(startTime);
                className.setTimeEnd(endTime);
                className.setCapacity(capacity);
                className.setEnrolled(enrolled);
                className.setRoom(room);
                className.setDateStart(startdate);
                className.setDateEnd(enddate);
                classes.add(className);
                results.moveToNext();
            }
        }
        results.close();
        return classes;
    }

    public void editUserInfo(String name, String email, String password) {
        if(!name.isEmpty()){
            mymav.execSQL("update Users set Name = '" + name +"' where LoginStatus = 1;");
        }
        if(!email.isEmpty()) {
            mymav.execSQL("update Users set Email = '" + email +"' where LoginStatus = 1;");
        }
        if(!password.isEmpty()) {
            mymav.execSQL("update Users set Password = '" + password +"' where LoginStatus = 1;");
        }
    }

    public void logoutUser() {
        mymav.execSQL("update Users set LoginStatus = 0;");
        mymav.execSQL("update ScheduleNames set ActiveStatus = 0;");
    }

    public String getPassword() {
        Cursor results = mymav.query(MySQLiteHelper.Table_Users, new String[]{MySQLiteHelper.Password}, MySQLiteHelper.LoginStatus + " = 1", null, null, null, null);
        if (results.moveToFirst()) {
            String usersPassword = results.getString(results.getColumnIndexOrThrow(MySQLiteHelper.Password));
            return usersPassword;
        }
        results.close();
        return null;
    }


    public void deleteUser() {
        String userToDelete = getActiveUser();
        mymav.execSQL("delete from ScheduleNames where UserID = '" + userToDelete + "';");
        mymav.execSQL("delete from Schedules where UserID = '" + userToDelete + "';");
        String UserDelete = "delete from Users where LoginStatus = 1;";
        mymav.execSQL(UserDelete);
    }
}
