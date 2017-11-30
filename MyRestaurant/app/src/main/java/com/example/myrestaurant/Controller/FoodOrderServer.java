package com.example.myrestaurant.Controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.Model.Order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vj on 11/24/17.
 */

public class FoodOrderServer extends Service{

    final String FILE = "/Users/vj/Desktop/Inventory.txt";
    final String databasefile = "/Users/vj/Desktop/CustomerDatabase.txt";
    public static final String NOTIFICATION = "com.example.vj.foodorderserver";
    private static final String TAG = "FoodOrderServer";
    static final String userNameExists = "4";
    static final String loginSuccess = "3";
    static final String passcodeWrong = "2";
    static final String noRecord = "1";
    static final String signUpSuccess = "0";
    static final int SIGNUP = 1;
    static final int LOGIN = 2;
    static final int ORDERPLACEMENT = 3;
    static final int STATUSCHECK = 4;
    static final int DEFAULT = 0;
    static Boolean isOrderPlaced = false;
    static final int preparingPhase = 1;
    static final int packagingPhase = 2;
    static final int orderReady = 3;
    //public static String actionTodo="1";
    public static String USERNAME;
    public static String USERPASS;
    public static final String actiontodo = "";


    Map<String, Customer> customerHashMap = new HashMap<String, Customer>();
    FileReader infile = null;
    String oldContent = "";
    Boolean alreadyBuffered = false;
    BufferedWriter bufout = null;
    final String userName = "customerid";
    final String passCode = "passcode";
    final Boolean isSignUp = false;
    ArrayList<String> ServerSideOrderList = new ArrayList<String>();

    /* Singleton Class : Only a single instance of InventoryList needs to be maintained at the kitchen */
    static class InventoryList {

        private static InventoryList inventoryInstance = null;
        private ArrayList<Integer> fooditemCount = new ArrayList<Integer>();

        public static InventoryList getInstance()
        {
            if (inventoryInstance == null)
                inventoryInstance = new InventoryList();

            return inventoryInstance;
        }

        public ArrayList<Integer> getFooditemCount() {
            return fooditemCount;
        }

        public void setFooditemCount(ArrayList<Integer> fooditemCount) {
            this.fooditemCount = fooditemCount;
        }
    }

