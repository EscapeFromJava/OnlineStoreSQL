package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.Brand;
import com.onlinestore.onlinestoresql.model.itemsSQL.Category;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import com.onlinestore.onlinestoresql.model.requestsSQL.Select;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.util.Comparator;
import java.util.regex.Pattern;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteProduct;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddClient;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddProduct;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectBrand;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectCategory;
import static com.onlinestore.onlinestoresql.model.requestsSQL.UpdateProduct.*;

public class ProductsController {
    @FXML
    AnchorPane paneAddProduct;
    @FXML
    ComboBox<String> comboBoxBrand, comboBoxCategory;
    @FXML
    TableView<Product> tblViewProducts;
    @FXML
    TextField textFieldName, textFieldPrice, textFieldVolume;
    ObservableList<Product> obsListProducts;
    ObservableList<Brand> obsListBrands;
    ObservableList<String> obsListOnlyBrands;
    ObservableList<Category> obsListCategories;
    ObservableList<String> obsListOnlyCategories;
    Connection conn;

    public void initialize() {
        conn = MainApplication.conn;
        updateTables();
    }

    private void updateTables() {
        initTableProducts();
    }

    private void initComboBoxCategory() {
        obsListCategories = runSQLSelectCategory(conn).sorted(Comparator.comparingInt(Category::getId));
        obsListOnlyCategories = FXCollections.observableArrayList(obsListCategories.stream().map(x -> x.getCategory()).toList());
        comboBoxCategory.setItems(obsListOnlyCategories);
        comboBoxCategory.getSelectionModel().select(0);
    }

    private void initComboBoxBrand() {
        obsListBrands = runSQLSelectBrand(conn).sorted(Comparator.comparingInt(Brand::getId));
        obsListOnlyBrands = FXCollections.observableArrayList(obsListBrands.stream().map(x -> x.getBrand()).toList());
        comboBoxBrand.setItems(obsListOnlyBrands);
        comboBoxBrand.getSelectionModel().select(0);
    }

    private void initTableProducts() {
        initComboBoxBrand();
        initComboBoxCategory();
        obsListProducts = Select.runSQLSelectProducts(conn);

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        TableColumn<Product, Integer> colVolume = new TableColumn<>("Volume");
        TableColumn<Product, String> colBrand = new TableColumn<>("Brand");
        TableColumn<Product, String> colCategory = new TableColumn<>("Category");

        tblViewProducts.getColumns().clear();
        tblViewProducts.getColumns().addAll(colName, colPrice, colVolume, colBrand, colCategory);
        tblViewProducts.setItems(obsListProducts);

        for (TableColumn currentColumn : tblViewProducts.getColumns())
            currentColumn.setStyle("-fx-alignment: CENTER;");

        colName.setCellValueFactory(el -> el.getValue().nameProperty());
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        colVolume.setCellValueFactory(new PropertyValueFactory<Product, Integer>("volume"));
        colBrand.setCellValueFactory(el -> el.getValue().brandProperty());
        colCategory.setCellValueFactory(el -> el.getValue().categoryProperty());

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
            runSQLUpdateProductName(conn, event.getNewValue(), selectProduct.getId());
            initTableProducts();
        });

        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colPrice.setOnEditCommit(event -> {
            Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
            runSQLUpdateProductPrice(conn, event.getNewValue(), selectProduct.getId());
            initTableProducts();
        });

        colVolume.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colVolume.setOnEditCommit(event -> {
            Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
            runSQLUpdateProductVolume(conn, event.getNewValue(), selectProduct.getId());
            initTableProducts();
        });

        colBrand.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<Product, String>() {

                    private final ComboBox comboBoxBrand = new ComboBox();

                    {
                        comboBoxBrand.setItems(obsListOnlyBrands);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(comboBoxBrand);
                            comboBoxBrand.setValue(item);
                            comboBoxBrand.setOnAction(event -> {
                                Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
                                if (selectProduct != null && comboBoxBrand.isFocused()) {
                                    String newBrand = comboBoxBrand.getValue().toString();
                                    runSQLUpdateProductBrand(conn, newBrand, selectProduct.getId());
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        });

        colCategory.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell<Product, String> call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<Product, String>() {

                    private final ComboBox comboBoxCategory = new ComboBox();

                    {
                        comboBoxCategory.setItems(obsListOnlyCategories);
                    }

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(comboBoxCategory);
                            comboBoxCategory.setValue(item);
                            comboBoxCategory.setOnAction(event -> {
                                Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
                                if (selectProduct != null && comboBoxCategory.isFocused()) {
                                    String newCategory = comboBoxCategory.getValue().toString();
                                    runSQLUpdateProductCategory(conn, newCategory, selectProduct.getId());
                                }
                            });
                        }
                    }
                };
                return cell;
            }
        });

        tblViewProducts.getSortOrder().setAll(colName);
    }

    public void onButtonAddProductClick() {
        for (Node el : paneAddProduct.getChildren()) {
            if (el instanceof TextField) {
                if (((TextField) el).getText().equals("")) {
                    el.setStyle("-fx-border-color: red;");
                } else
                    el.setStyle(null);
            }
        }

        Pattern patternDouble = Pattern.compile("-?\\d+(\\.\\d+)?");
        Pattern patternInteger = Pattern.compile("^[0-9]*(?:\\.[0-9]*)?$");

        if (!textFieldName.getText().equals("") &&
                patternDouble.matcher(textFieldPrice.getText()).matches() &&
                patternInteger.matcher(textFieldVolume.getText()).matches()
        ) {
            Product newProduct = new Product(textFieldName.getText(),
                                            Double.parseDouble(textFieldPrice.getText()),
                                            Integer.parseInt(textFieldVolume.getText()),
                                            String.valueOf(obsListBrands.stream().filter(x -> x.getBrand().equals(comboBoxBrand.getValue())).findFirst().get().getId()),
                                            String.valueOf(obsListCategories.stream().filter(x -> x.getCategory().equals(comboBoxCategory.getValue())).findFirst().get().getId()));


            if (newProduct != null) {
                runSQLInsertAddProduct(conn, newProduct);
                initTableProducts();
                onButtonClearClick();
            }
        }
    }

    public void onTableProductsClicked() {
        Product selectProduct = tblViewProducts.getSelectionModel().getSelectedItem();
        if (selectProduct != null) {
            textFieldName.setText(selectProduct.getName());
            textFieldPrice.setText(String.valueOf(selectProduct.getPrice()));
            textFieldVolume.setText(String.valueOf(selectProduct.getVolume()));
            comboBoxBrand.getSelectionModel().select(obsListBrands.stream().filter(x -> x.getBrand().equals(selectProduct.getBrand())).findFirst().get().getId() - 1);
            comboBoxCategory.getSelectionModel().select(obsListCategories.stream().filter(x -> x.getCategory().equals(selectProduct.getCategory())).findFirst().get().getId() - 1);
        }
    }

    public void onMenuItemDeleteProductClick() {
        int selectProduct = tblViewProducts.getSelectionModel().getSelectedItem().getId();
        runSQLDeleteProduct(conn, selectProduct);
        initTableProducts();
    }

    public void onButtonClearClick() {
        for (Node el : paneAddProduct.getChildren()) {
            if (el instanceof TextField) {
                ((TextField) el).clear();
                el.setStyle(null);
            }
            if (el instanceof ComboBox<?>) {
                ((ComboBox<?>) el).getSelectionModel().clearAndSelect(0);
            }
        }
    }

    public void onButtonAddBrandClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/brands-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Brands");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/add_brand.png")));
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

    public void onButtonAddCategoryClick() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/categories-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Categories");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/add_category.png")));
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
