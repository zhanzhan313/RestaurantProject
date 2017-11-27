package com.example.myrestaurant.Controller;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.example.myrestaurant.Model.Customer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SignUpService extends Service {
    public static String actionTodo="1";
//    public static String USERNAME;
//    public static String USERPASS;
    Map<String, Customer> customerHashMap = new HashMap<String, Customer>();
//    public static final String NOTIFICATION = "com.example.vj.foodorderserver";

    public SignUpService() {
    }

    private static final String TAG = "SignUpService";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        readFile();
    }

    public void sendBroadcastActivity(String result){
        Log.d(TAG, "sendBroadcastActivity: Now");
        Intent broadcastIntent = new Intent();
//        broadcastIntent.putExtra("RESULT", "1");

        if(result.equals( "Success")) {
            broadcastIntent.putExtra("RESULT", "SignUpSuccessfully");
        }
        else{
            System.out.println("Wrong result code received!!");
            broadcastIntent.putExtra("RESULT", "SignUpFail");
        }
        broadcastIntent.setAction(".SignUpService");
        sendBroadcast(broadcastIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: ");
        Bundle b = intent.getExtras();
        SignupLogin server = (SignupLogin)b.getParcelable("ServerObject");

        String action = server.getActionToDo();
        String userName = server.getUsername();
        String userPass = server.getPassword();
        save(userName,userPass);
        Log.d(TAG, "ActionTodo"+action);
        Log.d(TAG, "Username"+userName);
        Log.d(TAG, "userPass"+userPass);


//        String userName=intent.getStringExtra("USERNAME");
//        String userPass=intent.getStringExtra("USERPASS");
//        Log.d(TAG, userName+" "+userPass);
        sendBroadcastActivity("Success");
        if (customerHashMap != null && customerHashMap.containsKey(userName)){
                 /* Send a status code indicating Username already exists, pick a new one */
           //sendBroadcastActivity(userNameExists);
        }
        Customer newCustomer = new Customer(userName, userPass);
        customerHashMap.put(userName, newCustomer);
             /* Send a status code indicating successful signup */
//        sendBroadcastActivity("Success");

        return super.onStartCommand(intent, flags, startId);

    }
    public void save(String userName,String userPass) {

        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(userName+","+userPass);
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
    public void readFile() {
        String Name="";
        String Pass="";
        try {

            Scanner inS = new Scanner(new File("example/Input.txt"));
            inS.useDelimiter("[,\n]");

            while (inS.hasNext()) {
                Name = inS.next();
                Pass = inS.next();
                Log.d(TAG, "readFile: "+Name+" "+Pass);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
