package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.Brand;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.Connection;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteBrand;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddBrand;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectBrand;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Update.runSQLUpdateBrand;

public class BrandsController {
    @FXML
    TableView<Brand> tblViewBrands;
    @FXML
    TextField textFieldNewBrand;
    Connection conn;

    ObservableList<Brand> obsListBrand;

    public void initialize(){
        conn = MainApplication.conn;
        initTableBrands();
    }

    private void initTableBrands() {
        obsListBrand = runSQLSelectBrand(conn);

        TableColumn<Brand, String> colName = new TableColumn<>("Brand");

        tblViewBrands.getColumns().clear();
        tblViewBrands.getColumns().add(colName);
        tblViewBrands.setItems(obsListBrand);

        colName.setCellValueFactory(el -> el.getValue().brandProperty());

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            Brand selectBrand = tblViewBrands.getSelectionModel().getSelectedItem();
            runSQLUpdateBrand(conn, event.getNewValue(), selectBrand.getId());
            initTableBrands();
        });

        tblViewBrands.getSortOrder().setAll(colName);
    }

    public void onButtonAddBrandClick() {
        if (!textFieldNewBrand.getText().equals("")) {
            String newBrand = textFieldNewBrand.getText();
            runSQLInsertAddBrand(conn, newBrand);
            initTableBrands();
            textFieldNewBrand.clear();
        }
    }
    public void onMenuItemDeleteBrandClick() {
        String selectBrand = tblViewBrands.getSelectionModel().getSelectedItem().getBrand();
        if (selectBrand != null) {
            runSQLDeleteBrand(conn, selectBrand);
            initTableBrands();
        }
    }
}
