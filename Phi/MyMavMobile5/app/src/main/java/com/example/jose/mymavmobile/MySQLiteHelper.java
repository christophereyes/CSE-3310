package com.example.jose.mymavmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String Database_Name = "mymav.db";
    private static final int Database_Version = 1;
    Context cntx;
    private static final String TAG = "myApp";

    public static final String ID = "ID";
    public static final String Name = "Name";
    public static final String Email = "Email";
    public static final String Password = "Password";
    public static final String LoginStatus = "LoginStatus";
    public static final String ClassID = "ClassID";
    public static final String CourseDept = "CourseDept";
    public static final String CourseNumber = "CourseNumber";
    public static final String ClassDays = "ClassDays";
    public static final String TimeStart = "TimeStart";
    public static final String TimeEnd = "TimeEnd";
    public static final String Capacity = "Capacity";
    public static final String Enrolled = "Enrolled";
    public static final String Room = "Room";
    public static final String DateStart = "DateStart";
    public static final String DateEnd = "DateEnd";
    public static final String ScheduleName = "ScheduleName";
    public static final String ActiveStatus = "ActiveStatus";
    public static final String Table_Users = "Users";
    public static final String Table_Classes = "Classes";
    public static final String Table_Schedule_Names = "Schedulenames";
    public static final String Table_Schedules = "Schedules";


    private static final String Table_Users_Create = "create table Users (ID text not null, Name text null, Email text null, Password text null, LoginStatus integer null, primary key (ID))";
    private static final String Table_Classes_Create = "create table Classes (ClassID integer not null primary key, CourseDept text null, CourseNumber integer not null, ClassDays text null, TimeStart time null, TimeEnd time null, Capacity integer not null, Enrolled integer not null, Room text null, DateStart date null, DateEnd date null)";
    private static final String Table_Schedule_Names_Create = "create table Schedulenames (ScheduleName text not null, ActiveStatus integer null, UserID text not null, primary key (ScheduleName, UserID))";
    private static final String Table_Schedules_Create = "create table Schedules (ScheduleName text not null, UserID text not null, ClassID integer not null, primary key (ScheduleName, UserID, ClassID), foreign key (ScheduleName) references Schedulenames(ScheduleName) on delete cascade on update cascade, foreign key(UserID) references Users(ID) on delete cascade on update cascade, foreign key(ClassID) references Classes(ClassID) on delete cascade on update cascade);";

    public MySQLiteHelper(Context context) {
        super(context, Database_Name, null, Database_Version);
        this.cntx = context;
    }

    public void onDrop(SQLiteDatabase mymav) {
        mymav.execSQL("drop table if exists Users");
        mymav.execSQL("drop table if exists Classes");
        mymav.execSQL("drop table if exists Schedules");
        onCreate(mymav);
    }

    @Override
    public void onCreate(SQLiteDatabase mymav) {
        mymav.execSQL("PRAGMA foreign_keys = ON");

        mymav.execSQL(Table_Users_Create);
        mymav.execSQL(Table_Classes_Create);
        mymav.execSQL(Table_Schedule_Names_Create);
        mymav.execSQL(Table_Schedules_Create);

        String delims = ",";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(cntx.getAssets().open("hardcodedata")));
            String tokens = br.readLine();

            while (tokens != null){
                StringTokenizer st = new StringTokenizer(tokens,delims);

                int ID = Integer.parseInt(st.nextToken());
                String courseDept = st.nextToken();
                int courseNumber = Integer.parseInt(st.nextToken());
                String classDays = st.nextToken();
                String timeStart = st.nextToken();
                String timeEnd = st.nextToken();
                int cap = Integer.parseInt(st.nextToken());
                int enrolled= Integer.parseInt(st.nextToken());
                String room = st.nextToken();
                String startDate = st.nextToken();
                String endDate = st.nextToken();

                ContentValues values = new ContentValues();

                values.put(MySQLiteHelper.ClassID, ID);
                values.put(MySQLiteHelper.CourseDept, courseDept);
                values.put(MySQLiteHelper.CourseNumber, courseNumber);
                values.put(MySQLiteHelper.ClassDays ,classDays);
                values.put(MySQLiteHelper.TimeStart, timeStart);
                values.put(MySQLiteHelper.TimeEnd, timeEnd);
                values.put(MySQLiteHelper.Capacity, cap);
                values.put(MySQLiteHelper.Enrolled, enrolled);
                values.put(MySQLiteHelper.Room, room);
                values.put(MySQLiteHelper.DateStart, startDate);
                values.put(MySQLiteHelper.DateEnd, endDate);

                mymav.insert(Table_Classes, null, values);
                tokens = br.readLine();
            }
            br.close();
        }
        catch (Exception e) {
            Log.v(TAG, "failinSQLhelp");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS Table_Users_Create, Table_Classes_Create, Table_Schedules_Create");
        onCreate(db);
    }
}
