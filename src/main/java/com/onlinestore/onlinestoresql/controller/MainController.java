package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.model.Client;
import com.onlinestore.onlinestoresql.model.Order;
import com.onlinestore.onlinestoresql.model.Product;
import com.onlinestore.onlinestoresql.model.SQLrequest;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.onlinestore.onlinestoresql.model.SQLrequest.runSQLDeleteOrder;
import static com.onlinestore.onlinestoresql.model.SQLrequest.runSQLInsertMakeOrder;

public class MainController {

    @FXML
    Button btnOrder;
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TableView<Order> tblViewOrders;
    @FXML
    TableView<Product> tblViewProducts;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Order> obsListOrders;
    ObservableList<Product> obsListProducts;


    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/OnlineStore", "postgres", "123");
            initTableClients();
            initTableProducts();
            initTableOrders();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void initTableClients() {
        obsListClients = SQLrequest.runSQLSelectClients(conn);

        TableColumn<Client, String> colFio = new TableColumn<>("FIO");

        tblViewClients.getColumns().clear();
        tblViewClients.getColumns().add(colFio);
        tblViewClients.setItems(obsListClients);

        colFio.setCellValueFactory(el -> el.getValue().fioProperty());
    }

    public void initTableProducts() {
        obsListProducts = SQLrequest.runSQLSelectProducts(conn);

        TableColumn<Product, String> colName = new TableColumn<>("Name");

        tblViewProducts.getColumns().clear();
        tblViewProducts.getColumns().add(colName);
        tblViewProducts.setItems(obsListProducts);

        colName.setCellValueFactory(el -> el.getValue().nameProperty());
    }

    public void initTableOrders() {
        obsListOrders = SQLrequest.runSQLSelectOrders(conn);

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
}