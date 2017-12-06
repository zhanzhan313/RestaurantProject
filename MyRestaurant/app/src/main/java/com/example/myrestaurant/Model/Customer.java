package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vj on 11/25/17.
 */

public class Customer implements Parcelable{
    private   String userName;
    private   String passWord;
    private OrderList orderList;

    public Customer()
    {
        orderList=new OrderList();
    }
    public OrderList getOrderList() {
        return orderList;
    }

    public void setOrderList(OrderList orderList) {
        this.orderList = orderList;
    }


    public Customer(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            Customer customer=new Customer();
            customer.setUserName(in.readString());
            customer.setPassWord(in.readString());
            customer.setOrderList(in.<OrderList>readParcelable(OrderList.class.getClassLoader()));
            return customer;
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(userName);
        dest.writeString(passWord);
        dest.writeParcelable(orderList,flags);
    }
}
