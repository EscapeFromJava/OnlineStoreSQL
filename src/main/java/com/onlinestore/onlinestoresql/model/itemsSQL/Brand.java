package com.onlinestore.onlinestoresql.model.itemsSQL;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Brand {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty brand = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public Brand(int id, String brand) {
        setId(id);
        setBrand(brand);
    }

    @Override
    public String toString() {
        return "Brand{" + id + " " + brand + "}";
    }
}
