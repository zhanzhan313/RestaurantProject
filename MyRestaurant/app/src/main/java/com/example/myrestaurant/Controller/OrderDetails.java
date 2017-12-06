package com.example.myrestaurant.Controller;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.Model.Order;
import com.example.myrestaurant.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OrderDetails extends AppCompatActivity {
    public static final int UPDATE_TEXTFILE=1;
    TextView priceText_1, priceText_2,priceText_3,priceText_4, totalPriceText, orderStatus;
    private static final String TAG = "OrderDetails";
    private Customer customer=BackgroundService.currentCustomer;
    private Order currentorder;
    private  TextView quantityText_1, quantityText_2, quantityText_3, quantityText_4, taxText, groundTotalText;
    private int quantity_1 = 0, quantity_2 = 0,quantity_3 =0,quantity_4 =0, totalItems=0;
    private double totalPrice=0, groundTotal=0,tax=0;

    private Handler handler=new Handler() {

        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {

                case UPDATE_TEXTFILE:
                    Log.d(TAG, "Inside msg"+currentorder.getStatus());
                    orderStatus.setText(currentorder.getStatus());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Intent intent=super.getIntent();
        Bundle b = intent.getExtras();
        Log.d(TAG, "onCreate: "+customer);
        int index=b.getInt("DetailOrderObject");
        currentorder=customer.getOrderList().getOrderArrayList().get(index);
        ArrayList<Integer> orderNums= customer.getOrderList().getOrderArrayList().get(index).getOrderItemQuantity();
        Log.d(TAG, "onCreate: "+orderNums);
//        Log.d(TAG, "onCreate: "+currentorder.getOrderPlacedTime());

        this.setTitle("Welcome " + BackgroundService.currentCustomer.getUserName() + "!");
        quantityText_1 = (TextView) findViewById(R.id.Viewquantity_1);
        quantityText_2 = (TextView) findViewById(R.id.Viewquantity_2);
        quantityText_3 = (TextView) findViewById(R.id.Viewquantity_3);
        quantityText_4 = (TextView) findViewById(R.id.Viewquantity_4);
        orderStatus=(TextView)findViewById(R.id.Vieworderstatus);

        quantityText_1.setText(""+orderNums.get(0));
        quantityText_2.setText(""+orderNums.get(1));
        quantityText_3.setText(""+orderNums.get(2));
        quantityText_4.setText(""+orderNums.get(3));

        priceText_1 = (TextView) findViewById(R.id.Viewitem_price_1);
        priceText_2 = (TextView) findViewById(R.id.Viewitem_price_2);
        priceText_3 = (TextView) findViewById(R.id.Viewitem_price_3);
        priceText_4 = (TextView) findViewById(R.id.Viewitem_price_4);

        taxText = (TextView) findViewById(R.id.tax);
        groundTotalText = (TextView) findViewById(R.id.groundTotalView);

        priceText_1.setText("$"+ String.format("%.2f",MenuActivity.price_1));
        priceText_2.setText("$"+ String.format("%.2f",MenuActivity.price_2));
        priceText_3.setText("$"+ String.format("%.2f",MenuActivity.price_3));
        priceText_4.setText("$"+ String.format("%.2f",MenuActivity.price_4));


//        priceText_1.setText("$"+ MenuActivity.price_1);
//        priceText_2.setText("$"+ MenuActivity.price_2);
//        priceText_3.setText("$"+ MenuActivity.price_3);
//        priceText_4.setText("$"+ MenuActivity.price_4);

        totalPrice = currentorder.getOrderTotal();
        tax = totalPrice * MenuActivity.TaxRate;
        groundTotal = tax + totalPrice;
        totalPriceText = (TextView) findViewById(R.id.Viewtotal_price);
        totalPriceText.setText("$" + String.format("%.2f",totalPrice));
        orderStatus.setText(currentorder.getStatus());
        taxText.setText("$" + String.format("%.2f",tax));
        groundTotalText.setText("$" +String.format("%.2f",groundTotal));


        Log.d(TAG, "Information Updated ");
        final long period = 1000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Inside timer method");
                Message message=new Message();
                message.what=UPDATE_TEXTFILE;
                handler.sendMessage(message);
            }
        }, 0, period);
    }
}
