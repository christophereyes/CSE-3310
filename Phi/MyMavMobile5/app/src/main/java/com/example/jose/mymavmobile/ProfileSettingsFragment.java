package com.example.jose.mymavmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ProfileSettingsFragment extends Fragment{

    private MyMavDataSource mymav;
    private View rootView;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextCurrentPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstantState){
        rootView = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        //Call for backthread to keep database open
        mymav = new MyMavDataSource(rootView.getContext());
        mymav.open();

        //Find inputs for change Full Name
        editTextFirstName = (EditText) rootView.findViewById(R.id.inputFirstName);
        editTextLastName = (EditText) rootView.findViewById(R.id.inputLastName);
        Button buttonChangeFullName = (Button) rootView.findViewById(R.id.buttonChangeFullName);

        //Finds inputs for change Password
        editTextCurrentPassword = (EditText) rootView.findViewById(R.id.currentPassword);
        editTextNewPassword = (EditText) rootView.findViewById(R.id.newPassword);
        editTextConfirmPassword = (EditText) rootView.findViewById(R.id.confirmPassword);
        Button buttonChangePassword = (Button) rootView.findViewById(R.id.buttonChangePassword);

        Button buttonDeleteUser = (Button) rootView.findViewById(R.id.buttonDeleteUser);

        //Change Full Name
        buttonChangeFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptChangeFullName();
            }

        });

        //Change Password
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptChangePassword();
            }
        });
        //Delete User
        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteUser();
            }
        });
        return rootView;
    }

    private void attemptChangeFullName() {
        boolean cancel = false;

        //Reset errors
        editTextFirstName.setError(null);
        editTextLastName.setError(null);

        //Get input values
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String mymavdataEmail = mymav.getEmail();
        String mymavdataPassword = mymav.getPassword();

        if(firstName.isEmpty()){
            editTextFirstName.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(lastName.isEmpty()){
            editTextLastName.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        //changes FULL NAME
        if(!cancel){
            Toast.makeText(rootView.getContext(), "Changed User Name", Toast.LENGTH_LONG).show();
            mymav.editUserInfo(firstName + " " + lastName, mymavdataEmail, mymavdataPassword);
        }
    }

    public void attemptChangePassword(){
        boolean cancel = false;

        //Resets input errors
        editTextCurrentPassword.setError(null);
        editTextNewPassword.setError(null);
        editTextConfirmPassword.setError(null);

        //Gets input values
        String curpassword = editTextCurrentPassword.getText().toString();
        String newpassword = editTextNewPassword.getText().toString();
        String confirmpassword = editTextConfirmPassword.getText().toString();
        String mymavdataName = mymav.getName();
        String mymavdataEmail = mymav.getEmail();
        String mymavdataPassword = mymav.getPassword();

        if(curpassword.isEmpty()) {
            editTextCurrentPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(newpassword.isEmpty()) {
            editTextNewPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(confirmpassword.isEmpty()) {
            editTextConfirmPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(!cancel){
            if (curpassword.matches(mymavdataPassword)) {
                if (newpassword.equals(confirmpassword)) {
                    Toast.makeText(rootView.getContext(), "Changed Password", Toast.LENGTH_LONG).show();
                    //changes password
                    mymav.editUserInfo(mymavdataName, mymavdataEmail, newpassword);
                }
                else{
                    dialogbox(R.string.error_mismatch_passwords);
                }
            }
            //Displays not correct password
            else{
                editTextCurrentPassword.setError(getString(R.string.error_invalid_password));
            }
        }
    }

    //displays an error message
    public void dialogbox(int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setMessage(message).setCancelable(true);
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    //Confirms delete user
    public void confirmDeleteUser(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

        builder.setMessage("Are you sure you want to delete user?")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(rootView.getContext(), "User Deleted", Toast.LENGTH_LONG).show();
                        mymav.deleteUser();
                        Intent backLogin = new Intent(rootView.getContext(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(backLogin);
                    }
                });
        builder.show();
    }

    private boolean passwordEmpty(EditText editPassword){
        String password = editPassword.getText().toString();
        if(password.isEmpty()) {
            editPassword.setError(getString(R.string.error_field_required));
            return true;
        }
        return false;
    }
}
