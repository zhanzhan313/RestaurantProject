package com.example.myrestaurant.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myrestaurant.Model.Order;
import com.example.myrestaurant.R;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {
    TextView priceText_1, priceText_2,priceText_3,priceText_4, totalPriceText, orderStatus;
    private static final String TAG = "OrderDetails";
    private  TextView quantityText_1, quantityText_2, quantityText_3, quantityText_4;
    private int quantity_1 = 0, quantity_2 = 0,quantity_3 =0,quantity_4 =0, totalItems=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Intent intent=super.getIntent();
        Bundle b = intent.getExtras();
        Order currentorder = (Order) b.get("DetailOrderObject");
        ArrayList<Integer> orderNums= currentorder.getOrderItemQuantity();
        Log.d(TAG, "onCreate: "+currentorder);
        Log.d(TAG, "onCreate: "+currentorder.getOrderPlacedTime());

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




        priceText_1.setText("$"+ MenuActivity.price_1);
        priceText_2.setText("$"+ MenuActivity.price_2);
        priceText_3.setText("$"+ MenuActivity.price_3);
        priceText_4.setText("$"+ MenuActivity.price_4);

        totalPriceText = (TextView) findViewById(R.id.Viewtotal_price);
        totalPriceText.setText("$" + currentorder.getOrderTotal());
        orderStatus.setText(currentorder.getStatus());

        Log.d(TAG, "Information Updated ");
    }
}
