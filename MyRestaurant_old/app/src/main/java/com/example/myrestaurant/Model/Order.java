package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Order implements Parcelable {
    // In this class actionTodo = Orderplacement
    private String actionTodo;
    private double orderPlacedTime;
    private String userName;
    private static int orderID = 0;

    //private int[] orderItemQuantity = new int[4];
    private ArrayList<Integer>  orderItemQuantity = new ArrayList<Integer>();
    private double orderTotal;

    public Order(String userName, ArrayList<Integer> orderItemQuantity) {
        this.userName = userName;
        this.orderItemQuantity = orderItemQuantity;
    }

    protected Order(Parcel in) {
        actionTodo = in.readString();
        orderPlacedTime = in.readDouble();
        userName = in.readString();
        orderTotal = in.readDouble();
        orderItemQuantity = in.readArrayList(null);
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
        parcel.writeDouble(orderTotal);
        parcel.writeList(orderItemQuantity);
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

    public ArrayList<Integer> getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(ArrayList<Integer> orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}