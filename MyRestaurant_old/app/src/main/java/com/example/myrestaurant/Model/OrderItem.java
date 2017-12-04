package com.example.myrestaurant.Model;

/**
 * Created by 站站 on 2017/11/21.
 */

public class OrderItem {
    private String name;
    private int cost;
    private int quantity;
    private int totalCost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalCost() {
        return quantity*cost;
    }


}
