package com.onlinestore.onlinestoresql.model.itemsSQL;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty first_name = new SimpleStringProperty();
    SimpleStringProperty last_name = new SimpleStringProperty();
    SimpleStringProperty phone_number = new SimpleStringProperty();
    SimpleStringProperty city = new SimpleStringProperty();
    SimpleStringProperty district = new SimpleStringProperty();
    SimpleStringProperty street = new SimpleStringProperty();
    SimpleIntegerProperty house = new SimpleIntegerProperty();
    SimpleIntegerProperty apartment = new SimpleIntegerProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirst_name() {
        return first_name.get();
    }

    public SimpleStringProperty first_nameProperty() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }

    public String getLast_name() {
        return last_name.get();
    }

    public SimpleStringProperty last_nameProperty() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name.set(last_name);
    }

    public String getPhone_number() {
        return phone_number.get();
    }

    public SimpleStringProperty phone_numberProperty() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number.set(phone_number);
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getDistrict() {
        return district.get();
    }

    public SimpleStringProperty districtProperty() {
        return district;
    }

    public void setDistrict(String district) {
        this.district.set(district);
    }

    public String getStreet() {
        return street.get();
    }

    public SimpleStringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getHouse() {
        return house.get();
    }

    public SimpleIntegerProperty houseProperty() {
        return house;
    }

    public void setHouse(int house) {
        this.house.set(house);
    }

    public int getApartment() {
        return apartment.get();
    }

    public SimpleIntegerProperty apartmentProperty() {
        return apartment;
    }

    public void setApartment(int apartment) {
        this.apartment.set(apartment);
    }

    public Client(String first_name, String last_name, String phone_number, String city, String district, String street, int house, int apartment) {
        setFirst_name(first_name);
        setLast_name(last_name);
        setPhone_number(phone_number);
        setCity(city);
        setDistrict(district);
        setStreet(street);
        setHouse(house);
        setApartment(apartment);
    }

    public Client(int id, String first_name, String last_name, String phone_number, String city, String district, String street, int house, int apartment) {
        setId(id);
        setFirst_name(first_name);
        setLast_name(last_name);
        setPhone_number(phone_number);
        setCity(city);
        setDistrict(district);
        setStreet(street);
        setHouse(house);
        setApartment(apartment);
    }

    @Override
    public String toString() {
        return "Client{" + getId() +
                ", " + getFirst_name() +
                ", " + getLast_name() +
                ", " + getPhone_number() +
                ", " + getCity() +
                ", " + getDistrict() +
                ", " + getStreet() +
                ", " + getHouse() +
                ", " + getApartment() +
                '}';
    }
}
