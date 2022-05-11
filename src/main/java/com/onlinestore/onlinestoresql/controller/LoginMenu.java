package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginMenu {

    @FXML
    Button btnLogin;
    @FXML
    PasswordField passFieldPassword;
    @FXML
    TextField textFieldUsername;

    public void initialize(){

    }
    public void onButtonLoginClick() {
        try {
            Stage thisStage = (Stage) btnLogin.getScene().getWindow();
            thisStage.close();

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("maket/orders-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Orders");
            stage.setMinWidth(1000);
            stage.setMinHeight(600);
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("img/logo.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
