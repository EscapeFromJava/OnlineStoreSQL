package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.City;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.util.Comparator;
import java.util.regex.Pattern;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectCity;
import static com.onlinestore.onlinestoresql.model.requestsSQL.UpdateClient.*;

public class ClientsController {
    @FXML
    AnchorPane paneAddClient;
    @FXML
    ComboBox<String> comboBoxCity;
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TextField textFieldFirstName, textFieldLastName, textFieldPhoneNumber, textFieldDistrict, textFieldStreet, textFieldHouse, textFieldApartment;
    ObservableList<Client> obsListClients;
    ObservableList<City> obsListCity;
    ObservableList<String> obsListOnlyName;
    Connection conn;

    public void initialize() {
        conn = MainApplication.conn;
        updateTables();
    }

    private void updateTables() {
        initComboBoxCity();
        initTableClients();
    }

    private void initComboBoxCity() {
        obsListCity = runSQLSelectCity(conn).sorted(Comparator.comparingInt(City::getId));
        obsListOnlyName = FXCollections.observableArrayList(obsListCity.stream().map(x -> x.getName()).toList());
        comboBoxCity.setItems(obsListOnlyName);
        comboBoxCity.getSelectionModel().select(0);
    }

    public void initTableClients() {
        obsListClients = Select.runSQLSelectClients(conn);

        TableColumn<Client, String> colLastName = new TableColumn<>("Last name");
        TableColumn<Client, String> colFirstName = new TableColumn<>("First name");
        TableColumn<Client, String> colPhoneNumber = new TableColumn<>("Phone number");
        TableColumn<Client, String> colCity = new TableColumn<>("City");
        TableColumn<Client, String> colDistrict = new TableColumn<>("District");
        TableColumn<Client, String> colStreet = new TableColumn<>("Street");
        TableColumn<Client, Integer> colHouse = new TableColumn<>("House");
        TableColumn<Client, Integer> colApartment = new TableColumn<>("Apartment");

        tblViewClients.getColumns().clear();
        tblViewClients.getColumns().addAll(colLastName, colFirstName, colPhoneNumber, colCity, colDistrict, colStreet, colHouse, colApartment);
        tblViewClients.setItems(obsListClients);

        for (TableColumn currentColumn : tblViewClients.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        colLastName.setCellValueFactory(el -> el.getValue().last_nameProperty());
        colFirstName.setCellValueFactory(el -> el.getValue().first_nameProperty());
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

        colLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        colLastName.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientLastName(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        colPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhoneNumber.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientPhoneNumber(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        colCity.setCellFactory(new Callback<TableColumn<Client, String>, TableCell<Client, String>>() {
            @Override
            public TableCell<Client, String> call(final TableColumn<Client, String> param) {
                final TableCell<Client, String> cell = new TableCell<Client, String>() {

                    private final ComboBox comboBoxCity = new ComboBox();

                    {
                        comboBoxCity.setItems(obsListOnlyName);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(comboBoxCity);
                            comboBoxCity.setValue(item);
                            comboBoxCity.setOnAction(event -> {
                                Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
                                if (selectClient != null && comboBoxCity.isFocused()) {
                                    String newCity = comboBoxCity.getValue().toString();
                                    runSQLUpdateClientCity(conn, newCity, selectClient.getId());
                                    initTableClients();
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        });

        colDistrict.setCellFactory(TextFieldTableCell.forTableColumn());
        colDistrict.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientDistrict(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        colStreet.setCellFactory(TextFieldTableCell.forTableColumn());
        colStreet.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientStreet(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        colHouse.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colHouse.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientHouse(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        colApartment.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colApartment.setOnEditCommit(event -> {
            Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
            runSQLUpdateClientApartment(conn, event.getNewValue(), selectClient.getId());
            initTableClients();
        });

        tblViewClients.getSortOrder().setAll(colLastName);
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
            if (el instanceof TextField) {
                ((TextField) el).clear();
                el.setStyle(null);
            }
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
    public void onButtonAddCityClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/сities-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Cities");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/add_city.png")));
            stage.setMinWidth(320);
            stage.setMinHeight(440);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    updateTables();
                }
            });
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
