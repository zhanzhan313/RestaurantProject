package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    // In this class actionTodo = Orderplacement
    private String actionTodo;
    private double orderPlacedTime;
    private String userName;
    private static int orderID = 0;

    private int[] orderItemQuantity = new int[4];
    private double orderTotal;

    public Order(String userName, int[] orderItemQuantity) {
        this.userName = userName;
        this.orderItemQuantity = orderItemQuantity;
    }

    protected Order(Parcel in) {
        actionTodo = in.readString();
        orderPlacedTime = in.readDouble();
        userName = in.readString();
        orderItemQuantity = in.createIntArray();
        orderTotal = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(actionTodo);
        parcel.writeDouble(orderPlacedTime);
        parcel.writeString(userName);
        parcel.writeIntArray(orderItemQuantity);
        parcel.writeDouble(orderTotal);
    }

    public String getActionTodo() {
        return actionTodo;
    }

    public void setActionTodo(String actionTodo) {
        this.actionTodo = actionTodo;
    }

    public double getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public void setOrderPlacedTime(double orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static int getOrderID() {
        return orderID;
    }

    public static void setOrderID(int orderID) {
        Order.orderID = orderID;
    }

    public int[] getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(int[] orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}