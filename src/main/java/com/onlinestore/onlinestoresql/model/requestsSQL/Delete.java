package com.onlinestore.onlinestoresql.model.requestsSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
    public static void runSQLDeleteClient(Connection conn, int id){
        try {
            String request = "DELETE FROM orders WHERE id_client = " + id + "; " +
                    "DELETE FROM client WHERE id = " + id + ";";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }

    public static void runSQLDeleteCity(Connection conn, String deletedCity){
        try {
            String request = "DELETE FROM city WHERE name = '" + deletedCity + "';";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }

    public static void runSQLDeleteBrand(Connection conn, String deletedBrand){
        try {
            String request = "DELETE FROM brand WHERE brand = '" + deletedBrand + "';";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }

    public static void runSQLDeleteCategory(Connection conn, String deletedCategory){
        try {
            String request = "DELETE FROM category WHERE category = '" + deletedCategory + "';";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }
    public static void runSQLDeleteProduct(Connection conn, int id){
        try {
            String request = "DELETE FROM product WHERE id = " + id + ";";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }

    public static void runSQLDeleteOrder(Connection conn, int orderNumber) {
        try {
            String request = "DELETE FROM orders WHERE orders.id = " + orderNumber + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("delete ERROR: " + e.getMessage());
        }
    }
}
