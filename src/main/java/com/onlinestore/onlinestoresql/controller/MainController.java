package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Order;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import com.onlinestore.onlinestoresql.model.itemsSQL.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.onlinestore.onlinestoresql.model.SQLrequest.*;

public class MainController {
    @FXML
    AnchorPane anchorPainMain;
    @FXML
    ComboBox<String> comboBoxStatus;
    @FXML
    DatePicker datePickerCalendar;
    @FXML
    MenuItem menuItemAddClient, menuItemDeleteClient, menuItemAddProduct, menuItemDeleteProduct;
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TableView<Order> tblViewOrders;
    @FXML
    TableView<Product> tblViewProducts;
    @FXML
    TextField textFieldTime, textFieldAddClient, textFieldAddProductName, textFieldAddProductPrice, textFieldAddProductVolume;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Order> obsListOrders;
    ObservableList<Product> obsListProducts;
    ObservableList<Status> obsListStatus;


    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/OnlineStore", "postgres", "123");
            updateAllTables();
            initializeDatePicker();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void initializeDatePicker() {
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        datePickerCalendar.setConverter(converter);
    }

    public void initComboBoxStatus() {
        obsListStatus = runSQLSelectStatus(conn);
        ObservableList<String> obsListOnlyStatus = FXCollections.observableArrayList();
        for (Status s : obsListStatus) {
            obsListOnlyStatus.add(s.getStatus());
        }
        comboBoxStatus.setItems(obsListOnlyStatus);
    }

    public void initTableClients() {
        obsListClients = runSQLSelectClients(conn);

        TableColumn<Client, String> colFio = new TableColumn<>("FIO");

        tblViewClients.getColumns().clear();
        tblViewClients.getColumns().add(colFio);
        tblViewClients.setItems(obsListClients);

        colFio.setCellValueFactory(el -> el.getValue().fioProperty());
    }

    public void initTableProducts() {
        obsListProducts = runSQLSelectProducts(conn);

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        TableColumn<Product, Integer> colVolume = new TableColumn<>("Volume");

        tblViewProducts.getColumns().clear();
        tblViewProducts.getColumns().addAll(colName, colPrice, colVolume);
        tblViewProducts.setItems(obsListProducts);

        colName.setCellValueFactory(el -> el.getValue().nameProperty());
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        colVolume.setCellValueFactory(new PropertyValueFactory<Product, Integer>("volume"));
    }

    public void initTableOrders() {
        obsListOrders = runSQLSelectOrders(conn);

        TableColumn<Order, Integer> colOrderId = new TableColumn<>("Order_ID");
        TableColumn<Order, String> colClientFIO = new TableColumn<>("Client_FIO");
        TableColumn<Order, String> colProductName = new TableColumn<>("Product_Name");
        TableColumn<Order, String> colOrderDate = new TableColumn<>("Order_date");
        TableColumn<Order, String> colStatus = new TableColumn<>("Status");

        tblViewOrders.getColumns().clear();
        tblViewOrders.getColumns().addAll(colOrderId, colClientFIO, colProductName, colOrderDate, colStatus);
        tblViewOrders.setItems(obsListOrders);

//        colOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("order_id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("order_id"));
        colClientFIO.setCellValueFactory(el -> el.getValue().client_fioProperty());
        colProductName.setCellValueFactory(el -> el.getValue().product_nameProperty());
        colOrderDate.setCellValueFactory(el -> el.getValue().order_dateProperty());
        colStatus.setCellValueFactory(el -> el.getValue().statusProperty());

        TableColumn<Order, Void> colButtonDelete = new TableColumn<>("Delete");
        tblViewOrders.getColumns().add(colButtonDelete);
        colButtonDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
                    private final Button btn = new Button();

                    {
                        Image icon = new Image(getClass().getResourceAsStream("/com/onlinestore/onlinestoresql/img/trash.png"));
                        btn.setGraphic(new ImageView(icon));
                        btn.setOnAction((ActionEvent event) -> {
                            Order currentOrder = getTableView().getItems().get(getIndex());
                            obsListOrders.remove(currentOrder);
                            runSQLDeleteOrder(conn, currentOrder.getOrder_id());
                            initTableOrders();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        });

        tblViewOrders.getSortOrder().setAll(colOrderId);
    }

    public void onButtonOrderClick() {
        Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
        Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
        if (selectClient != null && selectProduct != null) {
            runSQLInsertMakeOrder(conn, selectClient.getId(), selectProduct.getId());
            initTableOrders();
        }
    }

    public void onButtonChangeClick() {
        if (tblViewOrders.getSelectionModel().getSelectedItem() != null) {
            Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
            String newDate = datePickerCalendar.getValue() + " " + textFieldTime.getText();
            int newStatus = obsListStatus.get(comboBoxStatus.getSelectionModel().getSelectedIndex()).getId();
            runSQLUpdateDate(conn, newDate, selectOrder.getOrder_id());
            runSQLUpdateStatus(conn, newStatus, selectOrder.getOrder_id());
            initTableOrders();
        }
    }

    public void onTableViewOrdersClicked() {
        Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
        if (selectOrder.getOrder_date() != null) {
            LocalDate localDate = LocalDate.parse(selectOrder.getOrder_date().substring(0, 10));
            datePickerCalendar.setValue(localDate);
            textFieldTime.setText(selectOrder.getOrder_date().substring(11));
        }
        if (selectOrder.getStatus() != null) {
            comboBoxStatus.setValue(selectOrder.getStatus());
        }
    }

    public void onMenuItemAddClientClick() {
        if (!textFieldAddClient.getText().equals("")) {
            String newFIO = textFieldAddClient.getText();
            runSQLInsertAddClient(conn, newFIO);
            initTableClients();
            textFieldAddClient.clear();
        }
    }

    public void onMenuItemDeleteClientClick() {
        Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
        if (selectClient != null) {
            runSQLDeleteClient(conn, selectClient.getId());
            initTableClients();
            initTableOrders();
        }
    }

    public void onMenuItemAddProductClick() {
        if (!textFieldAddProductName.getText().equals("") && !textFieldAddProductPrice.getText().equals("") && !textFieldAddProductVolume.getText().equals("")) {
            String newProductName = textFieldAddProductName.getText();
            double newProductPrice = Double.parseDouble(textFieldAddProductPrice.getText());
            int newProductVolume = Integer.parseInt(textFieldAddProductVolume.getText());
            runSQLInsertAddProduct(conn, newProductName, newProductPrice, newProductVolume);
            initTableProducts();
            textFieldAddProductName.clear();
            textFieldAddProductPrice.clear();
            textFieldAddProductVolume.clear();
        }
    }

    public void onMenuItemDeleteProductClick() {
        Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
        if (selectProduct != null) {
            runSQLDeleteProduct(conn, selectProduct.getId());
            initTableProducts();
            initTableOrders();
        }
    }

    public void updateAllTables() {
        initTableClients();
        initTableProducts();
        initTableOrders();
        initComboBoxStatus();
    }

    public void onButtonRefreshAllClick() {
        updateAllTables();
    }
}