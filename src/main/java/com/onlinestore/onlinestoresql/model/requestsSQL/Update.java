package com.onlinestore.onlinestoresql.model.requestsSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
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
    public static void runSQLUpdatePrice(Connection conn, double newPrice, int id) {
        try {
            String request = "UPDATE \"Product\" SET \"Price\" = " + newPrice + " WHERE \"ID\" = " + id + "; ";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateVolume(Connection conn, int newVolume, int id) {
        try {
            String request = "UPDATE \"Product\" SET \"Volume\" = " + newVolume + " WHERE \"ID\" = " + id + "; ";
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
}
