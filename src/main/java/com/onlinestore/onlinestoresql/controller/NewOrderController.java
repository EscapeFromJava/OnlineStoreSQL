package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.entity.ProductInBasket;
import com.onlinestore.onlinestoresql.model.itemsSQL.Category;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.Connection;
import java.util.Comparator;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertOrder;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.*;

public class NewOrderController {
    @FXML
    Button btnOrder;
    @FXML
    ComboBox<Category> comboBoxCategory;
    @FXML
    ComboBox<Client> comboBoxClients;
    @FXML
    Label lblResultOperation, lblTotalOrderCost;
    @FXML
    TableView<ProductInBasket> tblViewBasket;
    @FXML
    TableView<Product> tblViewProducts;
    int idClient;
    Connection conn;
    ObservableList<Client> obsListClients;
    ObservableList<Category> obsListCategories;
    ObservableList<Product> obsListProducts;
    ObservableList<ProductInBasket> obsListNewOrder = FXCollections.observableArrayList();

    public void initialize() {
        conn = MainApplication.conn;
        updateTables();
        obsListNewOrder.addListener((ListChangeListener<ProductInBasket>) change -> {
            updateTableBasket();
        });

    }

    private void initComboBoxClients() {
        obsListClients = runSQLSelectClients(conn).sorted(Comparator.comparingInt(Client::getId));
        comboBoxClients.setCellFactory(p -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getLast_name() + " " + item.getFirst_name());
                } else {
                    setText(null);
                }
            }
        });
        comboBoxClients.setItems(obsListClients);
        comboBoxClients.getSelectionModel().selectedItemProperty().addListener((observableValue, client, t1) -> {
            Client selectClient = comboBoxClients.getSelectionModel().getSelectedItem();
            if (selectClient != null && comboBoxClients.isFocused()) {
                idClient = selectClient.getId();
            }
        });
    }

    private void initComboBoxCategories() {
        obsListCategories = runSQLSelectCategory(conn).sorted(Comparator.comparingInt(Category::getId));
        comboBoxCategory.setCellFactory(p -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getCategory());
                } else {
                    setText(null);
                }
            }
        });
        comboBoxCategory.setItems(obsListCategories);
        comboBoxCategory.getSelectionModel().selectedItemProperty().addListener((observableValue, client, t1) -> {
            Category selectCategory = comboBoxCategory.getSelectionModel().getSelectedItem();
            if (selectCategory != null && comboBoxCategory.isFocused()) {
                int idCategory = selectCategory.getId();
                initTableProducts(idCategory);
            }
        });
    }

    public void updateTotalCost() {
        double totalCost = 0;
        for (ProductInBasket el : obsListNewOrder) {
            totalCost += el.getTotalCost();
        }
        lblTotalOrderCost.setText(String.valueOf(totalCost));
    }

    private void updateTableBasket() {
        TableColumn<ProductInBasket, String> colBrand = new TableColumn<>("Brand");
        TableColumn<ProductInBasket, String> colName = new TableColumn<>("Name");
        TableColumn<ProductInBasket, Double> colPrice = new TableColumn<>("Price");
        TableColumn<ProductInBasket, Integer> colVolume = new TableColumn<>("Volume");
        TableColumn<ProductInBasket, Double> colTotalCost = new TableColumn<>("Cost");
        TableColumn<ProductInBasket, Void> colButtonDelete = new TableColumn<>();

        tblViewBasket.getColumns().clear();
        tblViewBasket.getColumns().addAll(colBrand, colName, colPrice, colVolume, colTotalCost, colButtonDelete);
        tblViewBasket.setItems(obsListNewOrder);

        colBrand.setCellValueFactory(el -> el.getValue().brandProperty());
        colName.setCellValueFactory(el -> el.getValue().nameProperty());
        colPrice.setCellValueFactory(new PropertyValueFactory<ProductInBasket, Double>("price"));
        colVolume.setCellFactory(new Callback<TableColumn<ProductInBasket, Integer>, TableCell<ProductInBasket, Integer>>() {
            @Override
            public TableCell<ProductInBasket, Integer> call(final TableColumn<ProductInBasket, Integer> param) {
                final TableCell<ProductInBasket, Integer> cell = new TableCell<ProductInBasket, Integer>() {
                    private final HBox hboxVolume = new HBox();
                    private final Button buttonMinus = new Button("-");
                    private final Button buttonPlus = new Button("+");
                    private final TextField textFieldVolume = new TextField();

                    {
                        hboxVolume.getChildren().addAll(buttonMinus, textFieldVolume, buttonPlus);
                        buttonMinus.setMinWidth(25);
                        buttonMinus.setMaxWidth(25);
                        buttonPlus.setMinWidth(25);
                        buttonPlus.setMaxWidth(25);
                    }

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ProductInBasket productInBasket = tblViewBasket.getSelectionModel().getTableView().getItems().get(getIndex());
                            setGraphic(hboxVolume);
                            textFieldVolume.setText(String.valueOf(productInBasket.getVolume()));
                            buttonMinus.setOnAction((ActionEvent event) -> {
                                if (Integer.parseInt(textFieldVolume.getText()) > 0) {
                                    productInBasket.setVolume(productInBasket.getVolume() - 1);
                                    updateProductInBasketVolume(productInBasket);
                                }
                            });
                            buttonPlus.setOnAction((ActionEvent event) -> {
                                productInBasket.setVolume(productInBasket.getVolume() + 1);
                                updateProductInBasketVolume(productInBasket);
                            });
                        }
                    }

                    private void updateProductInBasketVolume(ProductInBasket productInBasket) {
                        productInBasket.setTotalCost(productInBasket.getPrice() * productInBasket.getVolume());
                        textFieldVolume.setText(String.valueOf(productInBasket.getVolume()));
                        updateTotalCost();
                        updateTableBasket();
                    }
                };
                return cell;
            }
        });

        colTotalCost.setCellFactory(new Callback<TableColumn<ProductInBasket, Double>, TableCell<ProductInBasket, Double>>() {
            @Override
            public TableCell<ProductInBasket, Double> call(final TableColumn<ProductInBasket, Double> param) {
                final TableCell<ProductInBasket, Double> cell = new TableCell<ProductInBasket, Double>() {
                    private final Label lblCost = new Label();

                    {

                    }

                    @Override
                    public void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(lblCost);
                            ProductInBasket productInBasket = tblViewBasket.getSelectionModel().getTableView().getItems().get(getIndex());
                            lblCost.setText(String.valueOf(productInBasket.getTotalCost()));
                        }
                    }
                };
                return cell;
            }
        });

        colButtonDelete.setCellFactory(new Callback<TableColumn<ProductInBasket, Void>, TableCell<ProductInBasket, Void>>() {
            @Override
            public TableCell<ProductInBasket, Void> call(final TableColumn<ProductInBasket, Void> param) {
                final TableCell<ProductInBasket, Void> cell = new TableCell<ProductInBasket, Void>() {
                    private final Button btnDelete = new Button();

                    {
                        Image icon = new Image(getClass().getResourceAsStream("/com/onlinestore/onlinestoresql/img/trash.png"));
                        btnDelete.setGraphic(new ImageView(icon));
                        btnDelete.setOnAction((ActionEvent event) -> {
                            ProductInBasket currentProduct = getTableView().getItems().get(getIndex());
                            obsListNewOrder.remove(currentProduct);
                            updateTotalCost();
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

        for (TableColumn currentColumn : tblViewBasket.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");
    }

    private void updateTables() {
        initComboBoxClients();
        initComboBoxCategories();
    }

    private void initTableProducts(int idCategory) {
        obsListProducts = runSQLSelectProductsByCategory(conn, idCategory);
        TableColumn<Product, String> colBrand = new TableColumn<>("Brand");
        TableColumn<Product, String> colName = new TableColumn<>("Name");
        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        TableColumn<Product, Integer> colVolume = new TableColumn<>("Volume");

        tblViewProducts.getColumns().clear();
        tblViewProducts.getColumns().addAll(colBrand, colName, colPrice, colVolume);
        tblViewProducts.setItems(obsListProducts);

        colBrand.setCellValueFactory(el -> el.getValue().brandProperty());
        colName.setCellValueFactory(el -> el.getValue().nameProperty());
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        colVolume.setCellValueFactory(new PropertyValueFactory<Product, Integer>("volume"));

        tblViewProducts.getSortOrder().setAll(colName);
        tblViewProducts.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
                        obsListNewOrder.add(new ProductInBasket(selectProduct.getId(), selectProduct.getBrand(), selectProduct.getName(), selectProduct.getPrice()));
                    }
                }
            }
        });
    }

    public void onButtonOrderClick() {
        if (idClient != 0 && obsListNewOrder.size() > 0) {
            runSQLInsertOrder(conn, idClient, obsListNewOrder);
            lblResultOperation.setText("Order added!");
        } else {
            lblResultOperation.setText("Error!");
        }
    }

    public void onButtonClearClick() {
        obsListNewOrder.clear();
        lblResultOperation.setText("");
    }
}
