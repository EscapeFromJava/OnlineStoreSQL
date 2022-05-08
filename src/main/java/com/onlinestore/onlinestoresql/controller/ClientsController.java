package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.City;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Order;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import com.onlinestore.onlinestoresql.model.requestsSQL.Update;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.sql.Connection;
import java.util.regex.Pattern;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectCity;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Update.runSQLUpdateClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Update.runSQLUpdateClientFirstName;

public class ClientsController {
    @FXML
    AnchorPane paneAddClient;
    @FXML
    Button btnAddClient;
    @FXML
    ComboBox<String> comboBoxCity;
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TextField textFieldFirstName, textFieldLastName, textFieldPhoneNumber, textFieldDistrict, textFieldStreet, textFieldHouse, textFieldApartment;
    ObservableList<Client> obsListClients;
    ObservableList<City> obsListCity;
    Connection conn;

    public void initialize() {
        conn = MainApplication.conn;
        initComboBoxCity();
        initTableClients();
    }

    private void initComboBoxCity() {
        obsListCity = runSQLSelectCity(conn);
        ObservableList<String> obsListOnlyName = FXCollections.observableArrayList(obsListCity.stream().map(x -> x.getName()).toList());
        comboBoxCity.setItems(obsListOnlyName);
        comboBoxCity.getSelectionModel().select(0);
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

        colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        colFirstName.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientFirstName(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        //todo
        // 1) доделать редактор всех оставшихся полей
        // 2) убрать колонку ID
    }

    public void onButtonAddClientClick() {
        for (Node el : paneAddClient.getChildren()) {
            if (el instanceof TextField) {
                if (((TextField) el).getText().equals("")) {
                    el.setStyle("-fx-border-color: red;");
                } else
                    el.setStyle(null);
            }
        }

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        if (!textFieldFirstName.getText().equals("") &&
                !textFieldLastName.getText().equals("") &&
                !textFieldPhoneNumber.getText().equals("") &&
                !textFieldDistrict.getText().equals("") &&
                !textFieldStreet.getText().equals("") &&
                pattern.matcher(textFieldHouse.getText()).matches() &&
                pattern.matcher(textFieldApartment.getText()).matches()
        ) {
            Client newClient = new Client(textFieldFirstName.getText(),
                    textFieldLastName.getText(),
                    textFieldPhoneNumber.getText(),
                    String.valueOf(obsListCity.stream().filter(x -> x.getName().equals(comboBoxCity.getValue())).findFirst().get().getId()),
                    textFieldDistrict.getText(),
                    textFieldStreet.getText(),
                    Integer.parseInt(textFieldHouse.getText()),
                    Integer.parseInt(textFieldApartment.getText()));
            if (newClient != null) {
                runSQLInsertAddClient(conn, newClient);
                initTableClients();
                onButtonClearClick();
            }
        }
    }

    public void onButtonClearClick() {
        for (Node el : paneAddClient.getChildren()) {
            if (el instanceof TextField)
                ((TextField) el).clear();
            if (el instanceof ComboBox<?>) {
                ((ComboBox<?>) el).getSelectionModel().clearAndSelect(0);
            }
        }
    }

    public void onMenuItemDeleteClientClick() {
        int selectClient = tblViewClients.getSelectionModel().getSelectedItem().getId();
        runSQLDeleteClient(conn, selectClient);
        initTableClients();
    }

    public void onTableClientsClicked() {
        Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
        if (selectClient != null) {
            textFieldFirstName.setText(selectClient.getFirst_name());
            textFieldLastName.setText(selectClient.getLast_name());
            textFieldPhoneNumber.setText(selectClient.getPhone_number());
            comboBoxCity.getSelectionModel().select(obsListCity.stream().filter(x -> x.getName().equals(selectClient.getCity())).findFirst().get().getId() - 1);
            textFieldDistrict.setText(selectClient.getDistrict());
            textFieldStreet.setText(selectClient.getStreet());
            textFieldHouse.setText(String.valueOf(selectClient.getHouse()));
            textFieldApartment.setText(String.valueOf(selectClient.getApartment()));
        }
    }
}
