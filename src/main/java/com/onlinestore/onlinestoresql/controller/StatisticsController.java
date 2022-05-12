package com.onlinestore.onlinestoresql.controller;

import com.onlinestore.onlinestoresql.MainApplication;
import com.onlinestore.onlinestoresql.model.itemsSQL.Client;
import com.onlinestore.onlinestoresql.model.itemsSQL.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.sql.Connection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.onlinestore.onlinestoresql.model.requestsSQL.Select.runSQLSelectOrders;

public class StatisticsController {
    @FXML
    BarChart barChartOrders;
    ObservableList<Order> obsListOrders;
    XYChart.Series<String, Number> dataSeries;
    Connection conn;

    public void initialize() {
        conn = MainApplication.conn;
        obsListOrders = runSQLSelectOrders(conn);
        initBarChartOrders();
    }

    private void initBarChartOrders() {
        if (dataSeries != null) {
            barChartOrders.getData().clear();
        }
        ObservableList<Order> osbListData = FXCollections.observableArrayList(obsListOrders);
        osbListData.forEach(x -> x.setOrder_date(x.getOrder_date().substring(0, 10)));

        dataSeries = new XYChart.Series<>();
        dataSeries.setName("Orders");

        osbListData.sort(Comparator.comparing(Order::getOrder_date));

        for (Order order : osbListData) {
            int count = (int) osbListData.stream().filter(n -> n.getOrder_date().equals(order.getOrder_date())).count();
            dataSeries.getData().add(new XYChart.Data<>(order.getOrder_date(), count));
        }

        barChartOrders.getData().add(dataSeries);
    }
}
