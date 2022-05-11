package com.onlinestore.onlinestoresql.model.requestsSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {

    public static void runSQLUpdateBrand(Connection conn, String newBrand, int id) {
        try {
            String request = "UPDATE brand SET brand = '" + newBrand + "' WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }

    public static void runSQLUpdateCategory(Connection conn, String newCategory, int id) {
        try {
            String request = "UPDATE category SET category = '" + newCategory + "' WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }

    public static void runSQLUpdateCity(Connection conn, String newName, int id) {
        try {
            String request = "UPDATE city SET name = '" + newName + "' WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }

    public static void runSQLUpdateStatus(Connection conn, int status, int id) {
        try {
            String request = "UPDATE orders SET status = " + status + " WHERE id = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
}
