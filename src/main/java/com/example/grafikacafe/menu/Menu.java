package com.example.grafikacafe.menu;

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

public class Menu implements Initializable {

    @FXML
    private TextField search;

    @FXML
    private TableView<ModelMenu> menuTableView;

    @FXML
    private TableColumn<ModelMenu, String> id_menu;

    @FXML
    private TableColumn<ModelMenu, String> menu;

    @FXML
    private TableColumn<ModelMenu, String> kategori;

    @FXML
    private TableColumn<ModelMenu, String> harga;

    @FXML
    private TableColumn<ModelMenu, String> deskripsi;

    @FXML
    private TableColumn<ModelMenu, String> status;

    @FXML
    private TableColumn<ModelMenu, String> action;

    ModelMenu modelMenu = null;

    ObservableList<ModelMenu> dataList = FXCollections.observableArrayList();

    PreparedStatement preparedStatement = null;

    ResultSet resultSet = null;

    public void Table() {
        var connection = SqiliteConnection.Connector();
        refreshTable();

        id_menu.setCellValueFactory(new PropertyValueFactory<>("id_menu"));
        menu.setCellValueFactory(new PropertyValueFactory<>("menu"));
        kategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        harga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        deskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        FilteredList<ModelMenu> filteredList = new FilteredList<>(dataList, e -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(((ProductMenu -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (ProductMenu.getMenu().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            })));
        });

        SortedList<ModelMenu> sortedList =new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(menuTableView.comparatorProperty());


        Callback<TableColumn<ModelMenu, String>, TableCell<ModelMenu, String>> cellFoctory = (TableColumn<ModelMenu, String> param) -> {
            final TableCell<ModelMenu, String> cell = new TableCell<ModelMenu, String>() {
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
                                modelMenu = menuTableView.getSelectionModel().getSelectedItem();
                                var query = "delete from tb_menu where id_menu = ?";
                                var connection = SqiliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, modelMenu.getId_menu());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refreshTable();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        updateIcon.setOnMouseClicked((MouseEvent event) -> {
                            modelMenu = menuTableView.getSelectionModel().getSelectedItem();
                            try {
                                Main main = new Main();
                                HandleMenu handle = new HandleMenu();
                                ModelMenu data = new ModelMenu(modelMenu.getId_menu(), modelMenu.getMenu(), modelMenu.getKategori(), modelMenu.getHarga(), modelMenu.getDeskripsi(), modelMenu.getStatus());
                                handle.setUpdateMenu(data);
                                main.changeScene("menu/UpdateMenu.fxml");
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
        menuTableView.setItems(dataList);
        menuTableView.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();
            var query ="select * from tb_menu";
            var connection = SqiliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                dataList.add(new ModelMenu(
                        resultSet.getString("id_menu"),
                        resultSet.getString("menu"),
                        resultSet.getString("kategori"),
                        resultSet.getString("harga"),
                        resultSet.getString("deskripsi"),
                        resultSet.getString("status")
                ));
                menuTableView.setItems(dataList);
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
    public void addmenu (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("menu/AddMenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.Table();
    }
}