    public int getFoodItemCount(String [] tempstring, InventoryList inventory){
        int FoodItemCount;
        if (tempstring[0] == "Burger") {
            FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.fooditemCount.get(0);
        } else if (tempstring[0] == "Chicken") {
            FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.fooditemCount.get(1);
        } else if (tempstring[0] == "FrenchFries") {
            FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.fooditemCount.get(2);
        } else {
            FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.fooditemCount.get(3);
        }
        return FoodItemCount;


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateInventoryDatabase(InventoryList inventory){
        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            Log.d(TAG, "inside updateInventoryDatabase");
            in = openFileInput("InventoryDatabase.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String[] tempstring = null;
            String Currline = "";
            int FoodItemCount;
            while ((Currline = reader.readLine()) != null) {
                Log.d(TAG, "Currline is " + Currline);
                oldContent = Currline;
                tempstring = Currline.split(" ");
                System.out.println("Value = " + tempstring[1]);
                FoodItemCount = getFoodItemCount(tempstring, inventory);
                // Now update with the latest food item count
                String newContent = oldContent.replaceAll(tempstring[1], Integer.toString(FoodItemCount));
                Log.d(TAG, "Newline is " + newContent);

                out = openFileOutput("InventoryDatabase.txt", Context.MODE_PRIVATE);
                bufout = new BufferedWriter(new OutputStreamWriter(out));

                bufout.write(newContent);
                bufout.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }

    public void createInventoryDatabase(){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("InventoryDatabase.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));

            writer.write("Burger 500" + "\n" + "Chicken 500" + "\n" + "FrenchFries 500" +  "\n" + "OnionRings 500");
            writer.flush();

            Log.d(TAG, "Done adding items to database");

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate");

        InventoryList kitchenInventory = InventoryList.getInstance();
        createInventoryDatabase();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendBroadcastActivity(String service, String status){

        Intent broadcastIntent = new Intent();
        //broadcastIntent.putExtra("SignUpStatus", "signUpSuccess");
        broadcastIntent.putExtra(service, status);
        broadcastIntent.setAction(".FoodOrderServer");
        Log.d(TAG, "Sent broadcast");
        sendBroadcast(broadcastIntent);

    }

    public void sendBroadcastOrderActivity(Boolean isOrderPlaced, int estimatedTime){

        /* After this the UI could go into Preparing state */
        Intent broadcastIntent = new Intent(NOTIFICATION);
        broadcastIntent.putExtra("isorderplaced", isOrderPlaced);
        broadcastIntent.putExtra("estimatedtime", estimatedTime);
        sendBroadcast(broadcastIntent);

    }

    public void sendBroadcastStatusActivity(int status){

        /* After this the UI could go into Preparing state */
        Intent broadcastIntent = new Intent(NOTIFICATION);
        broadcastIntent.putExtra("status", status);
        sendBroadcast(broadcastIntent);

    }


    /* public synchronized void updateServerSideOrderList(OrderList order){
        float timeOfOrder = order.getOrderPlacedTime();
        String customer = order.getCustomerId();

        ServerSideOrderList.add(customer);

    } */


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleLogin(SignupLogin server){
        Log.d(TAG, "inside handleLogin");
        String username  = server.getUsername();
        String password = server.getPassword();
        Intent broadcastIntent = new Intent();
        if (customerHashMap.containsKey(username)){
            Log.d(TAG, "Customer " + username + " present!");
            Customer customer = customerHashMap.get(username);
            if (Objects.equals(customer.getPassWord(), password)){
                Log.d(TAG, "Password matched!");
                /* Send a status code indicating successful login */
                sendBroadcastActivity("LoginStatus", "LoginSucessful");
            }
            else{
                Log.d(TAG, "Wrong Password!");
                Log.d(TAG, "Existing password = " + customer.getPassWord() + ", received Password = " + password);
                    /* Send a status code indicating wrong password */
                sendBroadcastActivity("LoginStatus", "WrongPassword");
            }
        }
        else{
            Log.d(TAG, "No Customer!");
            /* Send a status code indicating that no customer found, and signup may be required */
            sendBroadcastActivity("LoginStatus", "NorecordFound");
        }
    }

    public void handleSignup(SignupLogin server){
        Log.d(TAG, "inside handleSignup");
        String username  = server.getUsername();
        String password = server.getPassword();
        Intent broadcastIntent = new Intent();
        if (customerHashMap != null && customerHashMap.containsKey(username)){
            /* Send a status code indicating Username already exists, pick a new one */
            sendBroadcastActivity("SignUpStatus", "userNameExists");
        }
        else {
            Customer newCustomer = new Customer(username, password);
            customerHashMap.put(username, newCustomer);
            Log.d(TAG, "Added new new customer");
            /* Send a status code indicating successful signup */
            sendBroadcastActivity("SignUpStatus", "signUpSuccess");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        InventoryList kitchenInventory = InventoryList.getInstance();

        /* Already assign the kitchen inventory with 50 items , and deduct the same from Inventory database */
        for (int count = 0; count < 4; count++){
            kitchenInventory.fooditemCount.add(50);
        }

        updateInventoryDatabase(kitchenInventory);

        String actionreceived = intent.getStringExtra(actiontodo);
        Bundle b = intent.getExtras();

        if (Objects.equals(actionreceived, "SignUp")){
            SignupLogin server = (SignupLogin)b.getParcelable("ServerObject");
            String userName = server.getUsername();
            String userPass = server.getPassword();
            Log.d(TAG, "Username = "+userName);
            Log.d(TAG, "Password = "+userPass);

            handleSignup(server);
        }
        else if (Objects.equals(actionreceived, "Login")){
            SignupLogin server = (SignupLogin)b.getParcelable("ServerObject");
            String userName = server.getUsername();
            String userPass = server.getPassword();
            Log.d(TAG, "Username = "+userName);
            Log.d(TAG, "Password = "+userPass);

            /* Use below 4 lines for testing login */
            String username = "vj";
            String password = "1234";
            Customer newCustomer = new Customer(username, password);
            customerHashMap.put(username, newCustomer);
            handleLogin(server);

        }
        else if (Objects.equals(actionreceived, "OrderPlacement")){
            /* TODO : Need to sync with Client code for this intent */
            /* TODO : Need to have a separate class for OrderList (and OrderItem) recognised by both client and server */
            /* TODO : Also Client code to have object for type Order and put it intent and send it through Parcelable */
            Log.d(TAG, "Order is placed");
            Order currentorder = b.getParcelable("OrderObject");

            String string = b.getParcelable("TestString");
            Log.d(TAG, string);
            String time = currentorder.getOrderPlacedTime();
            String username=currentorder.getUserName();
            double totalPrice=currentorder.getOrderTotal();
            int [] orderItem=currentorder.getOrderItemQuantity();
            Log.d(TAG, "time"+time);
            Log.d(TAG, "totalPrice"+totalPrice);
            Log.d(TAG, "orderItem"+orderItem);

            /* Get the customer object using the username as key */
            Customer customer = customerHashMap.get(username);
            if (customer == null){
                Log.e(TAG, "Trying to place order for non-member " + username);
            }
//            customer.setCustomerActiveOrder(orderItem);

            Iterator<Integer> it = customer.getCustomerActiveOrder().iterator();
            int count = 0;
            int estimatedTime = 15;
            //getEstimatedTimeFood();
            ArrayList<Integer> remainingFoodCount = new ArrayList<Integer>();

            while(it.hasNext()) {
                int fooditemcount = it.next();
                if (fooditemcount == 0){
                    continue;
                }
                else if (fooditemcount > kitchenInventory.fooditemCount.get(count)){
                    // The item requested exceed what we have in kitchen, have to make an emergency request to Inventory
                }
                else{
                    // We can honor this request right away
                    remainingFoodCount.add(kitchenInventory.fooditemCount.get(count) - fooditemcount);
                    isOrderPlaced = true;
                    customer.setTimeAtCurrentOrder(new Date().getTime() / 1000);
                    sendBroadcastOrderActivity(isOrderPlaced, estimatedTime);

                    /* The order will be complete in 15 mins */
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sendBroadcastStatusActivity(orderReady);
                        }
                    }, 15*60*1000);
                }
                count++;
            }

            // Update kitchen inventory with the food item count remaining
            kitchenInventory.setFooditemCount(remainingFoodCount);

            /* We could do this for multi threads -> multi customers */
            //updateServerSideOrderList(order);
        }
        else if (Objects.equals(actionreceived, "OrderStatusCheck")) {
            /* Handle track order status message from client */
            String username = intent.getStringExtra(userName);
            int preparationTime = 13;
            int packagingTime = 2;
            double currentTime = new Date().getTime() / 1000;
            Customer customer = customerHashMap.get(username);


            if (currentTime > customer.getTimeAtCurrentOrder()) {
                double diff = currentTime - customer.getTimeAtCurrentOrder();

                if (diff <= preparationTime) {
                    sendBroadcastStatusActivity(preparingPhase);
                }
                else if (diff >= preparationTime && diff <= (packagingTime + preparationTime) ){
                    sendBroadcastStatusActivity(packagingPhase);
                }
                else if(diff >= (packagingTime + preparationTime)){
                    sendBroadcastStatusActivity(orderReady);
                }
            } else {
                // error : track order msg received before order placement?
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped...",Toast.LENGTH_LONG).show();
    }
}
