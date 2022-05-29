package com.example.grafikacafe.admin;

public class ModelActivity {

    public String employee;
    public String activity;
    public String date;

    public ModelActivity(String employee, String activity, String date) {
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
