package com.onlinestore.onlinestoresql.model.requestsSQL;

import com.onlinestore.onlinestoresql.model.itemsSQL.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateClient {
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
    public static void runSQLUpdateClientLastName(Connection conn, String newLastName, int idClient){
        try {
            String request = "UPDATE client SET last_name = '" + newLastName + "' WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateClientPhoneNumber(Connection conn, String newPhoneNumber, int idClient){
        try {
            String request = "UPDATE client SET phone_number = '" + newPhoneNumber + "' WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }

    public static void runSQLUpdateClientCity(Connection conn, String newCity, int idClient){
        try {
            String request = "UPDATE client SET city = (SELECT id FROM city WHERE name = '" + newCity + "') WHERE id = " + idClient + ";";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateClientDistrict(Connection conn, String newDistrict, int idClient){
        try {
            String request = "UPDATE client SET district = '" + newDistrict + "' WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateClientStreet(Connection conn, String newStreet, int idClient){
        try {
            String request = "UPDATE client SET street = '" + newStreet + "' WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateClientHouse(Connection conn, int newHouse, int idClient){
        try {
            String request = "UPDATE client SET house = " + newHouse + " WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
    public static void runSQLUpdateClientApartment(Connection conn, int newApartment, int idClient){
        try {
            String request = "UPDATE client SET apartment = " + newApartment + " WHERE id = " + idClient;
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("update ERROR: " + e.getMessage());
        }
    }
}
