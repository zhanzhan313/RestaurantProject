package com.example.myrestaurant.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrestaurant.Model.Order;
import com.example.myrestaurant.R;

import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity {

    EditText quantityText_1, quantityText_2, quantityText_3, quantityText_4;
    ImageView buttonAdd_1, buttonAdd_2, buttonAdd_3,buttonAdd_4;
    ImageView buttonRemove_1,buttonRemove_2,buttonRemove_3,buttonRemove_4;
    private int quantity_1 = 0, quantity_2 = 0,quantity_3 =0,quantity_4 =0, totalItems=0;
    private double price_1 =5.5, price_2 = 6.00, price_3= 2.00, price_4=2.5,totalPrice=0;  //
    TextView priceText_1, priceText_2,priceText_3,priceText_4, totalPriceText, totalItemsText;
    private static final String TAG = "MenuActivity";
    private static String userName = "";
    private MenuActivity.MyOrderReceiver receiver = null;

    Button placeOrderbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        userName = getIntent().getStringExtra("username");

        Log.d(TAG, "Received username at MenuActivity from LoginActivity " + userName);

        priceText_1 = (TextView) findViewById(R.id.item_price_1);
        priceText_2 = (TextView) findViewById(R.id.item_price_2);
        priceText_3 = (TextView) findViewById(R.id.item_price_3);
        priceText_4 = (TextView) findViewById(R.id.item_price_4);

        totalPriceText = (TextView) findViewById(R.id.total_price);
        totalItemsText = (TextView) findViewById(R.id.total_item);

        quantityText_1 = (EditText) findViewById(R.id.quantity_1);
        quantityText_2 = (EditText) findViewById(R.id.quantity_2);
        quantityText_3 = (EditText) findViewById(R.id.quantity_3);
        quantityText_4 = (EditText) findViewById(R.id.quantity_4);

        priceText_1.setText("$"+ price_1);
        priceText_2.setText("$"+ price_2);
        priceText_3.setText("$"+ price_3);
        priceText_4.setText("$"+ price_4);


        quantityText_1.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
                quantity_1 = Integer.parseInt(quantityText_1.getText().toString());
                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        quantityText_2.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
                quantity_2 = Integer.parseInt(quantityText_2.getText().toString());
                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        quantityText_3.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
                quantity_3 = Integer.parseInt(quantityText_3.getText().toString());
                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        quantityText_4.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable s) {
                quantity_4 = Integer.parseInt(quantityText_4.getText().toString());
                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        buttonAdd_1 = (ImageView) findViewById(R.id.item_add_1);
        buttonAdd_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_1 ++;
                quantityText_1.setText(""+ quantity_1);

                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
        });

        buttonRemove_1 = (ImageView) findViewById(R.id.item_remove_1);
        buttonRemove_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_1 --;
                if (quantity_1 < 0){
                    quantity_1 =0 ;
                }
                quantityText_1.setText(""+quantity_1);

                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
        });


        buttonAdd_2 = (ImageView) findViewById(R.id.item_add_2);
        buttonAdd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_2 ++;
                quantityText_2.setText(""+ quantity_2);

                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
        });

        buttonRemove_2 = (ImageView) findViewById(R.id.item_remove_2);
        buttonRemove_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_2 --;
                if (quantity_2 < 0){
                    quantity_2 =0 ;
                }
                quantityText_2.setText(""+quantity_2);

                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
        });

        buttonAdd_3 = (ImageView) findViewById(R.id.item_add_3);
        buttonAdd_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_3 ++;
                quantityText_3.setText(""+ quantity_3);

                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);
            }
        });

        buttonRemove_3 = (ImageView) findViewById(R.id.item_remove_3);
        buttonRemove_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_3 --;
                if (quantity_3 < 0){
                    quantity_3 =0 ;
                }

                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);

                quantityText_3.setText(""+quantity_3);
            }
        });

        buttonAdd_4 = (ImageView) findViewById(R.id.item_add_4);
        buttonAdd_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_4 ++;
                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);

                quantityText_4.setText(""+ quantity_4);
            }
        });

        buttonRemove_4 = (ImageView) findViewById(R.id.item_remove_4);
        buttonRemove_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_4 --;
                if (quantity_4 < 0){
                    quantity_4 =0 ;
                }
                totalItems = quantity_1 + quantity_2 +quantity_3 + quantity_4;
                totalPrice = quantity_1 * price_1 + quantity_2 * price_2 + quantity_3 * price_3 + quantity_4 * price_4;
                totalPriceText.setText("$" + totalPrice);
                totalItemsText.setText("" + totalItems);

                quantityText_4.setText(""+quantity_4);
            }
        });


        placeOrderbutton=(Button) findViewById(R.id.placeOrderbutton);

        placeOrderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "placeOrderbutton,Just before intent sending");

                 /*
                   orderItemQuantity[0] - Indicates quantity for Burger.
                   orderItemQuantity[1] - Indicates quantity for Chicken.
                   orderItemQuantity[2] - Indicates quantity for FrenchFries.
                   orderItemQuantity[3] - Indicates quantity for OnionRings.
                  */

                 /* ToDO This code below crashes if all 4 items are not updated in Menu UI */
                int quantity1=Integer.parseInt(quantityText_1.getText().toString());
                Log.d(TAG, "Here after quantity 1");
                int quantity2=Integer.parseInt(quantityText_2.getText().toString());
                Log.d(TAG, "Here after quantity 2");
                int quantity3=Integer.parseInt(quantityText_3.getText().toString());
                Log.d(TAG, "Here after quantity 3");
                int quantity4=Integer.parseInt(quantityText_4.getText().toString());
                Log.d(TAG, "Here after quantity 4");
                Log.d(TAG, "Here after quantity declared");
                //int [] foodquantity=new int[]{quantity1,quantity2,quantity3,quantity4};
                ArrayList<Integer> foodquantity = new ArrayList<Integer>();
                foodquantity.add(quantity1);
                foodquantity.add(quantity2);
                foodquantity.add(quantity3);
                foodquantity.add(quantity4);

                int count = 0;
                for(int temp: foodquantity){
                    if (count == 10){
                        break;
                    }
                    Log.d(TAG, "Food quantity = " + temp);
                    count ++;
                }

                Intent intent = new Intent(MenuActivity.this, FoodOrderServer.class);
                Log.d(TAG, "Just before intent sending");
                Log.d(TAG, "Just before intent sending");
                Log.d(TAG, "totalPrice"+totalPrice);

                intent.putExtra(FoodOrderServer.actiontodo, "OrderPlacement");
                Log.d(TAG, "Passing username now to Food order server with Order, userName " + userName);
                intent.putExtra("OrderObject", new Order(userName, foodquantity));
                startService(intent);

                receiver = new MenuActivity.MyOrderReceiver();
                IntentFilter filter = new IntentFilter();

                filter.addAction(".OrderStatus");
                MenuActivity.this.registerReceiver(receiver,filter);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.orderhistory:
                Toast.makeText(this,"Navigate to order page",Toast.LENGTH_SHORT).show();
//                Intent intent= new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://www.baidu.com"));
//                startActivity(intent);
                break;
            case R.id.orderfood:
                Toast.makeText(this,"Navigate to order history page",Toast.LENGTH_SHORT).show();

        }
        return  true;
    }

    public class MyOrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "In BroadcastReceiver ");
            Bundle bundle=intent.getExtras();
            String test = bundle.getString("teststring");
            Boolean orderplaced = bundle.getBoolean("isorderplaced");
            int estimatedtime = bundle.getInt("estimatedtime");
            Log.d(TAG, "test = " + test);
            Log.d(TAG, "In BroadcastReceiver : orderplaced: "+ orderplaced + " and estimatedtime " + estimatedtime);
            if(orderplaced == true)
            {
                Toast toast=Toast.makeText(getApplicationContext(), "Order Successful! Estimated time = " + estimatedtime + " mins", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Toast toast=Toast.makeText(getApplicationContext(), "Order Fail!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
