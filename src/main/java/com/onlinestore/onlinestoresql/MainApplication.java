package com.onlinestore.onlinestoresql;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApplication extends Application {

    public static Connection conn;
    @Override
    public void start(Stage stage) {
        connectingSQL();
        showLogin();
    }

    public static void showLogin(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/login-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Login");
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/logo.png")));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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