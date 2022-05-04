package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.model.Client;
import com.onlinestore.onlinestoresql.model.Order;
import com.onlinestore.onlinestoresql.model.Product;
import com.onlinestore.onlinestoresql.model.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.onlinestore.onlinestoresql.model.SQLrequest.*;

public class MainController {
    @FXML
    Button btnOrder, btnChangeDate, btnChangeStatus;
    @FXML
    ComboBox<String> comboBoxStatus;
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TableView<Order> tblViewOrders;
    @FXML
    TableView<Product> tblViewProducts;
    @FXML
    TextField textFieldDate;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Order> obsListOrders;
    ObservableList<Product> obsListProducts;
    ObservableList<Status> obsListStatus;


    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/OnlineStore", "postgres", "123");
            initTableClients();
            initTableProducts();
            initTableOrders();
            initComboBoxStatus();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void initComboBoxStatus() {
        obsListStatus = runSQLSelectStatus(conn);
        ObservableList<String> obsListOnlyStatus = FXCollections.observableArrayList();
        for (Status s: obsListStatus) {
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

        tblViewProducts.getColumns().clear();
        tblViewProducts.getColumns().add(colName);
        tblViewProducts.setItems(obsListProducts);

        colName.setCellValueFactory(el -> el.getValue().nameProperty());
    }

    public void initTableOrders() {
        obsListOrders = runSQLSelectOrders(conn);

        TableColumn<Order, String> colOrderId = new TableColumn<>("Order_ID");
        TableColumn<Order, String> colClientFIO = new TableColumn<>("Client_FIO");
        TableColumn<Order, String> colProductName = new TableColumn<>("Product_Name");
        TableColumn<Order, String> colOrderDate = new TableColumn<>("Order_date");
        TableColumn<Order, String> colStatus = new TableColumn<>("Status");

        tblViewOrders.getColumns().clear();
        tblViewOrders.getColumns().addAll(colOrderId, colClientFIO, colProductName, colOrderDate, colStatus);
        tblViewOrders.setItems(obsListOrders);

        colOrderId.setCellValueFactory(el -> el.getValue().order_idProperty());
        colClientFIO.setCellValueFactory(el -> el.getValue().client_fioProperty());
        colProductName.setCellValueFactory(el -> el.getValue().product_nameProperty());
        colOrderDate.setCellValueFactory(el -> el.getValue().order_dateProperty());
        colStatus.setCellValueFactory(el -> el.getValue().statusProperty());

        TableColumn<Order, Void> colButtonDelete = new TableColumn<>("Delete");
        tblViewOrders.getColumns().add(colButtonDelete);
        colButtonDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        Image icon = new Image(getClass().getResourceAsStream("/com/onlinestore/onlinestoresql/img/trash.png"));
                        btn.setGraphic(new ImageView(icon));
                        btn.setOnAction((ActionEvent event) -> {
                            Order currentOrder = getTableView().getItems().get(getIndex());
                            obsListOrders.remove(currentOrder);
                            runSQLDeleteOrder(conn, Integer.parseInt(currentOrder.getOrder_id()));
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
    }

    public void onButtonOrderClick() {
        Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
        Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
        runSQLInsertMakeOrder(conn, selectClient.getId(), selectProduct.getId());
        initTableOrders();
    }

    public void onButtonChangeDateClick() {
        Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
        String newDate = textFieldDate.getText();
        runSQLUpdateDate(conn, newDate, Integer.parseInt(selectOrder.getOrder_id()));
        initTableOrders();
    }

    public void onButtonChangeStatusClick() {
        Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
        int newStatus = obsListStatus.get(comboBoxStatus.getSelectionModel().getSelectedIndex()).getId();
        runSQLUpdateStatus(conn, newStatus, Integer.parseInt(selectOrder.getOrder_id()));
        initTableOrders();
    }

    public void onTableViewOrdersClicked() {
        Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
        textFieldDate.setText(selectOrder.getOrder_date());
        comboBoxStatus.setValue(selectOrder.getStatus());
    }
}