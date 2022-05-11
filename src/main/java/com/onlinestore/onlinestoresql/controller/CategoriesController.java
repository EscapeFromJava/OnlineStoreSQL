package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.Category;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.Connection;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Delete.runSQLDeleteCategory;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Insert.runSQLInsertAddCategory;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectCategory;
import static com.onlinestore.onlinestoresql.model.requestsSQL.Update.runSQLUpdateCategory;

public class CategoriesController {
    @FXML
    TableView<Category> tblViewCategories;
    @FXML
    TextField textFieldNewCategory;
    Connection conn;
    ObservableList<Category> obsListCategory;
    public void initialize(){
        conn = MainApplication.conn;
        initTableCategories();
    }
    private void initTableCategories() {
        obsListCategory = runSQLSelectCategory(conn);

        TableColumn<Category, String> colName = new TableColumn<>("Category");

        tblViewCategories.getColumns().clear();
        tblViewCategories.getColumns().add(colName);
        tblViewCategories.setItems(obsListCategory);

        colName.setCellValueFactory(el -> el.getValue().categoryProperty());

        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            Category selectCategory = tblViewCategories.getSelectionModel().getSelectedItem();
            runSQLUpdateCategory(conn, event.getNewValue(), selectCategory.getId());
            initTableCategories();
        });

        tblViewCategories.getSortOrder().setAll(colName);
    }

    public void onButtonAddCategoryClick() {
        if (!textFieldNewCategory.getText().equals("")) {
            String newCategory = textFieldNewCategory.getText();
            runSQLInsertAddCategory(conn, newCategory);
            initTableCategories();
            textFieldNewCategory.clear();
        }
    }
    public void onMenuItemDeleteCategoryClick() {
        String selectCategory = tblViewCategories.getSelectionModel().getSelectedItem().getCategory();
        if (selectCategory != null) {
            runSQLDeleteCategory(conn, selectCategory);
            initTableCategories();
        }
    }
}
