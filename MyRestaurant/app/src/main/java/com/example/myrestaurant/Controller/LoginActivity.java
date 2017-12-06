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
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private TextView signuptext;
    Button buttonLogin;
    public static final String USER_NAME = "username";
    private static final String TAG = "LoginActivity";
    private LoginActivity.MyReceiver receiver = null;
    private String username;
    String password;
    static LoginActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        instance=this;

        editTextUserName = (EditText) findViewById(R.id.TextUsername);
        editTextPassword = (EditText) findViewById(R.id.TextPassword);
        signuptext = (TextView) findViewById(R.id.SignupText);

        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                username = editTextUserName.getText().toString();
                password = editTextPassword.getText().toString();
              if(username.equals("")||password.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Login Fail! Please input Something", Toast.LENGTH_SHORT).show();
                return;
            }
                Log.d(TAG, "In Login activity : Username: "+username);
                Log.d(TAG, "In Login activity : Password: "+password);

                Intent intent = new Intent(LoginActivity.this, BackgroundService.class);
                Log.d(TAG, "Just before intent sending");
                intent.putExtra(BackgroundService.actiontodo, "Login");
                intent.putExtra("LoginUsername", username);
                intent.putExtra("LoginPassword", password);
                startService(intent);

                receiver=new LoginActivity.MyReceiver();
                IntentFilter filter=new IntentFilter();

                filter.addAction(".LoginStatus");
                LoginActivity.this.registerReceiver(receiver,filter);
            }
        });

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result= intent.getStringExtra("LoginStatus");
            Log.d(TAG, "In BroadcastReceiver : result: "+ result + " and username " + username);
            if(result.equals("LoginSuccess"))
            {
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent signtoMenu = new Intent(LoginActivity.this,MenuActivity.class);
                Log.d(TAG, "Added username to intent and sending to Menu page");
                signtoMenu.putExtra(USER_NAME, username);
                startActivity(signtoMenu);
            }
            else if(result.equals("LoginFailed"))
            {
                Toast.makeText(getApplicationContext(), "Login Fail! Please enter right password!", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("NorecordFound"))
            {
               Toast.makeText(getApplicationContext(), "Login Fail! No username found, please sign up first", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

