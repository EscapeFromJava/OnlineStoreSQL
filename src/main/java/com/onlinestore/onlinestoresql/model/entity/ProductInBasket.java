package com.onlinestore.onlinestoresql.model.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductInBasket {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty brand = new SimpleStringProperty();
    SimpleStringProperty name = new SimpleStringProperty();
    SimpleDoubleProperty price = new SimpleDoubleProperty();
    SimpleIntegerProperty volume = new SimpleIntegerProperty();
    SimpleDoubleProperty totalCost = new SimpleDoubleProperty();

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

    public double getTotalCost() {
        return getPrice() * getVolume();
    }

    public SimpleDoubleProperty totalCostProperty() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost.set(totalCost);
    }

    public ProductInBasket(int id, String brand, String name, double price) {
        setId(id);
        setBrand(brand);
        setName(name);
        setPrice(price);
    }

    @Override
    public String toString() {
        return "ProductInBasket{" +
                "id=" + id +
                ", brand=" + brand +
                ", name=" + name +
                ", price=" + price +
                ", volume=" + volume +
                ", totalCost=" + totalCost +
                '}';
    }
}
