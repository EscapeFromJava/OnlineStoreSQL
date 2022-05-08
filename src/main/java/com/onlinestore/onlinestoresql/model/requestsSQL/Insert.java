package com.onlinestore.onlinestoresql.model.requestsSQL;

import com.onlinestore.onlinestoresql.model.itemsSQL.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {
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
}
