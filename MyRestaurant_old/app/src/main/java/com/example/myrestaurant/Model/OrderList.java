package com.example.myrestaurant.Model;

/**
 * Created by vj on 11/25/17.
 */
import java.util.*;

public class OrderList {

    // In this class actionTodo = Orderplacement
    int actionTodo;
    float orderPlacedTime;
    String customerId;

    /* By default we shall assume that
    orderItemQuantity[0] - Indicates quantity for Burger.
    orderItemQuantity[1] - Indicates quantity for Chicken.
    orderItemQuantity[2] - Indicates quantity for FrenchFries.
    orderItemQuantity[3] - Indicates quantity for OnionRings. */
    ArrayList<Integer> orderItemQuantity = new ArrayList<Integer>();

    public int getActionTodo() {
        return actionTodo;
    }

    public float getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ArrayList<Integer> getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setActionTodo(int actionTodo) {
        this.actionTodo = actionTodo;
    }

    public void setOrderPlacedTime(float orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setOrderItemQuantity(ArrayList<Integer> orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }
}
