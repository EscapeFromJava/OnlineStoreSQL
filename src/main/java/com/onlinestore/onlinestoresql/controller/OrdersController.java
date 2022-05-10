package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.entity.ProductInBasket;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Order;
import com.onlinestore.onlinestoresql.model.itemsSQL.Status;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteOrder;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.*;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Update.runSQLUpdateStatus;

public class OrdersController {
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TableView<Order> tblViewOrderNumbers;
    @FXML
    TableView<Order> tblViewCurrentOrder;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Order> obsListOrders;
    ObservableList<Order> obsListCurrentOrder;
    ObservableList<Status> obsListStatus;

    public void initialize() {
        conn = MainApplication.conn;
        updateTables();
    }

    public void initComboBoxStatus() {
        obsListStatus = Select.runSQLSelectStatus(conn);
    }

    private void initTableClients() {
        obsListClients = runSQLSelectClients(conn);

        TableColumn<Client, Integer> colId = new TableColumn<>("ID");
        TableColumn<Client, String> colLastName = new TableColumn<>("Last name");
        TableColumn<Client, String> colFirstName = new TableColumn<>("First name");

        tblViewClients.getColumns().clear();
        tblViewClients.getColumns().addAll(colId, colLastName, colFirstName);
        tblViewClients.setItems(obsListClients);

        colId.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
        colLastName.setCellValueFactory(el -> el.getValue().last_nameProperty());
        colFirstName.setCellValueFactory(el -> el.getValue().first_nameProperty());

        tblViewClients.getSortOrder().setAll(colId);
        tblViewClients.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        Client selectClient = tblViewClients.getSelectionModel().getSelectedItem();
                        obsListOrders = runSQLSelectOrderNumbers(conn, selectClient.getId());
                        initListOrderNumbers(obsListOrders);
                        if (obsListCurrentOrder != null)
                            obsListCurrentOrder.clear();
                    }
                }
            }
        });

        for (TableColumn currentColumn : tblViewClients.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");
    }

    public void initListOrderNumbers(ObservableList<Order> obsListOrders){
        initComboBoxStatus();
        ObservableList<String> obsListOnlyStatus = FXCollections.observableArrayList(obsListStatus.stream().map(x -> x.getStatus()).toList());

        TableColumn<Order, Integer> colOrderId = new TableColumn<>("ID");
        TableColumn<Order, String> colOrderDate = new TableColumn<>("Order Date");
        TableColumn<Order, Double> colTotalCost = new TableColumn<>("Total Cost");
        TableColumn<Order, String> colStatus = new TableColumn<>("Status");
        TableColumn<Order, Void> colButtonDelete = new TableColumn<>();

        tblViewOrderNumbers.getColumns().clear();
        tblViewOrderNumbers.getColumns().addAll(colOrderId, colOrderDate, colTotalCost, colStatus, colButtonDelete);
        tblViewOrderNumbers.setItems(obsListOrders);

        colOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("order_id"));
        colOrderDate.setCellValueFactory(el -> el.getValue().order_dateProperty());
        colTotalCost.setCellFactory(new Callback<TableColumn<Order, Double>, TableCell<Order, Double>>() {
            @Override
            public TableCell<Order, Double> call(final TableColumn<Order, Double> param) {
                final TableCell<Order, Double> cell = new TableCell<Order, Double>() {
                    private final Label lblTotalCost = new Label();

                    {
                    }
                    @Override
                    public void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(lblTotalCost);
                            Order currentOrder = tblViewOrderNumbers.getSelectionModel().getTableView().getItems().get(getIndex());
                            double totalPrice = runSQLSelectTotalCost(conn, currentOrder.getOrder_id());
                            lblTotalCost.setText(String.valueOf(totalPrice));
                        }
                    }
                };
                return cell;
            }
        });
        colStatus.setCellValueFactory(el -> el.getValue().statusProperty());
        colStatus.setCellFactory(new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
            @Override
            public TableCell<Order, String> call(final TableColumn<Order, String> param) {
                final TableCell<Order, String> cell = new TableCell<Order, String>() {
                    private final ComboBox comboBoxStatus = new ComboBox();

                    {
                        comboBoxStatus.setItems(obsListOnlyStatus);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(comboBoxStatus);
                            comboBoxStatus.setValue(item);
                            comboBoxStatus.setOnAction(event -> {
                                Order selectOrder = tblViewOrderNumbers.getSelectionModel().getSelectedItem();
                                if (selectOrder != null && comboBoxStatus.isFocused()) {
                                    int idStatus = obsListStatus.stream().filter(x -> x.getStatus().equals(comboBoxStatus.getValue())).findFirst().get().getId();
                                    runSQLUpdateStatus(conn, idStatus, selectOrder.getOrder_id());
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        });

        colButtonDelete.setCellFactory(new Callback<TableColumn<Order, Void>, TableCell<Order, Void>>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
                    private final Button btnDelete = new Button();

                    {
                        Image icon = new Image(getClass().getResourceAsStream("/com/onlinestore/onlinestoresql/img/trash.png"));
                        btnDelete.setGraphic(new ImageView(icon));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDelete);
                            btnDelete.setOnAction((ActionEvent event) -> {
                                Order currentOrder = getTableView().getItems().get(getIndex());
                                obsListOrders.remove(currentOrder);
                                runSQLDeleteOrder(conn, currentOrder.getOrder_id());
                                if (obsListCurrentOrder != null)
                                    obsListCurrentOrder.clear();
                            });
                        }
                    }
                };
                return cell;
            }
        });

        for (TableColumn currentColumn : tblViewOrderNumbers.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        tblViewOrderNumbers.getSortOrder().setAll(colOrderId);

        tblViewOrderNumbers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        int selectOrder = tblViewOrderNumbers.getSelectionModel().getSelectedItem().getOrder_id();
                        obsListCurrentOrder = runSQLSelectCurrentOrder(conn, selectOrder);
                        initTableCurrentOrder(obsListCurrentOrder);
                    }
                }
            }
        });
    }
    public void initTableCurrentOrder(ObservableList<Order> obsListCurrentOrder) {
        TableColumn<Order, String> colProduct = new TableColumn<>("Product");
        TableColumn<Order, Double> colPrice = new TableColumn<>("Price");
        TableColumn<Order, Integer> colQuantity = new TableColumn<>("Quantity");

        tblViewCurrentOrder.getColumns().clear();
        tblViewCurrentOrder.getColumns().addAll(colProduct, colPrice, colQuantity);
        tblViewCurrentOrder.setItems(obsListCurrentOrder);

        colProduct.setCellValueFactory(el -> el.getValue().productProperty());
        colQuantity.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Order, Double>("price"));

        for (TableColumn currentColumn : tblViewCurrentOrder.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        tblViewCurrentOrder.getSortOrder().setAll(colProduct);
    }
    public void onButtonClientsClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/clients-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Clients");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/clients.png")));
            stage.setMinWidth(1200);
            stage.setMinHeight(800);
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

    public void onButtonProductsClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/products-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Products");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/products.png")));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
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

    public void onButtonNewOrderClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/new-order-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Order");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/new_order.png")));
            stage.setMinWidth(800);
            stage.setMinHeight(600);
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

    public void onButtonRefreshAllClick() {
        updateTables();
    }

    public void updateTables() {
        initComboBoxStatus();
        initTableClients();
    }
}