package com.onlinestore.onlinestoresql.model.requestsSQL;

import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Order;
import com.onlinestore.onlinestoresql.model.itemsSQL.Product;
import com.onlinestore.onlinestoresql.model.itemsSQL.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Select {
    public static ObservableList<Client> runSQLSelectClients(Connection conn) {
        ObservableList<Client> obsListClients = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM clients";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListClients.add(new Client(rs.getInt("id"),
                                        rs.getString("first_name"),
                                        rs.getString("last_name"),
                                        rs.getString("phone_number"),
                                        rs.getString("city"),
                                        rs.getString("district"),
                                        rs.getString("street"),
                                        rs.getInt("house"),
                                        rs.getInt("apartment")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListClients;
    }

    public static ObservableList<Product> runSQLSelectProducts(Connection conn) {
        ObservableList<Product> obsListProducts = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM \"Product\"";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListProducts.add(new Product(rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getDouble("Price"),
                        rs.getInt("Volume"),
                        rs.getInt("Brand"),
                        rs.getInt("Category")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListProducts;
    }

    public static ObservableList<Status> runSQLSelectStatus(Connection conn) {
        ObservableList<Status> obsListStatus = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM \"Status\"";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListStatus.add(new Status(rs.getInt("ID"),
                        rs.getString("Status")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListStatus;
    }

    public static ObservableList<Order> runSQLSelectOrders(Connection conn) {
        ObservableList<Order> obsListOrders = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM Orders_table";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListOrders.add(new Order(rs.getInt("Order_ID"),
                        rs.getString("Client_FIO"),
                        rs.getString("Product_Name"),
                        rs.getString("Order_date"),
                        rs.getString("Status")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListOrders;
    }
}
