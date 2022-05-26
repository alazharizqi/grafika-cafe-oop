package com.example.grafikacafe.admin;

import com.example.grafikacafe.Main;
import com.example.grafikacafe.connection.SqiliteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminUser implements Initializable {

    ModelUser modelUser = null;

    @FXML
    private TextField search;

    @FXML
    private TableView<ModelUser> userTableView;

    @FXML
    private TableColumn<ModelUser, String> id_user;

    @FXML
    private TableColumn<ModelUser, String> username;

    @FXML
    private TableColumn<ModelUser, String> role;

    @FXML
    private TableColumn<ModelUser, String> password;

    @FXML
    private TableColumn<ModelUser, String> action;

    ObservableList<ModelUser> dataList = FXCollections.observableArrayList();

    PreparedStatement preparedStatement = null;

    ResultSet resultSet = null;

    public void Table() {
        var connection = SqiliteConnection.Connector();
        refreshTable();

        id_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        FilteredList<ModelUser>filteredList = new FilteredList<>(dataList, e -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(((ProductUser -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (ProductUser.getUsername().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            })));
        });

        SortedList<ModelUser> sortedList =new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(userTableView.comparatorProperty());


        Callback<TableColumn<ModelUser, String>, TableCell<ModelUser, String>> cellFoctory = (TableColumn<ModelUser, String> param) -> {
            final TableCell<ModelUser, String> cell = new TableCell<ModelUser, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        HBox updateIcon = new HBox((new Label("Update")));
                        HBox deleteIcon = new HBox(new Label("Delete"));

                        deleteIcon.setStyle(
                                "-fx-cursor: hand;"

                        );
                        updateIcon.setStyle(
                                "-fx-cursor: hand;"
                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                modelUser = userTableView.getSelectionModel().getSelectedItem();
                                var query = "delete from tb_user where id_user = ?";
                                var connection = SqiliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, modelUser.getId_user());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refreshTable();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            modelUser = userTableView.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                HandleUser handle = new HandleUser();
                                ModelUser data = new ModelUser(modelUser.getId_user(), modelUser.getUsername(), modelUser.getPassword(), modelUser.getRole());
                                handle.setUpdateuser(data);
                                main.changeScene("admin/UpdateUser.fxml");
                            } catch (IOException ex) {
                                System.out.println(ex.getCause());
                            }
                        });

                        HBox hbox = new HBox(updateIcon, deleteIcon);
                        hbox.setStyle("-fx-alignment: center");
                        HBox.setMargin(deleteIcon,new Insets(2, 3, 0, 5));
                        HBox.setMargin(updateIcon,new Insets(2, 3, 0, 5));
                        setGraphic(hbox);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        action.setCellFactory(cellFoctory);
        userTableView.setItems(dataList);
        userTableView.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();
            var query ="select * from tb_user";
            var connection = SqiliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                dataList.add(new ModelUser(
                        resultSet.getString("id_user"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ));
                userTableView.setItems(dataList);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    public void logout (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("login/Login.fxml");
    }

    @FXML
    public void AddUser (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("admin/AddUser.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        this.Table();
    }
}
