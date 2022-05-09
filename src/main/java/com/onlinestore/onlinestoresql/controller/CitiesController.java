package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.City;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.Connection;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteCity;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddCity;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectCity;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Update.runSQLUpdateCity;

public class CitiesController {
    @FXML
    TextField textFieldNewCity;
    @FXML
    TableView<City> tblViewCities;
    Connection conn;
    ObservableList<City> obsListCity;

    public void initialize(){
        conn = MainApplication.conn;
        initTableCities();
    }

    private void initTableCities() {
        obsListCity = runSQLSelectCity(conn);

        TableColumn<City, String> colName = new TableColumn<>("City");

        tblViewCities.getColumns().clear();
        tblViewCities.getColumns().add(colName);
        tblViewCities.setItems(obsListCity);

        colName.setCellValueFactory(el -> el.getValue().nameProperty());

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            City selectCity = tblViewCities.getSelectionModel().getSelectedItem();
            runSQLUpdateCity(conn, event.getNewValue(), selectCity.getId());
            initTableCities();
        });

        tblViewCities.getSortOrder().setAll(colName);
    }

    public void onButtonAddCityClick() {
        if (!textFieldNewCity.getText().equals("")) {
            String newCity = textFieldNewCity.getText();
            runSQLInsertAddCity(conn, newCity);
            initTableCities();
            textFieldNewCity.clear();
        }
    }

    public void onMenuItemDeleteCityClick() {
        String selectCity = tblViewCities.getSelectionModel().getSelectedItem().getName();
        if (selectCity != null) {
            runSQLDeleteCity(conn, selectCity);
            initTableCities();
        }
    }
}
