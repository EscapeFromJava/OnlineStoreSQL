package com.onlinestore.onlinestoresql.model;

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

public class SQLrequest {
    public static ObservableList<Client> runSQLSelectClients(Connection conn) {
        ObservableList<Client> obsListClients = FXCollections.observableArrayList();
        try {
            String request = "SELECT * FROM \"Client\"";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                obsListClients.add(new Client(rs.getInt("ID"),
                        rs.getString("FIO")));
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

    public static void runSQLInsertMakeOrder(Connection conn, int id_client, int id_product) {
        try {
            String request = "INSERT INTO \"Order\"(\"ID_client\", \"ID_product\") " +
                    "VALUES (" + id_client + ", " + id_product + " );";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddClient(Connection conn, String fio){
        try {
            String request = "INSERT INTO \"Client\"(\"FIO\") " +
                    "VALUES ('" + fio + "');";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }

    public static void runSQLInsertAddProduct(Connection conn, String product, double price, int volume){
        try {
            String request = "INSERT INTO \"Product\"(\"Name\", \"Price\", \"Volume\") " +
                    "VALUES ('" + product + "', " + price + ", " + volume + ");";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("insert ERROR: " + e.getMessage());
        }
    }
    public static void runSQLDeleteClient(Connection conn, int id){
        try {
            String request = "DELETE FROM \"Order\" WHERE \"ID_client\" = " + id + "; " +
                             "DELETE FROM \"Client\" WHERE \"ID\" = " + id + ";";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }
    public static void runSQLDeleteProduct(Connection conn, int id){
        try {
            String request = "DELETE FROM \"Order\" WHERE \"ID_product\" = " + id + "; " +
                    "DELETE FROM \"Product\" WHERE \"ID\" = " + id + ";";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }

    public static void runSQLDeleteOrder(Connection conn, int orderNumber) {
        try {
            String request = "DELETE FROM \"Order\" WHERE \"Order\".\"ID\" = " + orderNumber + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateDate(Connection conn, String date, int id) {
        try {
            String request = "UPDATE \"Order\" SET \"Order_date\" = '" + date + "' WHERE \"ID\" = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateStatus(Connection conn, int status, int id) {
        try {
            String request = "UPDATE \"Order\" SET \"Status\" = " + status + " WHERE \"ID\" = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
}