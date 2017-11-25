package com.example.vj.foodorderserver;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vj on 11/24/17.
 */

public class FoodOrderServer extends Service{

    final String FILE = "/Users/vj/Desktop/Inventory.txt";
    final String databasefile = "/Users/vj/Desktop/CustomerDatabase.txt";
    public static final String NOTIFICATION = "com.example.vj.foodorderserver";
    static final String success = "3";
    static final String passcodeWrong = "2";
    static final String noRecord = "1";
    static final String signUp = "0";
    static final int SIGNUP = 1;
    static final int LOGIN = 2;
    static final int ORDERPLACEMENT = 3;


    Map<String, String> customerHashMap = new HashMap<String, String>();
    FileReader infile = null;
    String oldContent = "";
    Boolean alreadyBuffered = false;
    BufferedWriter bufout = null;
    final String customerId = "customerid";
    final String passCode = "passcode";
    final Boolean isSignUp = false;

    /* Singleton Class : Only a single instance of InventoryList needs to be maintained at the kitchen */
    static class InventoryList {

        private static class signUpLogin{
            private Boolean isSignUp;
            private String customerId;
            private String passcode;
        }

        private static InventoryList inventoryInstance = null;

        private int burgerCount;
        private int chickenCount;
        private int frenchFriesCount;
        private int onionRingsCount;

        public static InventoryList getInstance()
        {
            if (inventoryInstance == null)
                inventoryInstance = new InventoryList();

            return inventoryInstance;
        }

        public int getBurgerCount() {
            return burgerCount;
        }

        public int getChickenCount() {
            return chickenCount;
        }

        public int getFrenchFriesCount() {
            return frenchFriesCount;
        }

        public int getOnionRingsCount() {
            return onionRingsCount;
        }
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
                    if (tempstring[0] == "Burger") {
                        FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.burgerCount;
                    } else if (tempstring[0] == "Chicken") {
                        FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.chickenCount;
                    } else if (tempstring[0] == "FrenchFries") {
                        FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.frenchFriesCount;
                    } else {
                        FoodItemCount = Integer.parseInt(tempstring[1]) - inventory.onionRingsCount;
                    }

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
        kitchenInventory.burgerCount = 50;
        kitchenInventory.chickenCount = 50;
        kitchenInventory.frenchFriesCount = 50;
        kitchenInventory.onionRingsCount = 50;

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
            broadcastIntent.putExtra("SignUpDone", signUp);
        }
        if(result == "1") {
            broadcastIntent.putExtra("SuccessLogin", success);
        }
        else if(result == "2"){
            broadcastIntent.putExtra("WrongPassword", passcodeWrong);
        }
        else if(result == "3"){
            broadcastIntent.putExtra("NoCustomerRecord", noRecord);
        }
        else{
            System.out.println("Wrong result code received!!");
        }
        sendBroadcast(broadcastIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started...",Toast.LENGTH_LONG).show();

        /* TODO : Need to sync with Client code for this intent */
        int actionreceived = intent.getIntExtra("actionTodo", 0);

        if (actionreceived == SIGNUP){
            String customername  = intent.getStringExtra(customerId);
            String password = intent.getStringExtra(passCode);
            customerHashMap.put(customername, password);
             /* Send a status code indicating successful signup */
            sendBroadcastActivity(signUp);
        }
        else if (actionreceived == LOGIN){
            String customername  = intent.getStringExtra(customerId);
            String password = intent.getStringExtra(passCode);
            if (customerHashMap.containsKey(customername)){
                Log.d("Customer present", customername);
                String databasePasscode = customerHashMap.get(customername);
                if (databasePasscode == password){
                    System.out.println("Customer found");
                    /* Send a status code indicating successful login */
                    sendBroadcastActivity(success);
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
        else if (actionreceived == ORDERPLACEMENT){
            /* TODO : Need to sync with Client code for this intent */
            /* TODO : Need to have a separate class for OrderList (and OrderItem) recognised by both client and server */
            /* TODO : Also Client code to have object for type OrderList and put it intent and send it through Parcelable */

            /* The object that is passed from Client side should have Variable decalred by name "orderlist"? */
            OrderList order = (OrderList)intent.getParcelableExtra("orderlist");

            order.



        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped...",Toast.LENGTH_LONG).show();
    }
}
