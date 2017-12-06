package com.example.myrestaurant.Model;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;


public class Inventory implements Runnable{
    private int foodItemCountNeeded;
    private ArrayList<Integer> inventorylist;
    private static final String TAG = "Inventory";
    private static Inventory ourInstance = null;
    protected Context mContext;

    public static Inventory getInstance() {

        if (ourInstance == null)
            ourInstance = new Inventory();

        return ourInstance;
    }


    public ArrayList<Integer> getInventory() {
        return inventorylist;
    }

    public void setInventory(ArrayList<Integer> inventory) {
        this.inventorylist = inventory;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


    public int getFoodItemCountNeeded() {
        return foodItemCountNeeded;
    }

    public void setFoodItemCountNeeded(int foodItemCountNeeded) {
        this.foodItemCountNeeded = foodItemCountNeeded;
    }

    public ArrayList<Integer> checkInventoryDatabase(int foodItemCountNeeded) {

        Boolean alreadyBuffered = false;
        BufferedWriter bufout = null;
        String oldContent = "";

        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        Boolean quantityPresent = false;
        ArrayList<Integer> countAvailable = new ArrayList<Integer>();
        try {
            Log.d(TAG, "inside checkInventoryDatabase");
            in = mContext.openFileInput("SupplierInventory.txt");

            reader = new BufferedReader(new InputStreamReader(in));
            String[] tempstring = null;
            String Currline = "";
            int FoodItemCount;
            int count = 0;
            while ((Currline = reader.readLine()) != null) {
                Log.d(TAG, "Currline is " + Currline);
                oldContent = Currline;
                tempstring = Currline.split(" ");
                System.out.println("Value = " + tempstring[1]);
                if (Integer.parseInt(tempstring[1]) > foodItemCountNeeded){
                    Log.d(TAG, "Added " +  foodItemCountNeeded);
                    countAvailable.add(foodItemCountNeeded);
                }
                else{
                    countAvailable.add(Integer.parseInt(tempstring[1]));
                }
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

        for(int temp: countAvailable){
            Log.d(TAG, "CountAvailable = " + temp);
        }
        return countAvailable;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateInventoryDatabase(ArrayList<Integer> FoodItemCountTaken){
        BufferedWriter bufout = null;
        String oldContent = "";

        FileInputStream in = null;
        FileOutputStream out = null;
        BufferedReader reader = null;
        try {

            Log.d(TAG, "inside updateInventoryDatabase");
            in = mContext.openFileInput("SupplierInventory.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String[] tempstring = null;
            String Currline = "";
            Iterator it = FoodItemCountTaken.iterator();
            while ((Currline = reader.readLine()) != null && it.hasNext()) {

                Log.d(TAG, "Currline is " + Currline);
                oldContent = Currline;
                tempstring = Currline.split(" ");
                System.out.println("Value = " + tempstring[1]);

                int takenCount = (int) it.next();
                int diff = Integer.parseInt(tempstring[1]) - takenCount;

                // Now update with the latest food item count
                String newContent = oldContent.replaceAll(tempstring[1], Integer.toString(diff));
                Log.d(TAG, "newContent is " + newContent);

                out = mContext.openFileOutput("SupplierInventory.txt", Context.MODE_PRIVATE);
                bufout = new BufferedWriter(new OutputStreamWriter(out));

                bufout.write(newContent);
                bufout.flush();
            }
        }catch (IOException e) {
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


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        /* Ask for count 50, if supplier has it, then it will return 50, else it will send whatever count it has */
        /* The kitchen got updated with count 50 */
        Log.d(TAG, "Entered RUN");
        Inventory inventory = Inventory.getInstance();

        ArrayList<Integer> FoodItemCountAvailable = new ArrayList<Integer>();

        Log.d(TAG, "Starting check inventory");
        FoodItemCountAvailable = checkInventoryDatabase(inventory.getFoodItemCountNeeded());

        Log.d(TAG, "Set kitchen inventory");
        /* Update the kitchen Inventory list*/
        inventory.setInventory(FoodItemCountAvailable);

        Log.d(TAG, "Starting updating inventory");
        /* Next update Supplier Inventory List that we have taken that count */
        updateInventoryDatabase(FoodItemCountAvailable);
    }
}
