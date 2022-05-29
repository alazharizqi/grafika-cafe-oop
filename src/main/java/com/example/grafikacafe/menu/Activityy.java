package com.example.grafikacafe.menu;
import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import com.example.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Activityy implements Initializable {

    @FXML
    public TableView<ModelActivityy> table;

    @FXML
    public TableColumn<ModelActivityy, String> employee;

    @FXML
    public TableColumn<ModelActivityy, String> activity;

    @FXML
    public TableColumn<ModelActivityy, String> date;

    ObservableList<ModelActivityy> activitiesList = FXCollections.observableArrayList();

    public void table() {

        getActivitiesItem();

        activity.setCellValueFactory(new PropertyValueFactory<>("activity"));
        employee.setCellValueFactory(new PropertyValueFactory<>("employee"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.setItems(activitiesList);
    }

    public void getActivitiesItem() {
        Connection connection = SqiliteConnection.Connector();
        String query = "select * from tb_log";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                activitiesList.add(new ModelActivityy(
                        resultSet.getString("employee"),
                        resultSet.getString("activity"),
                        resultSet.getString("date")
                ));
                table.setItems(activitiesList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void menu (ActionEvent event) throws IOException {
        Main main = new Main();
        main.changeScene("menu/Menu.fxml");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.table();
    }
}