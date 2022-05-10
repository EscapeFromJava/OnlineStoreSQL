package com.onlinestore.onlinestoresql.model.itemsSQL;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Category {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty category = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public Category(int id, String category) {
        setId(id);
        setCategory(category);
    }

    @Override
    public String toString() {
        return getCategory();
    }
}
