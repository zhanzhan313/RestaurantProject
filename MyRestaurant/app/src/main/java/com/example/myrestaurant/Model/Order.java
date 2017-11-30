package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myrestaurant.Controller.SignupLogin;

import java.util.*;

public class Order implements Parcelable {

    // In this class actionTodo = Orderplacement
    String actionTodo;
    Date orderPlacedTime;
    String userName;
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actionTodo);
        dest.writeString(orderPlacedTime.toString());
        dest.writeString(userName);
    }
    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return null;
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
    /*
    orderItemQuantity[0] - Indicates quantity for Burger.
    orderItemQuantity[1] - Indicates quantity for Chicken.
    orderItemQuantity[2] - Indicates quantity for FrenchFries.
    orderItemQuantity[3] - Indicates quantity for OnionRings.
    */
    ArrayList<Integer> orderItemQuantity = new ArrayList<Integer>();

    public String getActionTodo() {
        return actionTodo;
    }

    public String getUserName() {
        return userName;
    }



    public ArrayList<Integer> getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setActionTodo(String actionTodo) {
        this.actionTodo = actionTodo;
    }

    public Date getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public void setOrderPlacedTime(Date orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrderItemQuantity(ArrayList<Integer> orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }
}
