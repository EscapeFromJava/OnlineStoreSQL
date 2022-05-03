package com.onlinestore.onlinestoresql.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty fio = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFio() {
        return fio.get();
    }

    public SimpleStringProperty fioProperty() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public Client(int id, String fio) {
        setId(id);
        setFio(fio);
    }

    @Override
    public String toString() {
        return "Client{" + id + " | " + fio + '}';
    }
}
