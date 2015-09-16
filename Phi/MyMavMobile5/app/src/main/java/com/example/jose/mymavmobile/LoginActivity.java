package com.example.jose.mymavmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {
    private AutoCompleteTextView inputUserIDView;
    private EditText inputPasswordView;
    private Button createUserButton;
    private MyMavDataSource mymav;
    private boolean cancel = false;
    private final String TAG = "TKT";
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate");

        //customize actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.mymav_titlebar_blue_bg));

        //Call for backthread to keep database open
        mymav = new MyMavDataSource(this);
        mymav.open();

        // Set up the login form.
        inputUserIDView = (AutoCompleteTextView) findViewById(R.id.userid);
        inputPasswordView = (EditText) findViewById(R.id.password);
        Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
        createUserButton = (Button) findViewById(R.id.create_new_user);

        //CHECKS FOR AN INTERNET CONNECTION
//        checkInternetConnection();

        //login functionality
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        //create a new user
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newuser = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(newuser);
            }
        });
    }

    //loops message to connect to a network
    public void checkInternetConnection() {
        if (!isOnline(this)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Must have a network connection. Connect to a network and try again.")
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            if (isOnline(getApplicationContext())) {
                                Toast.makeText(getBaseContext(), "Network Connection Found", Toast.LENGTH_SHORT).show();
                            } else
                                checkInternetConnection();
                        }
                    });
            builder.show();
        }
    }

    //check for an internet/network connection
    public static boolean isOnline(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Pressing Phones Back button
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            finish();
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit.", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        inputUserIDView.setText("");
        inputPasswordView.setText("");
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //displays an alert message w/ back button
    public void dialogbox(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(true);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    public void attemptLogin() {
        // Reset errors.
        inputUserIDView.setError(null);
        inputPasswordView.setError(null);
        cancel = false;

        // Store values at the time of the login attempt.
        String user_id = inputUserIDView.getText().toString();
        String password = inputPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            inputPasswordView.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        // Check for a valid user_id address.
        if (TextUtils.isEmpty(user_id)) {
            inputUserIDView.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        //Have right requirements for user_id and password
        if (!cancel) {
            //verify user_id and password in database
            try {
                if (mymav.loginUser(user_id, password)) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent newLogin = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(newLogin);

                }
            } catch (Exception e) {
                dialogbox(R.string.error_mismatch_username_password);
            }
        }
    }
}
