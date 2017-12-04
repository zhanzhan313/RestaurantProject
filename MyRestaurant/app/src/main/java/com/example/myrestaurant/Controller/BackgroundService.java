package com.example.myrestaurant.Controller;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.Model.CustomerList;
import com.example.myrestaurant.Model.Order;
import com.example.myrestaurant.Model.SignupLogin;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private CustomerList customerList;
    private Customer currentCustomer;

    public static final String actiontodo = "";
    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        customerList =CustomerList.getInstance();
        Log.d(TAG, "onCreate");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        String actionreceived = intent.getStringExtra(actiontodo);
        Log.d(TAG, "actionreceived: " + actionreceived);


        if (Objects.equals(actionreceived, "SignUp")){
            String userName=intent.getStringExtra("Username");
            String userPass = intent.getStringExtra("Password");
            handleSignup(userName,userPass);
            Log.d(TAG, "Username = "+userName);
            Log.d(TAG, "Password = "+userPass);
        }
        else if (Objects.equals(actionreceived, "Login")){
            String userName=intent.getStringExtra("LoginUsername");
            String userPass = intent.getStringExtra("LoginPassword");
            handleLogin(userName,userPass);
            Log.d(TAG, "LoginUsername = "+userName);
            Log.d(TAG, "LoginPassword = "+userPass);

        }
        else if (Objects.equals(actionreceived, "OrderPlacement")){
            Log.d(TAG, "Order is placed");
            Bundle b = intent.getExtras();
            Order currentorder = (Order) b.get("OrderObject");
            Log.d(TAG, "currentorder " + currentorder);
            Log.d(TAG, "currentCustomer " + currentCustomer);
            currentorder.setUserName(currentCustomer.getUserName());
            currentCustomer.getOrderList().getOrderArrayList().add(currentorder);

//            InventoryList kitchenInventory = InventoryList.getInstance();


            Log.d(TAG, "AT foodorderserver received username " + currentCustomer.getUserName());
            Log.d(TAG, "AT foodorderserver received username " + currentCustomer.getPassWord());
            Log.d(TAG, "AT foodorderserver received username " + currentCustomer.getOrderList().getOrderArrayList().get(0).getOrderItemQuantity());



//                ArrayList<Integer> checkingquant = currentorder.getOrderItemQuantity();
//
//                if (checkingquant.size() == 0){
//                    Log.d(TAG, "Error: ArrayList size received 0");
//
//                }
//                for(int temp: checkingquant){
//                    Log.d(TAG, "Food quantity = " + temp);
//                }
//
//                customer.setCustomerActiveOrder(currentorder.getOrderItemQuantity());
//                Log.d(TAG, "Moving ahead : we just assigned customer object with order arraylist");
//
//                Iterator<Integer> it2 = customer.getCustomerActiveOrder().iterator();
//                int count = 0;
//                int estimatedTime = 15;
//                //getEstimatedTimeFood();
//                ArrayList<Integer> remainingFoodCount = new ArrayList<Integer>();
//
//                while (it2.hasNext()) {
//                    int fooditemcount = it2.next();
//                    if (fooditemcount == 0) {
//                        continue;
//                    } else if (fooditemcount > kitchenInventory.fooditemCount.get(count)) {
//                        // The item requested exceed what we have in kitchen, have to make an emergency request to Inventory
//                    } else {
//                        // We can honor this request right away
//                        Log.d(TAG, "inside we can honor this request");
//                        isOrderPlaced = true;
//                        remainingFoodCount.add(kitchenInventory.fooditemCount.get(count) - fooditemcount);
//                    }
//                    count++;
//                }
//
//                if(isOrderPlaced == true){
//                    customer.setTimeAtCurrentOrder(new Date().getTime() / 1000);
//                    sendBroadcastOrderActivity(isOrderPlaced, estimatedTime);
//
//                    /* The order will be complete in 15 mins */
//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            sendBroadcastStatusActivity(orderReady);
//                        }
//                    }, 15 * 60 * 1000);
//                }
//
//                for(int temp: remainingFoodCount){
//                    Log.d(TAG, "Remaining quantity in inventory = " + temp);
//                }
//
//                // Update kitchen inventory with the food item count remaining
//                kitchenInventory.setFooditemCount(remainingFoodCount);
//
//            /* We could do this for multi threads -> multi customers */
//                //updateServerSideOrderList(order);

        }
        else if (Objects.equals(actionreceived, "ViewOrderHistory")) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("BackOrderHistory", currentCustomer.getUserName());
            broadcastIntent.setAction(".BackOrderHistory");
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "ViewOrderHistory: "+currentCustomer.getUserName());
        }
//        else if (Objects.equals(actionreceived, "OrderStatusCheck")) {
//            Intent broadcastIntent = new Intent();
//            broadcastIntent.putExtra("SignUpStatus", "SignUpFailed");
//            broadcastIntent.setAction(".SignUpStatus");
//        }
        Log.d(TAG, "Reached last part return START_STICKY");
        return START_STICKY;
    }

    public void handleSignup(String userName,String userPass){
        Log.d(TAG, "inside handleSignup");
        Intent broadcastIntent = new Intent();
        if(customerList.containsCustomer(userName)){
            //broadcastIntent.putExtra("SignUpStatus", "signUpSuccess");
            currentCustomer=null;

            broadcastIntent.putExtra("SignUpStatus", "SignUpFailed");
            broadcastIntent.setAction(".SignUpStatus");
        }
        else {
            Customer newCustomer = new Customer();
            newCustomer.setUserName(userName);
            newCustomer.setPassWord(userPass);
            customerList.addCustomer(newCustomer);
            Log.d(TAG, "Added new new customer");
            /* Send a status code indicating successful signup */
           currentCustomer=newCustomer;

            broadcastIntent.putExtra("SignUpStatus", "SignUpSuccess");
            broadcastIntent.setAction(".SignUpStatus");
        }   Log.d(TAG, "Sent broadcast");
        sendBroadcast(broadcastIntent);
    }
    public void handleLogin(String userName,String userPass){
        Log.d(TAG, "inside handleLogin");
        Intent broadcastIntent = new Intent();
        if(!customerList.containsCustomer(userName)){
            currentCustomer=null;
            broadcastIntent.putExtra("LoginStatus", "LoginFailed");
            broadcastIntent.setAction(".LoginStatus");
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "BroadCasting LoginFailed");
            return;
        }
        Customer customer=customerList.verifyCustomer(userName,userPass);
        if(customer==null)
        {
            broadcastIntent.putExtra("LoginStatus", "NorecordFound");
            broadcastIntent.setAction(".LoginStatus");
            sendBroadcast(broadcastIntent);
            currentCustomer=null;
            Log.d(TAG, "BroadCasting NorecordFound");
            return;
        }
        broadcastIntent.putExtra("LoginStatus", "LoginSuccess");
        broadcastIntent.setAction(".LoginStatus");
        currentCustomer=customer;
        Log.d(TAG, "Sent broadcast LoginSuccess");
        sendBroadcast(broadcastIntent);
    }
}
