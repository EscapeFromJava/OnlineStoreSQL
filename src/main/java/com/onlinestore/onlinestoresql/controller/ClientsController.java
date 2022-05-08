package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;

public class ClientsController {

    @FXML
    TableView<Client> tblViewClients;
    ObservableList<Client> obsListClients;
    Connection conn;
    public void initialize(){
        conn = MainApplication.conn;
        initTableClients();
    }
    public void initTableClients() {
        obsListClients = Select.runSQLSelectClients(conn);

        TableColumn<Client, Integer> colId = new TableColumn<>("ID");
        TableColumn<Client, String> colFirstName = new TableColumn<>("First name");
        TableColumn<Client, String> colLastName = new TableColumn<>("Last name");
        TableColumn<Client, String> colPhoneNumber = new TableColumn<>("Phone number");
        TableColumn<Client, String> colCity = new TableColumn<>("City");
        TableColumn<Client, String> colDistrict = new TableColumn<>("District");
        TableColumn<Client, String> colStreet = new TableColumn<>("Street");
        TableColumn<Client, Integer> colHouse = new TableColumn<>("House");
        TableColumn<Client, Integer> colApartment = new TableColumn<>("Apartment");

        tblViewClients.getColumns().clear();
        tblViewClients.getColumns().addAll(colId, colFirstName, colLastName, colPhoneNumber, colCity, colDistrict, colStreet, colHouse, colApartment);
        tblViewClients.setItems(obsListClients);

        for (TableColumn currentColumn : tblViewClients.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        colId.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
        colFirstName.setCellValueFactory(el -> el.getValue().first_nameProperty());
        colLastName.setCellValueFactory(el -> el.getValue().last_nameProperty());
        colPhoneNumber.setCellValueFactory(el -> el.getValue().phone_numberProperty());
        colCity.setCellValueFactory(el -> el.getValue().cityProperty());
        colDistrict.setCellValueFactory(el -> el.getValue().districtProperty());
        colStreet.setCellValueFactory(el -> el.getValue().streetProperty());
        colHouse.setCellValueFactory(new PropertyValueFactory<Client, Integer>("house"));
        colApartment.setCellValueFactory(new PropertyValueFactory<Client, Integer>("apartment"));
    }
}
