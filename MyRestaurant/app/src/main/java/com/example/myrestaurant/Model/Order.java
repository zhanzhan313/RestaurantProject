package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myrestaurant.Controller.SignupLogin;

import java.util.*;

public class Order implements Parcelable {
    // In this class actionTodo = Orderplacement
    private String actionTodo;
    private String orderPlacedTime;
    private String userName;
    private static int orderID=0;
    /*
   orderItemQuantity[0] - Indicates quantity for Burger.
   orderItemQuantity[1] - Indicates quantity for Chicken.
   orderItemQuantity[2] - Indicates quantity for FrenchFries.
   orderItemQuantity[3] - Indicates quantity for OnionRings.
   */
    private int[] orderItemQuantity = new int[4];
    private double orderTotal;

    public Order() {
        orderID++;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actionTodo);
        dest.writeString(orderPlacedTime);
        dest.writeString(userName);
        dest.writeInt(orderID);
        dest.writeIntArray(orderItemQuantity);
        dest.writeDouble(orderTotal);
    }

    public static int getOrderID() {
        return orderID;
    }

    public static void setOrderID(int orderID) {
        Order.orderID = orderID;
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

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            Order order=new Order();
            order.setActionTodo(source.readString());
            order.setOrderPlacedTime(source.readString());
            order.setUserName(source.readString());
            order.setOrderID(source.readInt());
//            order.setOrderItemQuantity(source.readIntArray());
            order.setOrderTotal(source.readFloat());
            return order;
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[0];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }


    public String getActionTodo() {
        return actionTodo;
    }

    public String getUserName() {
        return userName;
    }


    public void setActionTodo(String actionTodo) {
        this.actionTodo = actionTodo;
    }

    public String getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public void setOrderPlacedTime(String orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int[] getOrderItemQuantity() {
        return orderItemQuantity;
    }

}
