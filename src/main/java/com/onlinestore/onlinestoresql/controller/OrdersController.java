package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.*;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import com.onlinestore.onlinestoresql.model.requestsSQL.Update;
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
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectClients;

public class OrdersController {
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TableView<Order> tblViewOrders;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Order> obsListOrders;
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
                        obsListOrders = Select.runSQLSelectOrders(conn, selectClient.getId());
                        initTableOrders();
                    }
                }
            }
        });
    }

    public void initTableOrders() {
        initComboBoxStatus();
        ObservableList<String> obsListOnlyStatus = FXCollections.observableArrayList(obsListStatus.stream().map(x -> x.getStatus()).toList());

        TableColumn<Order, Integer> colOrderId = new TableColumn<>("ID");
        TableColumn<Order, String> colProduct = new TableColumn<>("Product");
        TableColumn<Order, Double> colPrice = new TableColumn<>("Price");
        TableColumn<Order, Integer> colQuantity = new TableColumn<>("Quantity");
        TableColumn<Order, String> colOrderDate = new TableColumn<>("Order date");
        TableColumn<Order, String> colStatus = new TableColumn<>("Status");
        TableColumn<Order, Void> colButtonDelete = new TableColumn<>("Del");
        colOrderId.setMinWidth(40);
        colOrderId.setMaxWidth(40);
        colProduct.setMinWidth(300);
        colPrice.setMinWidth(120);
        colPrice.setMaxWidth(120);
        colQuantity.setMinWidth(80);
        colQuantity.setMaxWidth(80);
        colOrderDate.setMinWidth(200);
        colOrderDate.setMaxWidth(200);
        colStatus.setMinWidth(100);
        colStatus.setMaxWidth(100);
        colButtonDelete.setMinWidth(40);
        colButtonDelete.setMaxWidth(40);


        tblViewOrders.getColumns().clear();
        tblViewOrders.getColumns().addAll(colOrderId, colProduct, colPrice, colQuantity, colOrderDate, colStatus, colButtonDelete);
        tblViewOrders.setItems(obsListOrders);

        colOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("order_id"));
        colProduct.setCellValueFactory(el -> el.getValue().productProperty());
        colQuantity.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<Order, Double>("price"));
        colOrderDate.setCellValueFactory(el -> el.getValue().order_dateProperty());
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
                                Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
                                if (selectOrder != null && comboBoxStatus.isFocused()) {
                                    int idStatus = obsListStatus.stream().filter(x -> x.getStatus().equals(comboBoxStatus.getValue())).findFirst().get().getId();
                                    Update.runSQLUpdateStatus(conn, idStatus, selectOrder.getOrder_id());
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        });

        colStatus.setCellValueFactory(order ->
        {
            return order.getValue().statusProperty();
        });
        colButtonDelete.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<Order, Void>() {
                    private final Button btnDelete = new Button();

                    {
                        Image icon = new Image(getClass().getResourceAsStream("/com/onlinestore/onlinestoresql/img/trash.png"));
                        btnDelete.setGraphic(new ImageView(icon));
                        btnDelete.setOnAction((ActionEvent event) -> {
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
                            setGraphic(btnDelete);
                        }
                    }
                };
                return cell;
            }
        });

        for (TableColumn currentColumn : tblViewOrders.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        tblViewOrders.getSortOrder().setAll(colOrderId);
    }

    public void onTableViewOrdersClicked() {
        Order selectOrder = tblViewOrders.getSelectionModel().getSelectedItem();
        if (selectOrder.getOrder_date() != null) {
        }
    }

    public void updateTables() {
        initComboBoxStatus();
        initTableClients();
    }




    public void onButtonClientsClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/clients-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Clients");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/clients.png")));
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
}