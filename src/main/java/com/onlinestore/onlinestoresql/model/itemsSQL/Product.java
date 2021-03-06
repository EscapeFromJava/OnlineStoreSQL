package com.onlinestore.onlinestoresql.model.itemsSQL;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty name = new SimpleStringProperty();
    SimpleDoubleProperty price = new SimpleDoubleProperty();
    SimpleIntegerProperty volume = new SimpleIntegerProperty();
    SimpleStringProperty brand = new SimpleStringProperty();
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getVolume() {
        return volume.get();
    }

    public SimpleIntegerProperty volumeProperty() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume.set(volume);
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

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public Product(String name, double price, int volume, String brand, String category) {
        setName(name);
        setPrice(price);
        setVolume(volume);
        setBrand(brand);
        setCategory(category);
    }

    public Product(int id, String name, double price, int volume, String brand, String category) {
        setId(id);
        setName(name);
        setPrice(price);
        setVolume(volume);
        setBrand(brand);
        setCategory(category);
    }

    @Override
    public String toString() {
        return "Product{" + id + " | " + name + " | " + price + " | " + volume + " | " + brand + " | " + category + " | " + '}';
    }
}
