package com.onlinestore.onlinestoresql.model.requestsSQL;

import com.onlinestore.onlinestoresql.model.itemsSQL.*;
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
            String request = "SELECT * FROM products_table";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListProducts.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("volume"),
                        rs.getString("brand"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListProducts;
    }

    public static ObservableList<Product> runSQLSelectProductsByCategory(Connection conn, int idCategory) {
        ObservableList<Product> obsListProducts = FXCollections.observableArrayList();
        try {
            String request = "SELECT * " +
                             "FROM products_table " +
                             "WHERE category = " +
                                 "(SELECT category FROM category WHERE id = " + idCategory + ");";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListProducts.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("volume"),
                        rs.getString("brand"),
                        rs.getString("category")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListProducts;
    }

    public static ObservableList<Status> runSQLSelectStatus(Connection conn) {
        ObservableList<Status> obsListStatus = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM status";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListStatus.add(new Status(rs.getInt("id"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListStatus;
    }

    public static ObservableList<Brand> runSQLSelectBrand(Connection conn) {
        ObservableList<Brand> obsListBrand = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM brand";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListBrand.add(new Brand(rs.getInt("id"),
                                            rs.getString("brand")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListBrand;
    }

    public static ObservableList<Category> runSQLSelectCategory(Connection conn) {
        ObservableList<Category> obsListCategories = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM category";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListCategories.add(new Category(rs.getInt("id"),
                                                    rs.getString("category")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListCategories;
    }

    public static ObservableList<City> runSQLSelectCity(Connection conn) {
        ObservableList<City> obsListCity = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM city";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListCity.add(new City(rs.getInt("id"),
                        rs.getString("name")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListCity;
    }

    public static ObservableList<Order> runSQLSelectOrders(Connection conn, int idClient) {
        ObservableList<Order> obsListOrders = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM client_orders WHERE client = " + idClient + ";";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListOrders.add(new Order(rs.getInt("order_id"),
                                            rs.getInt("client"),
                                            rs.getString("product"),
                                            rs.getInt("quantity"),
                                            rs.getDouble("price"),
                                            rs.getString("order_date"),
                                            rs.getString("status")));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return obsListOrders;
    }
}
