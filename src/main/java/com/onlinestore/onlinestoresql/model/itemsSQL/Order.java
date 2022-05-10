package com.onlinestore.onlinestoresql.model.itemsSQL;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
    SimpleIntegerProperty order_id = new SimpleIntegerProperty();
    SimpleIntegerProperty client = new SimpleIntegerProperty();
    SimpleStringProperty product = new SimpleStringProperty();
    SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    SimpleDoubleProperty price = new SimpleDoubleProperty();
    SimpleStringProperty order_date = new SimpleStringProperty();
    SimpleStringProperty status = new SimpleStringProperty();

    public int getOrder_id() {
        return order_id.get();
    }

    public SimpleIntegerProperty order_idProperty() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id.set(order_id);
    }

    public int getClient() {
        return client.get();
    }

    public SimpleIntegerProperty clientProperty() {
        return client;
    }

    public void setClient(int client) {
        this.client.set(client);
    }

    public String getProduct() {
        return product.get();
    }

    public SimpleStringProperty productProperty() {
        return product;
    }

    public void setProduct(String product) {
        this.product.set(product);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
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

    public String getOrder_date() {
        return order_date.get();
    }

    public SimpleStringProperty order_dateProperty() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date.set(order_date);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public Order(int order_id, int client, String order_date, String status) {
        setOrder_id(order_id);
        setClient(client);
        setOrder_date(order_date);
        setStatus(status);
    }

    public Order(int order_id, String product, int quantity, double price) {
        setOrder_id(order_id);
        setProduct(product);
        setQuantity(quantity);
        setPrice(price);
    }

    public Order(int order_id, int client, String product, int quantity, double price, String order_date, String status) {
        setOrder_id(order_id);
        setClient(client);
        setProduct(product);
        setQuantity(quantity);
        setPrice(price);
        setOrder_date(order_date);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "Orders{" + order_id + " | " + client + " | " + product + " | " + quantity + " | " + price + " | " + order_date + " | " + status + '}';
    }
}
