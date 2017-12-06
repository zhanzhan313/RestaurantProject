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

/**
 * Created by 站站 on 2017/12/4.
 */

public class Inventory {
    private ArrayList<Integer> inventorylist;
    private static final String TAG = "Inventory";
    private static final Inventory ourInstance = new Inventory();

    public  static Inventory getInstance() {
        return ourInstance;
    }

    private Inventory() {

        inventorylist = new ArrayList<>();
        for(int i=0;i<4;i++)
        {
            inventorylist.add(5);
        }

    }

    public ArrayList<Integer> getInventory() {
        return inventorylist;
    }

    public void setInventory(ArrayList<Integer> inventory) {
        this.inventorylist = inventory;
    }
}
