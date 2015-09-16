package com.example.jose.mymavmobile;

public class ClassObject {
    private int ClassID;
    private String CourseDept;
    private int CourseNumber;
    private String ClassDays;
    private String TimeStart;
    private String TimeEnd;
    private int Capacity;
    private int Enrolled;
    private String Room;
    private String DateStart;
    private String DateEnd;

    public ClassObject() {
    }

    public ClassObject(int ClassID, String CourseDept, int CourseNumber, String ClassDays, String TimeStart, String TimeEnd, int Capacity, int Enrolled, String Room, String DateStart, String DateEnd) {
        this.ClassID = ClassID;
        this.CourseDept = CourseDept;
        this.CourseNumber = CourseNumber;
        this.ClassDays = ClassDays;
        this.TimeStart = TimeStart;
        this.TimeEnd = TimeEnd;
        this.Capacity = Capacity;
        this.Enrolled = Enrolled;
        this.Room = Room;
        this.DateStart = DateStart;
        this.DateEnd = DateEnd;
    }

    public int getClassID(){

        return ClassID;
    }
    public void setClassID(int ClassID){
        this.ClassID = ClassID;
    }
    public String getCourseDept(){
        return CourseDept;
    }
    public void setCourseDept(String CourseDept){
        this.CourseDept = CourseDept;
    }
    public int getCourseNumber(){
        return CourseNumber;
    }
    public void setCourseNumber(int CourseNumber){
        this.CourseNumber = CourseNumber;
    }
    public String getClassDays(){
        return ClassDays;
    }
    public void setClassDays(String ClassDays){
        this.ClassDays = ClassDays;
    }
    public String getTimeStart(){
        return TimeStart;
    }
    public void setTimeStart(String TimeStart){
        this.TimeStart = TimeStart;
    }
    public String getTimeEnd(){
        return TimeEnd;
    }
    public void setTimeEnd(String TimeEnd){
        this.TimeEnd = TimeEnd;
    }
    public int getCapacity(){
        return Capacity;
    }
    public void setCapacity(int Capacity){
        this.Capacity = Capacity;
    }
    public int getEnrolled(){
        return Enrolled;
    }
    public void setEnrolled(int Enrolled){
        this.Enrolled = Enrolled;
    }
    public String getRoom(){
        return Room;
    }
    public void setRoom(String Room){
        this.Room = Room;
    }
    public String getDateStart(){
        return DateStart;
    }
    public void setDateStart(String DateStart){
        this.DateStart = DateStart;
    }
    public String getDateEnd(){
        return DateEnd;
    }
    public void setDateEnd(String DateEnd){
        this.DateEnd = DateEnd;
    }
}
