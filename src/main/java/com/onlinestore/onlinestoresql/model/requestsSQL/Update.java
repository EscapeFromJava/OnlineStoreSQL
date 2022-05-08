package com.onlinestore.onlinestoresql.model.requestsSQL;

import com.onlinestore.onlinestoresql.model.itemsSQL.Client;

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

    public static void runSQLUpdateClient(Connection conn, Client editedClient) {
        try {
            String request = "UPDATE client SET first_name = '" + editedClient.getFirst_name() + "', " +
                    "last_name = '" + editedClient.getLast_name() + "', " +
                    "phone_number = '" + editedClient.getPhone_number() + "', " +
                    "district = '" + editedClient.getDistrict() + "', " +
                    "street = '" + editedClient.getStreet() + "', " +
                    "house = " + editedClient.getHouse() + ", " +
                    "apartment = " + editedClient.getApartment() + ", " +
                    "city = (SELECT id FROM city WHERE name = '" + editedClient.getCity() +"') " +
                    "WHERE id = " + editedClient.getId();
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }

    public static void runSQLUpdateClientFirstName(Connection conn, String newFirstName, int idClient){
        try {
            String request = "UPDATE client SET first_name = '" + newFirstName + "' WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
}
