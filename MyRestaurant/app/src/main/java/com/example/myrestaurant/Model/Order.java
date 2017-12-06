package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;

import com.example.myrestaurant.Controller.MenuActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Order implements Parcelable,Runnable{
    private String status;
    private String orderPlacedTime;
    private String userName;
    private ArrayList<Integer>  orderItemQuantity;
    private double orderTotal;
    public static  int orderIdCount=0;
    private  int orderId;

    public enum OrderStatus{
        Submitted("Submitted"),
        Receiving("Received"),
        Preparing("Preparing"),
        Packaging("Packaging"),
        FoodReady("Food Ready"),
        NotAvailable("Not Available"),
        PartiallyAvailable("Partially Available"),
        PartiallyAvailableAccepted("Partially Available Accepted"),
        PartiallyAvailableCancelled("Partially Available Cancelled");



        private String value;
        private OrderStatus(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
//public Order( ArrayList<Integer> orderItemQuantity) {
//    this.orderItemQuantity = orderItemQuantity;
//}
public Order()
{   orderPlacedTime=getStringToday();
    orderIdCount++;
    orderId=orderIdCount;
    orderItemQuantity=new ArrayList<>();
    for(int i=0;i<4;i++)
    {
        orderItemQuantity.add(0);
    }

}
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
//    protected Order(Parcel in) {
//        status=in.readString();
//        orderPlacedTime = in.readDouble();
//        userName = in.readString();
//        orderTotal = in.readDouble();
//        orderItemQuantity = in.readArrayList(null);
//
//    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            Order order=new Order();
            order.setStatus(in.readString());
            order.setOrderPlacedTime(in.readString());
            order.setUserName(in.readString());
            order.setOrderItemQuantity(in.readArrayList(null));
            order.setOrderTotal(in.readDouble());
            return order;
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
        parcel.writeString(status);
        parcel.writeString(orderPlacedTime);
        parcel.writeString(userName);
        parcel.writeList(orderItemQuantity);
        parcel.writeDouble(orderTotal);

    }

    public String getActionTodo() {
        return status;
    }

    public void setActionTodo(String actionTodo) {
        this.status = actionTodo;
    }

    public String getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public void setOrderPlacedTime(String orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Integer> getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(ArrayList<Integer> orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public double getOrderTotal() {
        calculateOrderTotal();
        return orderTotal;
    }
public double calculateOrderTotal()
{
    orderTotal=orderItemQuantity.get(0)* MenuActivity.price_1+orderItemQuantity.get(1)* MenuActivity.price_2
            +orderItemQuantity.get(2)* MenuActivity.price_3+orderItemQuantity.get(3)* MenuActivity.price_4;
return orderTotal;
}
private void calculatime()
{

}
    public String getStatus() {
        return status;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public void run() {
        String[] order_status = {"Submitted","Received","Preparing","Packaging","Food Ready"};
        Random rn = new Random();
        int sleepTime = rn.nextInt(60 - 20 + 1) + 20 ; //Sleep Time for thread between 20 to 60 seconds.
        for (int i=0; i<order_status.length; i++) {
            try {
                status = order_status[i];
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}