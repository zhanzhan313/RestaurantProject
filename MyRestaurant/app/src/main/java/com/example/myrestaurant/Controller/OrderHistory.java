package com.example.myrestaurant.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Button refreshBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderhistory);


        refreshBut=(Button)findViewById(R.id.refreshBut);
        refreshBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }});
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
            Log.d(TAG, "In BroadcastReceiver : customer: "+ customer);
            orders=customer.getOrderList().getOrderArrayList();
            Log.d(TAG, "In BroadcastReceiver : orders: "+ orders);
//            adapter.notifyDataSetChanged();
            int i=0;

                    for(Order order:orders)
                    {
                        data.add(String.valueOf( order.getOrderPlacedTime() +"        "+
                                "Order ID : "+order.getOrderId() +"\n"+
                                "Burgers :                                             "+order.getOrderItemQuantity().get(0)+"\n"+
                                "French Fries :                                     "+order.getOrderItemQuantity().get(1)+"\n"+
                                "Chickens :                                          "+order.getOrderItemQuantity().get(2)+"\n"+
                                "Onion Rings :                                     "+order.getOrderItemQuantity().get(3)+"\n"+
                                "----------"+order.getStatus()+"......"

                        ));
                        i++;
                    }
            ListView listview=(ListView)findViewById(R.id.list_item);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Order order=orders.get(position);
//                    Intent intent=new Intent(OrderHistory.this,BackgroundService.class);
//                    intent.putExtra(BackgroundService.actiontodo, "ViewOrderDetail");
//                    Log.d(TAG, "ViewOrderDetail " + order);
//                    intent.putExtra("DetailOrderObject", order);
//                    startService(intent);
                    Intent intent=new Intent(OrderHistory.this,OrderDetails.class);

                    Log.d(TAG, "ViewOrderDetail " + order);
                    intent.putExtra("DetailOrderObject", order);
                    startActivity(intent);
                }
            });
        }
    }
}
