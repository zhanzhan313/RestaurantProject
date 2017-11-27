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

import com.example.myrestaurant.Model.Customer;
import com.example.myrestaurant.R;

/**
 * Created by User on 2017/11/18.
 */

public class SignupActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    private TextView signintext;
    String GetUsername, GetPassword;
    private MyReceiver receiver=null;

    Button buttonSubmit;

    String DataParseUrl = "http://chakusoza.com/devs/ucu/signup.php";
    private static final String TAG = "SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        editTextUsername = (EditText) findViewById(R.id.TextUsername);
        editTextPassword = (EditText) findViewById(R.id.TextPassword);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                GetUsername = editTextUsername.getText().toString();
                GetPassword = editTextPassword.getText().toString();

                Log.d(TAG, "GetUsername: "+GetUsername);
                Log.d(TAG, "GetPassword: "+GetPassword);
                Intent intent = new Intent(SignupActivity.this, SignUpService.class);
//                intent.putExtra("CUSTOMER",customer);
//                Intent intent = new Intent(SignupActivity.this, FoodOrderServer.class);
                intent.putExtra("ServerObject", new SignupLogin(GetUsername,GetPassword,"SignUpsuccessfully"));
                startService(intent);
//                intent.putExtra("USERNAME",GetUsername);
//                intent.putExtra("USERPASS",GetPassword);
                startService(intent);

                receiver=new MyReceiver();
                IntentFilter filter=new IntentFilter();

                filter.addAction(".SignUpService");
                SignupActivity.this.registerReceiver(receiver,filter);
                //SendDataToServer(GetUsername, GetPassword);

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

////    private static final String TAG = "SignupActivity";
//    private BroadcastReceiver receiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "onReceive");
//            Bundle bundle=intent.getExtras();
//           String result= bundle.getString("RESULT");
//            Log.d(TAG, "onReceive: "+result);
//        }
//    };
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        String result= bundle.getString("RESULT");
        Log.d(TAG, "onReceive: "+result);
        if(result.equals("SignUpSuccessfully"))
        {

            Toast toast=Toast.makeText(getApplicationContext(), "Sign Up Successfully!", Toast.LENGTH_SHORT);
            toast.show();
            Intent signtoMenu =new Intent(SignupActivity.this,MenuActivity.class);
            startActivity(signtoMenu);
        }
        else if(result.equals("SignUpFail"))
        {
            Toast toast=Toast.makeText(getApplicationContext(), "Sign Up Fail! User name has been occupied1", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

}