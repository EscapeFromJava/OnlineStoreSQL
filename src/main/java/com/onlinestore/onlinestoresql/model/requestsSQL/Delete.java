package com.onlinestore.onlinestoresql.model.requestsSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Delete {
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
}
