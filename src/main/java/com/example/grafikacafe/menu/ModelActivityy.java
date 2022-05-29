package com.example.grafikacafe.menu;

public class ModelActivityy {

    public String employee;
    public String activity;
    public String date;

    public ModelActivityy(String employee, String activity, String date) {
        this.employee = employee;
        this.activity = activity;
        this.date = date;
    }

    public String getEmployee() {return employee;}
    public  void setEmployee(String employee) {this.employee = employee;}

    public String getActivity() {return activity;}
    public  void setActivity(String activity) {this.activity = activity;}

    public String getDate() {return date;}
    public  void setDate(String date) {this.date = date;}

}
