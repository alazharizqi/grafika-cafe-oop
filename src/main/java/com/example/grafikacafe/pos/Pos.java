package com.example.grafikacafe.pos;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class Pos implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private ComboBox menu = new ComboBox();

    ObservableList<String> kode_menuItems = FXCollections.observableArrayList();

    public void setKode_menuItems() {
        String query = "select * from tb_menu where status = ?";
        connection = SqiliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Available");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                kode_menuItems.add(resultSet.getString("menu"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        menu.setItems(kode_menuItems);
    }


    @FXML
    public void logout (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("login/Login.fxml");
    }

    @FXML
    public void sales (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("pos/Sales.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setKode_menuItems();
    }
}
