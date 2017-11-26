package com.example.vj.foodorderserver;

import java.util.*;

/**
 * Created by vj on 11/25/17.
 */

public class Customer {
    String userName;
    String passWord;
    double timeAtCurrentOrder;
    /* Indicates the current ongoing order of the customer */
    ArrayList<Integer> customerActiveOrder = new ArrayList<Integer>();
    /* Indicates the list of orders made by the customer (history of orders).
     * Does not include the current ongoing customer */
    ArrayList<ArrayList<Integer>> customerCompletedOrderList = new ArrayList<ArrayList<Integer>>();

    public Customer(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }


}
