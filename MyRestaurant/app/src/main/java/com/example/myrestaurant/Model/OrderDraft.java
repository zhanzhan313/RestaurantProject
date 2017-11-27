package com.example.myrestaurant.Model;

import java.util.ArrayList;

/**
 * Created by 站站 on 2017/11/21.
 */

public class OrderDraft {
    private static int orderID=0;
    private ArrayList<OrderItem> orderItems;
    private int orderTotal;

    public OrderDraft(ArrayList<OrderItem> orderItems) {
        this.orderItems = new ArrayList<>();
        orderID++;
    }

    public static int getOrderID() {
        return orderID;
    }

//    public static void setOrderID(int orderID) {
//        Order.orderID = orderID;
//    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }
    public void deleteOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
    }
}
