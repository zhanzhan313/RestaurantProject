package com.example.myrestaurant.Controller;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.Model.Order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vj on 11/24/17.
 */

public class FoodOrderServer extends Service{

    final String FILE = "/Users/vj/Desktop/Inventory.txt";
    final String databasefile = "/Users/vj/Desktop/CustomerDatabase.txt";
    public static final String NOTIFICATION = "com.example.vj.foodorderserver";
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
    public static String actionTodo="1";
    public static String USERNAME;
    public static String USERPASS;


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
        try {
            infile = new FileReader(FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try (BufferedReader bufin = new BufferedReader(infile)) {
            String Currline = null;
            String[] tempstring = null;
            int FoodItemCount;

            while((Currline = bufin.readLine()) != null) {
				/* Added this extra check since certain files may have extra hidden characters */
                if(Currline.contains(" ")) {
                    oldContent = Currline;
                    tempstring = Currline.split(" ");
                    System.out.println("Value = " + tempstring[1]);
                    FoodItemCount = getFoodItemCount(tempstring, inventory);
                    /* Now update with the latest food item count */
                    String newContent = oldContent.replaceAll(tempstring[1], Integer.toString(FoodItemCount));
                    System.out.println("New line = " + newContent);

                    if (alreadyBuffered = false) {
                        bufout = new BufferedWriter(new FileWriter(FILE));
                        alreadyBuffered = true;
                    }

                    bufout.write(newContent);
                    bufout.flush();
                    bufout.newLine();
                }
                else {
                    System.out.println("Line corrupted, hidden characters. Use cleaned and newly created file");
                }
            }
        }catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {

        Toast.makeText(this, "Service Created...",Toast.LENGTH_LONG).show();
        InventoryList kitchenInventory = InventoryList.getInstance();

        /* Already assign the kitchen inventory with 50 items , and deduct the same from Inventory database */
        for (int count = 0; count < 4; count++){
            kitchenInventory.fooditemCount.add(50);
        }

        updateInventoryDatabase(kitchenInventory);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendBroadcastActivity(String result){
        Intent broadcastIntent = new Intent(NOTIFICATION);
        if(result == "0") {
            broadcastIntent.putExtra("SignUpDone", signUpSuccess);
        }
        if(result == "1") {
            broadcastIntent.putExtra("SuccessLogin", loginSuccess);
        }
        else if(result == "2"){
            broadcastIntent.putExtra("WrongPassword", passcodeWrong);
        }
        else if(result == "3"){
            broadcastIntent.putExtra("NoCustomerRecord", userNameExists);
        }
        else if(result == "4"){
            broadcastIntent.putExtra("UserNameExists", noRecord);
        }
        else{
            System.out.println("Wrong result code received!!");
        }
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

    public void handleLogin(Intent intent){
        String username  = intent.getStringExtra(userName);
        String password = intent.getStringExtra(passCode);
        if (customerHashMap.containsKey(username)){
            Log.d("Customer present", username);
            Customer customer = customerHashMap.get(username);
            if (customer.getPassWord() == password){
                System.out.println("Customer found");
                    /* Send a status code indicating successful login */
                sendBroadcastActivity(loginSuccess);
            }
            else{
                System.out.println("Password entered is wrong");
                    /* Send a status code indicating wrong password */
                sendBroadcastActivity(passcodeWrong);
            }
        }
        else{
            System.out.println("No record of customer found");
                /* Send a status code indicating that no customer found, and signup may be required */
            sendBroadcastActivity(noRecord);
        }
    }

    public void handleSignup(Intent intent){
        String username  = intent.getStringExtra(userName);
        String password = intent.getStringExtra(passCode);
        if (customerHashMap != null && customerHashMap.containsKey(username)){
                 /* Send a status code indicating Username already exists, pick a new one */
            sendBroadcastActivity(userNameExists);
        }
        Customer newCustomer = new Customer(username, password);
        customerHashMap.put(username, newCustomer);
             /* Send a status code indicating successful signup */
        sendBroadcastActivity(signUpSuccess);
    }

    private static final String TAG = "FoodOrderServer";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.print("onStartCommand");
        Log.d(TAG, "onstartCommand");
     String userName=intent.getStringExtra(USERNAME);
        String userPass=intent.getStringExtra(USERPASS);
        Log.d(TAG, userName+" "+userPass);


        /* TODO : Need to sync with Client code for this intent */
        int actionreceived = intent.getIntExtra("actionTodo", DEFAULT);

        if (actionreceived == SIGNUP){
            handleSignup(intent);
        }
        else if (actionreceived == LOGIN){
            handleLogin(intent);
        }
        else if (actionreceived == ORDERPLACEMENT){
            /* TODO : Need to sync with Client code for this intent */
            /* TODO : Need to have a separate class for OrderList (and OrderItem) recognised by both client and server */
            /* TODO : Also Client code to have object for type Order and put it intent and send it through Parcelable */

            /* The object that is passed from Client side should have Variable declared by name "orderlist"? */
            Order order = (Order)intent.getParcelableExtra("order");

            String username  = order.getUserName();
            ArrayList<Integer> ordereditems = order.getOrderItemQuantity();
            InventoryList kitchenInventory = InventoryList.getInstance();

            /* Get the customer object using the username as key */
            Customer customer = customerHashMap.get(username);
            customer.setCustomerActiveOrder(ordereditems);

            Iterator<Integer> it = customer.getCustomerActiveOrder().iterator();
            int count = 0;
            int estimatedTime = 15;
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
        else if (actionreceived == STATUSCHECK) {
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
