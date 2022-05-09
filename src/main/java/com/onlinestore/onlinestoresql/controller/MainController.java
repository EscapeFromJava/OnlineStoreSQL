package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Order;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import com.onlinestore.onlinestoresql.model.itemsSQL.Status;
import com.onlinestore.onlinestoresql.model.requestsSQL.Delete;
import com.onlinestore.onlinestoresql.model.requestsSQL.Insert;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import com.onlinestore.onlinestoresql.model.requestsSQL.Update;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteOrder;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertMakeOrder;

public class MainController {
    @FXML
    DatePicker datePickerCalendar;
    @FXML
    TableView<Client> tblViewClients;
    @FXML
    TableView<Order> tblViewOrders;
    @FXML
    TableView<Product> tblViewProducts;
    @FXML
    TextField textFieldTime, textFieldAddProductName, textFieldAddProductPrice, textFieldAddProductVolume;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Order> obsListOrders;
    ObservableList<Product> obsListProducts;
    ObservableList<Status> obsListStatus;

    public void initialize() {
        conn = MainApplication.conn;
        updateTables();
        initializeDatePicker();
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
        obsListStatus = Select.runSQLSelectStatus(conn);
    }



    public void initTableProducts() {
        obsListProducts = Select.runSQLSelectProducts(conn);

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        TableColumn<Product, Integer> colVolume = new TableColumn<>("Volume");

        tblViewProducts.getColumns().clear();
        tblViewProducts.getColumns().addAll(colName, colPrice, colVolume);
        tblViewProducts.setItems(obsListProducts);

        for (TableColumn currentColumn : tblViewProducts.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        colName.setCellValueFactory(el -> el.getValue().nameProperty());
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        colVolume.setCellValueFactory(new PropertyValueFactory<Product, Integer>("volume"));

        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(event -> {
            Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
            Update.runSQLUpdatePrice(conn, event.getNewValue(), selectProduct.getId());
        });
        colVolume.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colVolume.setOnEditCommit(event -> {
            Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
            Update.runSQLUpdateVolume(conn, event.getNewValue(), selectProduct.getId());
        });
    }
    private void initTableClients() {
        obsListClients = Select.runSQLSelectClients(conn);

        TableColumn<Client, String> colClient = new TableColumn<>("Client");

        tblViewClients.getColumns().clear();
        tblViewClients.getColumns().add(colClient);
        tblViewClients.setItems(obsListClients);

        colClient.setCellValueFactory(cellData -> Bindings.createStringBinding( () -> cellData.getValue().getLast_name() + " " + cellData.getValue().getFirst_name()));
    }

    public void initTableOrders() {
        obsListOrders = Select.runSQLSelectOrders(conn);
        ObservableList<String> obsListOnlyStatus = FXCollections.observableArrayList(obsListStatus.stream().map(x -> x.getStatus()).toList());

        TableColumn<Order, Integer> colOrderId = new TableColumn<>("Order_ID");
        TableColumn<Order, String> colClientFIO = new TableColumn<>("Client_FIO");
        TableColumn<Order, String> colProductName = new TableColumn<>("Product_Name");
        TableColumn<Order, String> colOrderDate = new TableColumn<>("Order_date");

        tblViewOrders.getColumns().clear();
        tblViewOrders.getColumns().addAll(colOrderId, colClientFIO, colProductName, colOrderDate);
        tblViewOrders.setItems(obsListOrders);

        colOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("order_id"));
        colClientFIO.setCellValueFactory(el -> el.getValue().client_fioProperty());
        colProductName.setCellValueFactory(el -> el.getValue().product_nameProperty());
        colOrderDate.setCellValueFactory(el -> el.getValue().order_dateProperty());

        TableColumn<Order, String> colStatus = new TableColumn<>("Status");
        tblViewOrders.getColumns().add(colStatus);
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
                                    // todo
                                    //  некореектно работает выполнение запроса:
                                    //  выбрать можно одну строку, а комбо бокс - в другой строке.
                                    //  либо переделать выбор комбобокса - либо лочить кнопка
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

        TableColumn<Order, Void> colButtonDelete = new TableColumn<>("Delete");
        tblViewOrders.getColumns().

                add(colButtonDelete);

        for (
                TableColumn currentColumn : tblViewOrders.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

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
            Update.runSQLUpdateDate(conn, newDate, selectOrder.getOrder_id());
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
    }

    public void onMenuItemAddProductClick() {
        if (!textFieldAddProductName.getText().equals("") && !textFieldAddProductPrice.getText().equals("") && !textFieldAddProductVolume.getText().equals("")) {
            String newProductName = textFieldAddProductName.getText();
            double newProductPrice = Double.parseDouble(textFieldAddProductPrice.getText());
            int newProductVolume = Integer.parseInt(textFieldAddProductVolume.getText());
            Insert.runSQLInsertAddProduct(conn, newProductName, newProductPrice, newProductVolume);
            initTableProducts();
            textFieldAddProductName.clear();
            textFieldAddProductPrice.clear();
            textFieldAddProductVolume.clear();
        }
    }

    public void onMenuItemDeleteProductClick() {
        Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
        if (selectProduct != null) {
            Delete.runSQLDeleteProduct(conn, selectProduct.getId());
            initTableProducts();
            initTableOrders();
        }
    }

    public void updateTables() {
        initComboBoxStatus();
        initTableProducts();
        initTableOrders();
        initTableClients();
    }



    public void onButtonRefreshAllClick() {
        updateTables();
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
}