package com.example.vj.foodorderserver;

import java.util.*;

public class Order {

    // In this class actionTodo = Orderplacement
    int actionTodo;
    float orderPlacedTime;
    String userName;

    /*
    orderItemQuantity[0] - Indicates quantity for Burger.
    orderItemQuantity[1] - Indicates quantity for Chicken.
    orderItemQuantity[2] - Indicates quantity for FrenchFries.
    orderItemQuantity[3] - Indicates quantity for OnionRings.
    */
    ArrayList<Integer> orderItemQuantity = new ArrayList<Integer>();

    public int getActionTodo() {
        return actionTodo;
    }

    public String getUserName() {
        return userName;
    }

    public float getOrderPlacedTime() {
        return orderPlacedTime;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrderItemQuantity(ArrayList<Integer> orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }
}
