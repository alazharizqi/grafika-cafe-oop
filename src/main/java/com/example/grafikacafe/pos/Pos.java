package com.example.grafikacafe.pos;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import com.example.grafikacafe.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Pos implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public TextField customer = new TextField();

    @FXML
    public TextField kode_pos = new TextField();

    @FXML
    public TextField no_meja = new TextField();

    @FXML
    private ComboBox menu = new ComboBox();

    @FXML
    public TextField kategori = new TextField();

    @FXML
    public TextField harga = new TextField();

    @FXML
    public TextField deskripsi = new TextField();

    @FXML
    public TextField status = new TextField();

    @FXML
    public TextField qty = new TextField();

    @FXML
    public TextField total = new TextField();

    @FXML
    public DatePicker date = new DatePicker();

    ObservableList<String> menu_Items = FXCollections.observableArrayList();

    public void setTotal(ActionEvent event) {
        total.setText(String.valueOf( Double.parseDouble( qty.getText()) * Double.parseDouble(harga.getText())));
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
            preparedStatement.setString(2, "Add transaction");
            preparedStatement.setString(3, format);
            preparedStatement.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMenu(ActionEvent event) {
        String query = "select * from tb_menu where menu = ?";
        connection = SqiliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, menu.getValue().toString());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                kategori.setText(resultSet.getString("kategori"));
                harga.setText(resultSet.getString("harga"));
                deskripsi.setText(resultSet.getString("deskripsi"));
                status.setText(resultSet.getString("status"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMenuItems() {
        String query = "select * from tb_menu where status = ?";
        connection = SqiliteConnection.Connector();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "Available");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                menu_Items.add(resultSet.getString("menu"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        menu.setItems(menu_Items);
    }




    public void submitData (ActionEvent event) throws IOException {



        Main main = new Main();
        connection = SqiliteConnection.Connector();
        String query = "insert into tb_pos (kode_pos, no_meja, customer, menu, kategori, harga, deskripsi, status, qty, total, date) values (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (kode_pos.getText().isEmpty() || no_meja.getText().isEmpty() || customer.getText().isEmpty() || menu.getValue() == null || kategori.getText().isEmpty() || harga.getText().isEmpty() || deskripsi.getText().isEmpty() || status.getText().isEmpty() || qty.getText().isEmpty() || total.getText().isEmpty() || date.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Form Tidak Boleh Kosong !");
                alert.showAndWait();
            } else {

//                    main.changeScene("pos/Sales.fxml");

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, kode_pos.getText());
                preparedStatement.setString(2, no_meja.getText());
                preparedStatement.setString(3, customer.getText());
                preparedStatement.setString(4, menu.getValue().toString());
                preparedStatement.setString(5, kategori.getText());
                preparedStatement.setString(6, harga.getText());
                preparedStatement.setString(7, deskripsi.getText());
                preparedStatement.setString(8, status.getText());
                preparedStatement.setString(9, qty.getText());
                preparedStatement.setString(10, total.getText());
                preparedStatement.setString(11, String.valueOf(date.getValue()));

                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
                resultSet.close();
                setLog();



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
    public void sales (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("pos/Sales.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setMenuItems();
    }
}
