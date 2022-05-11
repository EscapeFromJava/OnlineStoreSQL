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
    @FXML
    ComboBox comboBoxMonth, comboBoxYear;
    ObservableList<Order> obsListOrders;

    XYChart.Series<String, Number> dataSeries;
    Connection conn;

    public void initialize() {
        conn = MainApplication.conn;
        obsListOrders = runSQLSelectOrders(conn);
        initBarChartOrders();
        initComboBoxMonth();
        initComboBoxYear();
    }

    private void initComboBoxYear() {
        ObservableList<Order> obsListYears = FXCollections.observableArrayList(obsListOrders);
        obsListYears.stream().map(Order::getOrder_date).collect(Collectors.toList()).forEach(el -> el.substring(0, 4));

        comboBoxYear.setItems(obsListYears);
    }

    private void initComboBoxMonth() {
        Map<Integer, String> mapMonths = new HashMap<>();
        mapMonths.put(1, "January");
        mapMonths.put(2, "February");
        mapMonths.put(3, "March");
        mapMonths.put(4, "April");
        mapMonths.put(5, "May");
        mapMonths.put(6, "June");
        mapMonths.put(7, "July");
        mapMonths.put(8, "August");
        mapMonths.put(9, "September");
        mapMonths.put(10, "October");
        mapMonths.put(11, "November");
        mapMonths.put(12, "December");

        ObservableList<String> obsListMonths = FXCollections.observableArrayList();
        obsListMonths.addAll("January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December");

        comboBoxMonth.setItems(obsListMonths);
        comboBoxMonth.setOnAction(actionEvent -> {
            String selectMonth = String.valueOf(comboBoxMonth.getValue());
        });
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
