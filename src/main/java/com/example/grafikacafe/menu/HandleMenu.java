package com.example.grafikacafe.menu;

import com.example.grafikacafe.menu.ModelMenu;
import com.example.grafikacafe.connection.SqiliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.prefs.Preferences;

public class HandleMenu {

    PreparedStatement preparedStatement = null;
    Connection connection = null;
    ResultSet resultSet = null;

    public void setUpdateMenu (ModelMenu user) {
        Preferences preferences = Preferences.userRoot();
        preferences.put("id_menu", user.id_menu);
        preferences.put("menu", user.menu);
        preferences.put("kategori", user.kategori);
        preferences.put("harga", user.harga);
        preferences.put("deskripsi", user.deskripsi);
        preferences.put("status", user.status);
    }

    public static ModelMenu getUpdateMenu() {
        Preferences preferences = Preferences.userRoot();
        var id_menu = preferences.get("id_menu", "String");
        var menu = preferences.get("menu", "String");
        var kategori = preferences.get("kategori", "String");
        var harga = preferences.get("harga", "String");
        var deskripsi = preferences.get("deskripsi", "String");
        var status = preferences.get("status", "String");
        return new ModelMenu(id_menu, menu, kategori, harga, deskripsi, status);
    }

    public boolean checkValidation(String menu, String id_menu) {
        connection = SqiliteConnection.Connector();
        String validate = "select * from tb_menu where menu = ? and id_menu = ?";
        String search = "select * from tb_menu where menu = ?";
        try {
            preparedStatement = connection.prepareStatement(validate);
            preparedStatement.setString(1, menu);
            preparedStatement.setString(2, id_menu);
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
                searching.setString(1, menu);
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
