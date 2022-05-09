package com.onlinestore.onlinestoresql.model.requestsSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateProduct {
    public static void runSQLUpdateProductName(Connection conn, String newName, int id) {
        try {
            String request = "UPDATE product SET name = '" + newName + "' WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateProductPrice(Connection conn, double newPrice, int id) {
        try {
            String request = "UPDATE product SET price = " + newPrice + " WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateProductVolume(Connection conn, int newVolume, int id) {
        try {
            String request = "UPDATE product SET volume = " + newVolume + " WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }

    public static void runSQLUpdateProductBrand(Connection conn, String newBrand, int id) {
        try {
            String request = "UPDATE product SET brand = (SELECT id FROM brand WHERE brand = '" + newBrand + "') WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateProductCategory(Connection conn, String newCategory, int id) {
        try {
            String request = "UPDATE product SET category = (SELECT id FROM category WHERE category = '" + newCategory + "') WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
}
