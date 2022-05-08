package com.onlinestore.onlinestoresql;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApplication extends Application {

    public static Connection conn;
    @Override
    public void start(Stage stage) throws IOException {
        connectingSQL();

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Online Store");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();
    }

    private void connectingSQL() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/OnlineStore", "postgres", "123");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}