package com.example.grafikacafe.admin;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import com.example.grafikacafe.admin.HandleUser;
import com.example.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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

public class UpdateUser implements Initializable {

    PreparedStatement preparedStatement = null;
    Connection connection = null;

    @FXML
    private TextField username = new TextField();

    @FXML
    private ComboBox<String> role = new ComboBox<>();

    @FXML
    private TextField password = new TextField();

    ObservableList<String> roleList = FXCollections.observableArrayList("Admin", "Manager", "Kasir");

    public void updateQuery() {
        connection = SqiliteConnection.Connector();
        String query = "update tb_user set username = ?, password = ?, role = ? where id_user = ?";
        var id_user = HandleUser.getUpdateUser();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username.getText());
            preparedStatement.setString(2, password.getText());
            preparedStatement.setString(3, role.getValue());
            preparedStatement.setString(4, id_user.id_user);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
            preparedStatement.setString(2, "Update user");
            preparedStatement.setString(3, format);
            preparedStatement.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void submitData(ActionEvent event) {
        Main main = new Main();
        var id_user = HandleUser.getUpdateUser();
        HandleUser validation = new HandleUser();
        try {
            if (username.getText().isEmpty() || password.getText().isEmpty() || role.getValue() == null) {
                Alert alert =  new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Form Tidak Boleh Kosong !");
                alert.showAndWait();
            } else {
                if (validation.checkValidation(username.getText(), id_user.id_user)) {
                    updateQuery();
                    setLog();
                    main.changeScene("admin/AdminUser.fxml");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Username Sudah Terdaftar !");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void addRole() {
        role.setItems(roleList);
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
    public void user (ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("admin/AdminUser.fxml");
    }

    @FXML
    public void activity (ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("admin/Activity.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addRole();
        var data = HandleUser.getUpdateUser();
        username.setText(data.username);
        password.setText(data.password);
        role.setValue(data.role);
    }
}