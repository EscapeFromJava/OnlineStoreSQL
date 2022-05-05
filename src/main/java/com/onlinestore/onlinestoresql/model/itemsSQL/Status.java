package com.onlinestore.onlinestoresql.model.itemsSQL;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Status {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty status = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public Status(int id, String status) {
        setId(id);
        setStatus(status);
    }

    @Override
    public String toString() {
        return "Status{" + id + " | " + status + '}';
    }
}
