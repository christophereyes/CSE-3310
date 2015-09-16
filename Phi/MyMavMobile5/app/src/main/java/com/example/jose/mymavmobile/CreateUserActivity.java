package com.example.jose.mymavmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateUserActivity extends LoginActivity {

    private Button Cancel;
    private Button CreateUser;
    private MyMavDataSource mymav;

    private EditText inputUserName;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPassword;
    private EditText reinputPassword;
    private EditText inputEmail;

    private static int USERNAME_MIN_LENGTH = 3;
    private static int PASSWORD_MIN_LENGTH = 8;
    private static int EMAIL_MIN_LENGTH = 16;
    private static String MAV_EMAIL = "@mavs.uta.edu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        //find view inputs
        inputUserName = (EditText) findViewById(R.id.createNewUsername);
        inputFirstName = (EditText) findViewById(R.id.createFirstName);
        inputLastName = (EditText) findViewById(R.id.createLastName);
        inputPassword = (EditText) findViewById(R.id.createNewPassword);
        reinputPassword = (EditText) findViewById(R.id.createNewPasswordCheck);
        inputEmail = (EditText) findViewById(R.id.createNewEmail);

        //find input buttons
        Cancel = (Button) findViewById(R.id.cancel_button);
        CreateUser = (Button) findViewById(R.id.create_user_button);

        // Call for backthread to keep database open
        mymav = new MyMavDataSource(this);
        mymav.open();

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset errors.
                inputUserName.setError(null);
                inputPassword.setError(null);
                inputEmail.setError(null);

                String username = inputUserName.getText().toString();
                String firstname = inputFirstName.getText().toString();
                String lastname = inputLastName.getText().toString();
                String fullname = firstname + " " + lastname;
                String newpassword = inputPassword.getText().toString();
                String renewpassword = reinputPassword.getText().toString();
                String email = inputEmail.getText().toString();

                //check internet connection
                checkInternetConnection();

                //Creates new user if inputs are valid
                if (validCreateNewUser(username, firstname, lastname, newpassword, renewpassword, email)) {
                    //password and retype password match
                    if (newpassword.equals(renewpassword)) {
                        try{
                            mymav.createUser(username, fullname, newpassword, renewpassword, email);
                            Intent useradded = new Intent(CreateUserActivity.this, MainMenuActivity.class);
                            finish();
                            startActivity(useradded);

                        } catch (Exception e){
                            dialogbox(R.string.error_duplicate_user);
                        }
                    } else {
                        dialogbox(R.string.error_mismatch_passwords);
                    }
                } else {
                    dialogbox(R.string.error_incorrect_user_credentials);
                }
            }
        });
    }

    private boolean validCreateNewUser(String username, String firstname, String lastname, String password, String repassword, String email) {
        boolean valid = true;

        if (!isUserNameValid(username))
            valid = false;
        if (!isUserFullNameValid(firstname, lastname))
            valid = false;
        if (!isPasswordValid(password))
            valid = false;
        if (!isReEnterPasswordValid(repassword))
            valid = false;
        if (!isEmailValid(email))
            return false;


        return valid;
    }

    public boolean isUserNameValid(String username) {

        if (username.isEmpty()) {
            inputUserName.setError(getString(R.string.error_field_required));
            return false;
        } else if (username.length() < USERNAME_MIN_LENGTH) {
            inputUserName.setError(getString(R.string.error_short_username));
            return false;
        }
        return true;
    }

    public boolean isUserFullNameValid(String first, String last) {
        boolean valid = true;
        if (first.isEmpty()) {
            inputFirstName.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if (last.isEmpty()) {
            inputLastName.setError(getString(R.string.error_field_required));
            valid = false;
        }
        return valid;
    }

    public boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            inputEmail.setError(getString(R.string.error_field_required));
            return false;
        } else if (!email.contains("@")) {
            inputEmail.setError("Missing @");
            return false;
        } else if (email.length() < EMAIL_MIN_LENGTH || !email.contains(MAV_EMAIL)) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }

    public boolean isPasswordValid(String password) {
        if (password.isEmpty()) {
            inputPassword.setError(getString(R.string.error_field_required));
            return false;
        } else if (password.length() < PASSWORD_MIN_LENGTH) {
            inputPassword.setError(getString(R.string.error_short_password));
            return false;
        }
        return true;
    }

    public boolean isReEnterPasswordValid(String password) {
        if (password.isEmpty()) {
            reinputPassword.setError(getString(R.string.error_field_required));
            return false;
        } else if (password.length() < PASSWORD_MIN_LENGTH) {
            reinputPassword.setError(getString(R.string.error_short_password));
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
    protected void onStop() {
        mymav.close();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mymav.close();
        super.onDestroy();
    }
}
