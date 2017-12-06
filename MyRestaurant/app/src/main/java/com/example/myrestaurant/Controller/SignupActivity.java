package com.example.myrestaurant.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrestaurant.R;

/**
 * Created by User on 2017/11/18.
 */

public class SignupActivity extends AppCompatActivity {
    static SignupActivity instance;
    EditText editTextUsername, editTextPassword;
    private TextView signintext;
    String getUsername, getPassword;
    private MyReceiver receiver=null;

    Button buttonSubmit;

    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        editTextUsername = (EditText) findViewById(R.id.TextUsername);
        editTextPassword = (EditText) findViewById(R.id.TextPassword);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                getUsername = editTextUsername.getText().toString();
                getPassword = editTextPassword.getText().toString();
                if(getUsername.equals("")||getPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Login Fail! Please input Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                new  Thread(new Runnable() {
                    @Override
                    public void run() {

                Log.d(TAG, "GetUsername: "+getUsername);
                Log.d(TAG, "GetPassword: "+getPassword);
                Intent intent = new Intent(SignupActivity.this, BackgroundService.class);
                Log.d(TAG, "Just before intent sending");
                intent.putExtra(BackgroundService.actiontodo, "SignUp");
                intent.putExtra("Username", getUsername);
                intent.putExtra("Password", getPassword);
                startService(intent);

                receiver=new MyReceiver();
                IntentFilter filter=new IntentFilter();

                filter.addAction(".SignUpStatus");
                SignupActivity.this.registerReceiver(receiver,filter);
                    }
                }).start();
                // TODO Auto-generated method stub


            }
        });

        signintext = (TextView) findViewById(R.id.SignupText);

        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        String result= bundle.getString("SignUpStatus");
        String username = bundle.getString("username");
        Log.d(TAG, "In BroadcastReceiver : onReceive: "+ result);
        if(result.equals("SignUpSuccess"))
        {

            Toast toast=Toast.makeText(getApplicationContext(), "Sign Up Successful!", Toast.LENGTH_SHORT);
            toast.show();
            Intent signtoMenu =new Intent(SignupActivity.this,MenuActivity.class);
            signtoMenu.putExtra("username", username);
            startActivity(signtoMenu);

        }
        else if(result.equals("SignUpFailed"))
        {
            Toast toast=Toast.makeText(getApplicationContext(), "Sign Up Fail! User name already exists! Pick another one!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

}