package com.example.myrestaurant.Model;

import java.util.ArrayList;

/**
 * Created by vj on 11/25/17.
 */

public class Customer{
    private   String userName;
    private   String passWord;
    double    timeAtCurrentOrder;

    ArrayList<Integer> customerActiveOrder = new ArrayList<Integer>();

    /* Indicates the list of orders made by the customer (history of orders).
     * Does not include the current ongoing customer */
    ArrayList<int[]> customerCompletedOrderList = new ArrayList<int[]>();

    public Customer(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

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

    public double getTimeAtCurrentOrder() {
        return timeAtCurrentOrder;
    }

    public void setTimeAtCurrentOrder(double timeAtCurrentOrder) {
        this.timeAtCurrentOrder = timeAtCurrentOrder;
    }

    public ArrayList<Integer> getCustomerActiveOrder() {
        return customerActiveOrder;
    }

    public void setCustomerActiveOrder(ArrayList<Integer> customerActiveOrder) {
        this.customerActiveOrder = customerActiveOrder;
    }

    public ArrayList<int[]> getCustomerCompletedOrderList() {
        return customerCompletedOrderList;
    }

    public void setCustomerCompletedOrderList(ArrayList<int[]> customerCompletedOrderList) {
        this.customerCompletedOrderList = customerCompletedOrderList;
    }
}
