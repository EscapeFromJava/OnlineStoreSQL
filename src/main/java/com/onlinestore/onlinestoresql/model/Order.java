package com.onlinestore.onlinestoresql.model;

import javafx.beans.property.SimpleStringProperty;

public class Order {
    SimpleStringProperty order_id = new SimpleStringProperty();
    SimpleStringProperty client_fio = new SimpleStringProperty();
    SimpleStringProperty product_name = new SimpleStringProperty();
    SimpleStringProperty order_date = new SimpleStringProperty();
    SimpleStringProperty status = new SimpleStringProperty();

    public String getOrder_id() {
        return order_id.get();
    }

    public SimpleStringProperty order_idProperty() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id.set(order_id);
    }

    public String getClient_fio() {
        return client_fio.get();
    }

    public SimpleStringProperty client_fioProperty() {
        return client_fio;
    }

    public void setClient_fio(String client_fio) {
        this.client_fio.set(client_fio);
    }

    public String getProduct_name() {
        return product_name.get();
    }

    public SimpleStringProperty product_nameProperty() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name.set(product_name);
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

    public Order(String order_id, String client_fio, String product_name, String order_date, String status) {
        setOrder_id(order_id);
        setClient_fio(client_fio);
        setProduct_name(product_name);
        setOrder_date(order_date);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "Orders{" + order_id + " | " + client_fio + " | " + product_name + " | " + order_date + " | " + status + '}';
    }
}
