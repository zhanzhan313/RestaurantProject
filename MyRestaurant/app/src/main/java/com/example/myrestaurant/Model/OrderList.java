package com.example.myrestaurant.Model;

/**
 * Created by vj on 11/25/17.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class OrderList implements Parcelable{

    private ArrayList<Order> orderArrayList;

    public OrderList() {
        this.orderArrayList = new ArrayList<>();
    }


    public ArrayList<Order> getOrderArrayList() {
        return orderArrayList;
    }

    public void setOrderArrayList(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(orderArrayList);

    }
    public static final Creator<OrderList> CREATOR = new Creator<OrderList>() {
        @Override
        public OrderList createFromParcel(Parcel in) {
            OrderList orderList=new OrderList();
            orderList.setOrderArrayList(in.readArrayList(null));
            return orderList;
        }

        @Override
        public OrderList[] newArray(int size) {
            return new OrderList[size];
        }
    };
}
