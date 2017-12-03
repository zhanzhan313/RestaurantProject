package com.example.myrestaurant.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Intent;
import com.example.myrestaurant.R;

/**
 * Created by User on 2017/12/2.
 */

public class OrderStatusActivity extends AppCompatActivity{

    private TextView statusText, timeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        Bundle bundle=getIntent().getExtras();
        int estimatedtime = bundle.getInt("estimatedtime");
        String status = bundle.getString("orderStatus");

        statusText = (TextView) findViewById(R.id.textView_status);
        timeText = (TextView) findViewById(R.id.textView_time);

        statusText.setText("" + status);
        timeText.setText(estimatedtime + " mins");


    }
}
