package com.example.grafikacafe.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.grafikacafe.connection.SqiliteConnection;
import com.example.grafikacafe.admin.ModelUser;
import java.util.prefs.Preferences;

public class HandleUser {
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    ResultSet resultSet = null;

    public void setUpdateuser (ModelUser user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id_user", user.id_user);
        preferences.put("username", user.username);
        preferences.put("password", user.password);
        preferences.put("role", user.role);
    }

    public static ModelUser getUpdateUser() {
        Preferences preferences = Preferences.userRoot();
        var id_user = preferences.get("id_user", "String");
        var username = preferences.get("username", "String");
        var password = preferences.get("password", "String");
        var role = preferences.get("role", "String");
        return new ModelUser(id_user, username, password, role);
    }

    public boolean checkValidation(String username, String id_user) {
        connection = SqiliteConnection.Connector();
        String validate = "select * from tb_user where username = ? and id_user = ?";
        String search = "select * from tb_user where username = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, id_user);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                return true;
            } else {
                resultSet.close();
                preparedStatement.close();
                var searching = connection.prepareStatement(search);
                ResultSet searched = null;
                searching.setString(1, username);
                searched = searching.executeQuery();
                if (searched.next()) {
                    searching.close();
                    searched.close();
                    return false;
                } else {
                    searching.close();
                    searched.close();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}