package com.example.myrestaurant.Model;

import java.util.ArrayList;

/**
 * Created by 站站 on 2017/11/21.
 */

public class Customer {
    private String customerID;
    private String customerPass;
    private ArrayList<Order> orders;

    public Customer() {
        this.orders = new ArrayList<>();
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerPass() {
        return customerPass;
    }

    public void setCustomerPass(String customerPass) {
        this.customerPass = customerPass;
    }
    public void addOrder(Order order) {
       orders.add(order);
    }
}
