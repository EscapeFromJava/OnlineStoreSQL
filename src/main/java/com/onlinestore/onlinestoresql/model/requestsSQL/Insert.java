package com.onlinestore.onlinestoresql.model.requestsSQL;

import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
    public static void runSQLInsertMakeOrder(Connection conn, int id_client, int id_product) {
        try {
            String request = "INSERT INTO orders (id_client, id_product, order_date, status) " +
                    "VALUES (" + id_client + ", " + id_product + ", date_trunc('second', timestamp 'now'), 1);";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddCity(Connection conn, String newCity) {
        try {
            String request = "INSERT INTO city (name) VALUES ('" + newCity + "');";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddBrand(Connection conn, String newBrand) {
        try {
            String request = "INSERT INTO brand (brand) VALUES ('" + newBrand + "');";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddCategory(Connection conn, String newCategory) {
        try {
            String request = "INSERT INTO category (category) VALUES ('" + newCategory + "');";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddClient(Connection conn, Client newClient){
        try {
            String request = "INSERT INTO client(first_name, last_name, phone_number, district, street, house, apartment, city) " +
                    "VALUES ('" + newClient.getFirst_name() + "', '" +
                                newClient.getLast_name() + "', '" +
                                newClient.getPhone_number() + "', '" +
                                newClient.getDistrict() + "', '" +
                                newClient.getStreet() + "', " +
                                newClient.getHouse() + ", " +
                                newClient.getApartment() + ", " +
                                newClient.getCity() + ");";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddProduct(Connection conn, Product newProduct){
        try {
            String request = "INSERT INTO product (name, price, volume, brand, category) " +
                    "VALUES ('" + newProduct.getName() + "', " + newProduct.getPrice() + ", " + newProduct.getVolume() + ", " +
                    "(SELECT id FROM brand WHERE id = " + newProduct.getBrand() + "), " +
                    "(SELECT id FROM category WHERE id = " + newProduct.getCategory() + "));";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }
}
