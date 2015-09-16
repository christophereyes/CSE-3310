package com.example.jose.mymavmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class EditUserActivity extends Activity {
    private MyMavDataSource mymav;

    private Button logout;
    private Button home;
    private Button delete;
    private Button submitChanges;
    private TextView nameT;
    private TextView passT;
    private TextView emailT;

    final Context context = EditUserActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_edit_user);
        mymav = new MyMavDataSource(this);
        mymav.open(); // Call for backthread to keep database open

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeScreen = new Intent(EditUserActivity.this, MainMenuActivity.class);
                startActivity(homeScreen);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtologin = new Intent(EditUserActivity.this, LoginActivity.class);
                mymav.logoutUser();
                startActivity(backtologin);
            }
        });

        submitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameT.getText().toString();
                String pass = passT.getText().toString();
                String email = emailT.getText().toString();

                mymav.editUserInfo(name, email, pass);
                dialogbox();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtologin = new Intent(EditUserActivity.this, LoginActivity.class);
                mymav.deleteUser();
                startActivity(backtologin);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_edit_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void dialogbox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Settings changed!").setCancelable(true);
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        mymav.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mymav.close();
        super.onPause();
    }

    @Override
    protected void onStop(){
        mymav.close();
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        mymav.close();
        super.onDestroy();
    }
}
