package com.example.grafikacafe.menu;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddMenu {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField menu = new TextField();

    @FXML
    public TextField harga = new TextField();

    @FXML
    public TextArea deskripsi = new TextArea();

    @FXML
    public ComboBox<String> kategori = new ComboBox<>();

    @FXML
    public ComboBox<String> status = new ComboBox<>();

    ObservableList<String> kategoriList = FXCollections.observableArrayList("Food", "Drink", "Snack");
    ObservableList<String> statusList = FXCollections.observableArrayList("Available", "Unavailable");

    public void addItems() {
        kategori.setItems(kategoriList);
        status.setItems(statusList);
    }

    public void submitData(ActionEvent event) throws IOException {
        Main main = new Main();
        connection = SqiliteConnection.Connector();

        String query = "insert into tb_menu (menu, kategori, harga, deskripsi, status) values (?, ?, ?, ?, ?)";
        String validate = "select * from tb_menu where menu = ?";
        try {
            if (menu.getText().isEmpty() || kategori.getValue() == null || harga.getText().isEmpty() || deskripsi.getText().isEmpty() || status.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Form Tidak Boleh Kosong !");
                alert.showAndWait();
            } else {
                var validation = connection.prepareStatement(validate);
                validation.setString(1, menu.getText());
                resultSet = validation.executeQuery();
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Menu yang anda masukkan telah terdaftar !");
                    alert.showAndWait();
                } else {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, menu.getText());
                    preparedStatement.setString(2, kategori.getValue());
                    preparedStatement.setString(3, harga.getText());
                    preparedStatement.setString(4, deskripsi.getText());
                    preparedStatement.setString(5, status.getValue());
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();
                    main.changeScene("menu/Menu.fxml");
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
        kategori.setItems(kategoriList);
        status.setItems(statusList);
    }

    @FXML
    public void logout (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("login/Login.fxml");
    }

    @FXML
    public void menu (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("menu/Menu.fxml");
    }

}