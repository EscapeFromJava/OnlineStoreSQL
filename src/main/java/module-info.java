module com.onlinestore.onlinestoresql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.onlinestore.onlinestoresql to javafx.fxml;
    exports com.onlinestore.onlinestoresql;
    exports com.onlinestore.onlinestoresql.controller;
    opens com.onlinestore.onlinestoresql.controller to javafx.fxml;
}