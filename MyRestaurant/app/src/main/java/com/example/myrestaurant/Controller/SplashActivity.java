package com.example.myrestaurant.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.myrestaurant.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(ifInOpenHour())
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Next-Activity. */

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    //finish();

                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else {
            Intent intent = new Intent(SplashActivity.this, ClosedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }



    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if(ifInOpenHour())
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Next-Activity. */

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    //finish();

                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else {
            Intent intent = new Intent(SplashActivity.this, ClosedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }

    private boolean ifInOpenHour()
    {
        SimpleDateFormat formatters = new SimpleDateFormat("HHmm");
        Date curDates = new Date(System.currentTimeMillis());
        String strs = formatters.format(curDates);
        int curDateInt=Integer.parseInt(strs);
        System.out.println(strs);

        int sth = 100;
        int eth = 2400;//hour
        if(sth<=curDateInt&&eth>=curDateInt)
        {return true;}
        else return false;

    }

}
