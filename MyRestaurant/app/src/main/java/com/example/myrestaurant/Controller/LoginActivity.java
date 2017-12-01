package com.example.myrestaurant.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    public static final String USER_NAME = "USERNAME";
    private static final String TAG = "LoginActivity";
    private LoginActivity.MyReceiver receiver = null;
    String username;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent intent = new Intent(LoginActivity.this, FoodOrderServer.class);

                Log.d(TAG, "Just before intent sending");
                intent.putExtra(FoodOrderServer.actiontodo, "Login");
                intent.putExtra("ServerObject", new SignupLogin(username,password,"Login"));
                startService(intent);

                receiver = new MyReceiver();
                IntentFilter filter=new IntentFilter();

                filter.addAction(".FoodOrderServer");
                LoginActivity.this.registerReceiver(receiver,filter);
            }
        });

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String result= bundle.getString("LoginStatus");
            String username = bundle.getString("username");
            Log.d(TAG, "In BroadcastReceiver : result: "+ result + " and username " + username);
            if(result.equals("LoginSucessful"))
            {

                Toast toast=Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT);
                toast.show();
                Intent signtoMenu = new Intent(LoginActivity.this,MenuActivity.class);
                signtoMenu.putExtra("username", username);
                Log.d(TAG, "Added username to intent and sending to Menu page");
                startActivity(signtoMenu);
            }
            else if(result.equals("WrongPassword"))
            {
                Toast toast=Toast.makeText(getApplicationContext(), "Login Fail! Please enter right password!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(result.equals("NorecordFound"))
            {
                Toast toast=Toast.makeText(getApplicationContext(), "Login Fail! No username found, please sign up first", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

