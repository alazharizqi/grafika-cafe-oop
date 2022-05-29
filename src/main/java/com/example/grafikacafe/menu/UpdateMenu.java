package com.example.grafikacafe.menu;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.admin.HandleUser;
import com.example.grafikacafe.connection.SqiliteConnection;
import com.example.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UpdateMenu implements Initializable {

    PreparedStatement preparedStatement = null;
    Connection connection = null;

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

    public void setLog() {
        Connection connection = SqiliteConnection.Connector();
        var session = Session.getSession();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String format = localDateTime.format(dateTimeFormatter);
        String query = "insert into tb_log (employee, activity, date) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, session.name);
            preparedStatement.setString(2, "Update menu");
            preparedStatement.setString(3, format);
            preparedStatement.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateQuery() {
        connection = SqiliteConnection.Connector();
        String query = "update tb_menu set menu = ?, kategori = ?, harga = ? , deskripsi = ? , status = ? where id_menu = ?";
        var id_menu = HandleMenu.getUpdateMenu();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, menu.getText());
            preparedStatement.setString(2, kategori.getValue());
            preparedStatement.setString(3, harga.getText());
            preparedStatement.setString(4, deskripsi.getText());
            preparedStatement.setString(5, status.getValue());
            preparedStatement.setString(6, id_menu.id_menu);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void submitData(ActionEvent event) {
        Main main = new Main();
        var id_menu = HandleMenu.getUpdateMenu();
        HandleMenu validation = new HandleMenu();
        try {
            if (menu.getText().isEmpty() || kategori.getValue() == null || harga.getText().isEmpty() || deskripsi.getText().isEmpty() || status.getValue() == null) {
                Alert alert =  new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Form Tidak Boleh Kosong !");
                alert.showAndWait();
            } else {
                if (validation.checkValidation(menu.getText(), id_menu.id_menu)) {
                    updateQuery();
                    setLog();
                    main.changeScene("menu/Menu.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Menu Sudah Terdaftar !");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void logout() throws BackingStoreException {
        Connection connection = SqiliteConnection.Connector();
        var session = Session.getSession();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String format = localDateTime.format(dateTimeFormatter);
        String query = "insert into tb_log (employee, activity, date) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, session.name);
            preparedStatement.setString(2, "Logout");
            preparedStatement.setString(3, format);
            preparedStatement.execute();
            Main main = new Main();
            main.changeScene("login/Login.fxml");
            Preferences preferences = Preferences.userRoot();
            preferences.clear();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void menu (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("menu/Menu.fxml");
    }

    @FXML
    public void activity (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("menu/Activity.fxml");
    }

    @FXML
    private void addKategori() {
        kategori.setItems(kategoriList);
    }

    @FXML
    private void addStatus() {
        status.setItems(statusList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addKategori();
        this.addStatus();
        var data = HandleMenu.getUpdateMenu();
        menu.setText(data.menu);
        kategori.setValue(data.kategori);
        harga.setText(data.harga);
        deskripsi.setText(data.deskripsi);
        status.setValue(data.status);
    }
}
