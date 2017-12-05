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
import com.example.myrestaurant.Model.Inventory;
import com.example.myrestaurant.Model.Order;

import java.util.Objects;


public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private CustomerList customerList=CustomerList.getInstance();
    private Customer currentCustomer;
    private Inventory inventory=Inventory.getInstance();
    private Order partialOrder=new Order();

    public static final String actiontodo = "";


    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate"+inventory.getInventory());
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onCreate"+inventory.getInventory());
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

        }else if (Objects.equals(actionreceived, "ParticallyOrderPlacement")){
            Log.d(TAG, "ParticallyOrderPlacement---avaliable");
            Bundle b = intent.getExtras();
            Order currentorder = (Order) b.get("ParticallyOrderObject");
            Log.d(TAG, "ParticallyOrderPlacement---CurrentOrder"+currentorder.getOrderItemQuantity());
            Log.d(TAG, "CurrentInventory"+inventory.getInventory());
            setParticialOrder(currentorder);
            if(partialOrder.getOrderItemQuantity().get(0)==0&&partialOrder.getOrderItemQuantity().get(1)==0
                    &&partialOrder.getOrderItemQuantity().get(2)==0&&
                    partialOrder.getOrderItemQuantity().get(3)==0)//making sure the partial sure contains ar least one item
            {
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra("OrderStatus", "ParticallyOrderFail");
                broadcastIntent.setAction(".OrderStatus");
                sendBroadcast(broadcastIntent);
            }
            partialOrder.setUserName(currentCustomer.getUserName());
            currentCustomer.getOrderList().getOrderArrayList().add(partialOrder);
            orderFromInventory(partialOrder);
            Log.d(TAG, "ParticallyOrderPlacement: partialOrder"+partialOrder);
        }
        else if (Objects.equals(actionreceived, "OrderPlacement")){
            Log.d(TAG, "Order is placed");
            Bundle b = intent.getExtras();
            Order currentorder = (Order) b.get("OrderObject");
            Intent broadcastIntent = new Intent();

            if(checkOrderStatus(currentorder))
            {
                Log.d(TAG, "OrderStatus---avaliable");
                currentorder.setUserName(currentCustomer.getUserName());
                currentCustomer.getOrderList().getOrderArrayList().add(currentorder);
                orderFromInventory(currentorder);
                broadcastIntent.putExtra("OrderStatus", "avaliable");
                broadcastIntent.setAction(".OrderStatus");
                sendBroadcast(broadcastIntent);
            }
            else{
                setParticialOrder(currentorder);
                if(partialOrder.getOrderItemQuantity().get(0)==0&&partialOrder.getOrderItemQuantity().get(1)==0
                        &&partialOrder.getOrderItemQuantity().get(2)==0&&
                        partialOrder.getOrderItemQuantity().get(3)==0)//making sure the partial sure contains ar least one item
                {
                    broadcastIntent.putExtra("OrderStatus", "OrderFail");
                    broadcastIntent.setAction(".OrderStatus");
                    sendBroadcast(broadcastIntent);
                }else{
                    broadcastIntent.putExtra("OrderStatus", "PartlyAvaliable");
                    broadcastIntent.setAction(".OrderStatus");
                    sendBroadcast(broadcastIntent);
                }

            }
            Log.d(TAG, "currentorder " + currentorder);
            Log.d(TAG, "currentCustomer " + currentCustomer);


//            InventoryList kitchenInventory = InventoryList.getInstance();


//            Log.d(TAG, "AT foodorderserver received username " + currentCustomer.getUserName());
//            Log.d(TAG, "AT foodorderserver received username " + currentCustomer.getPassWord());
//            Log.d(TAG, "AT foodorderserver received username " + currentCustomer.getOrderList().getOrderArrayList().get(0).getOrderItemQuantity());



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
        else if (Objects.equals(actionreceived, "ViewOrderDetail")) {
            Log.d(TAG, "ViewOrderDetail");
            Bundle b = intent.getExtras();
            Order currentorder = (Order) b.get("DetailOrderObject");
            Log.d(TAG, "currentorder " + currentorder);
            Log.d(TAG, "currentCustomer " + currentCustomer);

            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra("SignUpStatus", "SignUpFailed");
            broadcastIntent.setAction(".SignUpStatus");
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
