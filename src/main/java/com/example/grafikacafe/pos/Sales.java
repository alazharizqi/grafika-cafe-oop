package com.example.grafikacafe.pos;

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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Sales implements Initializable {

    ModelPos modelPos = null;

    @FXML
    private TextField search;

    @FXML
    private TableView<ModelPos> posTableView;

    @FXML
    private TableColumn<ModelPos, String> id_pos;

    @FXML
    private TableColumn<ModelPos, String> kode_pos;

    @FXML
    private TableColumn<ModelPos, String> no_meja;

    @FXML
    private TableColumn<ModelPos, String> customer;

    @FXML
    private TableColumn<ModelPos, String> date;

    @FXML
    private TableColumn<ModelPos, String> total;

    ObservableList<ModelPos> dataList = FXCollections.observableArrayList();

    PreparedStatement preparedStatement = null;

    ResultSet resultSet = null;

    public void Table() {
        var connection = SqiliteConnection.Connector();
        refreshTable();

        id_pos.setCellValueFactory(new PropertyValueFactory<>("id_pos"));
        kode_pos.setCellValueFactory(new PropertyValueFactory<>("kode_pos"));
        no_meja.setCellValueFactory(new PropertyValueFactory<>("no_meja"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        FilteredList<ModelPos> filteredList = new FilteredList<>(dataList, e -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(((ProductPos -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String keyword = newValue.toLowerCase();

                if (ProductPos.getId_pos().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (ProductPos.getKode_pos().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (ProductPos.getNo_meja().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (ProductPos.getCustomer().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (ProductPos.getDate().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else if (ProductPos.getTotal().toLowerCase().indexOf(keyword) > -1) {
                    return true;
                } else {
                    return false;
                }
            })));
        });

        SortedList<ModelPos> sortedList =new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(posTableView.comparatorProperty());


        Callback<TableColumn<ModelPos, String>, TableCell<ModelPos, String>> cellFoctory = (TableColumn<ModelPos, String> param) -> {
            final TableCell<ModelPos, String> cell = new TableCell<ModelPos, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        HBox deleteIcon = new HBox(new Label("Delete"));

                        deleteIcon.setStyle(
                                "-fx-cursor: hand;"

                        );
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                modelPos = posTableView.getSelectionModel().getSelectedItem();
                                var query = "delete from tb_pos where id_pos = ?";
                                var connection = SqiliteConnection.Connector();
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, modelPos.getId_pos());
                                preparedStatement.execute();
                                preparedStatement.close();
                                resultSet.close();
                                refreshTable();
                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        HBox hbox = new HBox(deleteIcon);
                        hbox.setStyle("-fx-alignment: center");
                        HBox.setMargin(deleteIcon,new Insets(2, 3, 0, 5));
                        setGraphic(hbox);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        posTableView.setItems(dataList);
        posTableView.setItems(sortedList);
    }

    public void refreshTable() {
        try {
            dataList.clear();
            var query ="select * from tb_pos";
            var connection = SqiliteConnection.Connector();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                dataList.add(new ModelPos(
                        resultSet.getString("id_pos"),
                        resultSet.getString("kode_pos"),
                        resultSet.getString("no_meja"),
                        resultSet.getString("customer"),
                        resultSet.getString("date"),
                        resultSet.getString("total")
                ));
                posTableView.setItems(dataList);
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
    public void pos (ActionEvent event) throws IOException {
        Main change = new Main();
        change.changeScene("pos/Pos.fxml");
    }

    @FXML
    public void print (ActionEvent event) throws  IOException {
        JasperPrint jasperPrint = null;
        Map param = new HashMap();

        try {
            jasperPrint = JasperFillManager.fillReport("report/SalesReport.jasper", param, SqiliteConnection.Connector());
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Sales Report");
            viewer.setVisible(true);
        } catch (JRException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.Table();
    }
}
