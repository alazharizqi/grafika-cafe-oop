package com.example.grafikacafe.admin;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

import java.io.IOException;
import java.util.Locale;

public class AddUser {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;


    @FXML
    public TextField username = new TextField();

    @FXML
    public ComboBox<String> role = new ComboBox<>();

    @FXML
    public PasswordField password = new PasswordField();

    ObservableList<String> roleList = FXCollections.observableArrayList("Admin", "Manager", "Kasir");

    public void addItems() {
        role.setItems(roleList);
    }

    public void submitData(ActionEvent event) throws IOException {
        Main main = new Main();
        connection = SqiliteConnection.Connector();

        String query = "insert into tb_user (username, password, role) values (?, ?, ?)";
        String validate = "select * from tb_user where username = ?";
        try {
            if (username.getText().isEmpty() || role.getValue() == null || password.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Form Tidak Boleh Kosong !");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, username.getText());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Username yang anda masukkan telah terdaftar !");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username.getText());
                    preparedStatement.setString(2, password.getText());
                    preparedStatement.setString(3, role.getValue());
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                    main.changeScene("admin/AdminUser.fxml");
                }
                resultSet.close();
                validation.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        role.setItems(roleList);
    }

    @FXML
    public void User (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("admin/AdminUser.fxml");
    }

    @FXML
    public void logout (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("login/login.fxml");
    }
}