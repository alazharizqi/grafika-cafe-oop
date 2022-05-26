package com.example.grafikacafe.admin;

public class ModelUser {

    public String id_user;
    public String username;
    public String password;
    public String role;

    public ModelUser(String id_user, String username, String password, String role) {
        this.id_user = id_user;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getId_user() {return id_user;}
    public  void setId_user(String id_user) {this.id_user = id_user;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}

}
