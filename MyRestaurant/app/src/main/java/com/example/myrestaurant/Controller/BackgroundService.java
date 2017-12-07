package com.example.myrestaurant.Controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.Model.CustomerList;
import com.example.myrestaurant.Model.Inventory;
import com.example.myrestaurant.Model.Order;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.Timer;


public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private CustomerList customerList=CustomerList.getInstance();
    public static Customer currentCustomer;
    private Inventory inventory=Inventory.getInstance();
    private Order partialOrder=new Order();

    public static final String actiontodo = "";


    public void createInventoryDatabase(){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("SupplierInventory.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));

            writer.write("Burger 500" + "\n" + "Chicken 500" + "\n" + "FrenchFries 500" +  "\n" + "OnionRings 500");
            writer.flush();

            Log.d(TAG, "Done adding items to Supplier database");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate"+inventory.getInventory());

        Timer timer;
        timer = new Timer();
        /* Create Supplier Database */
        createInventoryDatabase();
        inventory.setmContext(BackgroundService.this);
        Log.d(TAG, "Creating new thread for inventory");
        Thread inventoryThread = new Thread(inventory);
        inventory.setFoodItemCountNeeded(50);
        Log.d(TAG, "Starting thread");
        inventoryThread.start();

/*
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Thread inventoryThread = new Thread(inventory);
                        inventoryThread.start();
                    }
                },
                2000, (30 * 60 * 1000)
        );*/
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand"+inventory.getInventory());

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
        else if (Objects.equals(actionreceived, "OrderPlacement")) {
            Log.d(TAG, "Order is underChecking");
            Bundle b = intent.getExtras();
            Order currentorder = (Order) b.get("OrderObject");
            Intent broadcastIntent = new Intent();

            if (checkOrderStatus(currentorder)) {
                Log.d(TAG, "OrderStatus---avaliable");
                currentorder.setUserName(currentCustomer.getUserName());
                currentorder.setStatus(Order.OrderStatus.Submitted.getValue());
                currentCustomer.getOrderList().getOrderArrayList().add(currentorder);
                Thread d=new Thread(currentorder);
                d.start();
                orderFromInventory(currentorder);
                broadcastIntent.putExtra("OrderStatus", "avaliable");
                broadcastIntent.setAction(".OrderStatus");
                sendBroadcast(broadcastIntent);
            } else {
                setParticialOrder(currentorder);
                if (partialOrder.getOrderItemQuantity().get(0) >0 || partialOrder.getOrderItemQuantity().get(1) > 0
                        ||partialOrder.getOrderItemQuantity().get(2) > 0 ||
                        partialOrder.getOrderItemQuantity().get(3) > 0)//making sure the partial sure contains ar least one item
                {   Log.d(TAG, "OrderStatus---PartlyAvaliable");
                    broadcastIntent.putExtra("OrderStatus", "PartlyAvaliable");
                    broadcastIntent.setAction(".OrderStatus");
                    sendBroadcast(broadcastIntent);

                } else {
                    Log.d(TAG, "OrderStatus---OrderFail");
                    broadcastIntent.putExtra("OrderStatus", "OrderFail");
                    broadcastIntent.setAction(".OrderStatus");
                    sendBroadcast(broadcastIntent);
                }

            }
        }
        else if (Objects.equals(actionreceived, "ParticallyOrderPlacement")){
            Log.d(TAG, "ParticallyOrderPlacement---ParticallyOrderPlacement");
            Log.d(TAG, "ParticallyOrderPlacement---CurrentOrder"+partialOrder.getOrderItemQuantity());
            Log.d(TAG, "CurrentInventory"+inventory.getInventory());

            Intent broadcastIntent = new Intent();
            Order order= currentCustomer.getOrderList().newOrderToadd();
            for(int i=0;i<4;i++)
            { order.getOrderItemQuantity().set(i,partialOrder.getOrderItemQuantity().get(i));}
            order.setUserName(currentCustomer.getUserName());
            order.setStatus(Order.OrderStatus.Submitted.getValue());
            order.setOrderId(Order.orderIdCount+1);
            order.setOrderPlacedTime(Order.getStringToday());

            Thread d=new Thread(partialOrder);
            d.start();
            orderFromInventory(partialOrder);
            broadcastIntent.putExtra("OrderStatus", "avaliable");
            broadcastIntent.setAction(".OrderStatus");
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "ParticallyOrderPlacement: partialOrder"+partialOrder);
        }
        else if (Objects.equals(actionreceived, "ViewOrderHistory")) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("BackOrderHistory", currentCustomer.getUserName());
            broadcastIntent.setAction(".BackOrderHistory");
            sendBroadcast(broadcastIntent);
            Log.d(TAG, "ViewOrderHistory: "+currentCustomer.getUserName());
        }
        else if (Objects.equals(actionreceived, "ViewOrderDetail")) {
        }
        Log.d(TAG, "Reached last part return START_STICKY");
        return START_STICKY;
    }

    private boolean checkOrderStatus(Order currentorder) {
        for(int i=0;i<4;i++)
        {
            if(inventory.getInventory().get(i)<currentorder.getOrderItemQuantity().get(i))
            {
                return false;
            }
        }
        return true;
    }
    private void setParticialOrder(Order currentorder) {
        for(int i=0;i<4;i++)
        {
            if(inventory.getInventory().get(i)<currentorder.getOrderItemQuantity().get(i))
            {
                partialOrder.getOrderItemQuantity().set(i,inventory.getInventory().get(i));
            }else  partialOrder.getOrderItemQuantity().set(i,currentorder.getOrderItemQuantity().get(i));
        }

    }
private void orderFromInventory(Order currentorder)
{
    for(int i=0;i<4;i++)
    {
            int s=inventory.getInventory().get(i)-currentorder.getOrderItemQuantity().get(i);
            inventory.getInventory().set(i,s);

    }

}
    public void handleSignup(String userName,String userPass){
        Log.d(TAG, "inside handleSignup");
        Intent broadcastIntent = new Intent();
        if(customerList.containsCustomer(userName)){
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

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void updateInventoryDatabase(Inventory inventory){
//        FileInputStream in = null;
//        FileOutputStream out = null;
//        BufferedReader reader = null;
//        StringBuilder content = new StringBuilder();
//        try {
//            Log.d(TAG, "inside updateInventoryDatabase");
//            in = openFileInput("InventoryDatabase.txt");
//            reader = new BufferedReader(new InputStreamReader(in));
//            String[] tempstring = null;
//            String Currline = "";
//            int FoodItemCount;
//            while ((Currline = reader.readLine()) != null) {
//                Log.d(TAG, "Currline is " + Currline);
//                oldContent = Currline;
//                tempstring = Currline.split(" ");
//                System.out.println("Value = " + tempstring[1]);
//                FoodItemCount = getFoodItemCount(tempstring, inventory);
//                // Now update with the latest food item count
//                String newContent = oldContent.replaceAll(tempstring[1], Integer.toString(FoodItemCount));
//                Log.d(TAG, "Newline is " + newContent);
//
//                out = openFileOutput("InventoryDatabase.txt", Context.MODE_PRIVATE);
//                bufout = new BufferedWriter(new OutputStreamWriter(out));
//
//                bufout.write(newContent);
//                bufout.flush();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return;
//    }
}
