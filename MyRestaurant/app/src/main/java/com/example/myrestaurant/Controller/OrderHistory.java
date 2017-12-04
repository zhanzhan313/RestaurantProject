package com.example.myrestaurant.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.Model.CustomerList;
import com.example.myrestaurant.Model.Order;
import com.example.myrestaurant.R;

import java.util.List;
import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {
    private static final String TAG = "OrderHistory";
    private MyReceiver receiver=null;
    private ArrayAdapter<String> adapter;
private  ArrayList<String> data=new ArrayList<>();
    private ArrayList<Order> orders;
    CustomerList customerlist=CustomerList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderhistory);



        Intent intent = new Intent(OrderHistory.this, BackgroundService.class);
        Log.d(TAG, "Just before intent sending");
        intent.putExtra(BackgroundService.actiontodo, "ViewOrderHistory");
        startService(intent);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);

        receiver= new MyReceiver();
        IntentFilter filter=new IntentFilter();

        filter.addAction(".BackOrderHistory");
        this.registerReceiver(receiver,filter);
    }



    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String name = bundle.getString("BackOrderHistory");
            Log.d(TAG, "In BroadcastReceiver : customer: "+ name);
//            orders.clear();
            Customer customer=customerlist.getCustomer(name);

            orders=customer.getOrderList().getOrderArrayList();
            Log.d(TAG, "In BroadcastReceiver : orders: "+ orders);
//            adapter.notifyDataSetChanged();
            int i=0;

                    for(Order order:orders)
                    {
                        data.add(String.valueOf( order.getOrderPlacedTime() +"\n"+
                                order.getOrderId() +"\n"+
                                "Burgers "+order.getOrderItemQuantity().get(0)+"\n"+
                                "French Fries "+order.getOrderItemQuantity().get(1)+
                                "Chickens "+order.getOrderItemQuantity().get(2)+
                                "Onion Rings "+order.getOrderItemQuantity().get(3)

                        ));
                        i++;
                    }
            ListView listview=(ListView)findViewById(R.id.list_item);
            listview.setAdapter(adapter);
        }
    }
}
