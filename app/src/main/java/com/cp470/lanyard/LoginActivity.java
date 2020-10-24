package com.cp470.lanyard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
//import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    static final int NO_CREDENTIALS = -2;
    static final int INVALID_USERNAME_PASSWORD = -1;
    static final int NO_STORED_CREDENTIALS=0;
    static final int VALID_CREDENTIALS=1;

    Button loginButton;
    Button registerButton;
    String loginPrefsFileName;
    EditText usernameMaster;
    EditText passwordMaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"In onCreate()");

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        loginPrefsFileName = getString(R.string.filePrefsName);
        usernameMaster = findViewById(R.id.editTextEmailAddress);
        passwordMaster = findViewById(R.id.editTextPassword);
    }

    public void onLoginClicked(View v){
        //TODO Login validation then save/load

        final String username = usernameMaster.getText().toString();
        //less secure to have string even temp store password but deal with it later
        final String password = passwordMaster.getText().toString();

        int result=validate(username,password);
        if(result==INVALID_USERNAME_PASSWORD){
            //invalid username or password
            //choosing Toast instead of snackbar to get it from the top?
            Toast toast = Toast.makeText(this,getResources().getString(R.string.invalidUserPass),Toast.LENGTH_LONG);
            toast.show();
//            Snackbar.make(v, getResources().getString(R.string.invalidUserPass),Snackbar.LENGTH_LONG)
//                    .setAction("Action",null).show();
        }else if(result==NO_CREDENTIALS){

            Toast toast = Toast.makeText(this,getResources().getString(R.string.emptyUserPassWarning),Toast.LENGTH_LONG);
            toast.show();
//            Snackbar.make(v, getResources().getString(R.string.emptyUserPassWarning),Snackbar.LENGTH_LONG)
//                    .setAction("Action",null).show();
        }else if(result==NO_STORED_CREDENTIALS){
            //new account then jump
//                Boolean newAcc=false;
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle(R.string.noUserAccountAlert);
            // Add the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    createAccount(username,password);
                    //TODO Jump to MainActivity, replace mainActivity with actual main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
//                if(newAcc){
//                createAccount(username,password);}else{
//                    return;
//                }
        }else{
            //TODO welcome message?
            //TODO jump to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //context.getSharedPreferences("YOUR_PREFS", 0).edit().clear().commit(); //remove all your prefs :)

    private int validate(String u, String p){//, View v){
        //First case, username or password entered is empty
        if(u.equals("") || p.equals("")){
            return NO_CREDENTIALS;
        }

        SharedPreferences loginPrefs = getSharedPreferences(loginPrefsFileName, MODE_PRIVATE);
//            SharedPreferences.Editor prefEdit = loginPrefs.edit();
        String storedUser = loginPrefs.getString("CurrentUsername","noUser");
        String storedPass = loginPrefs.getString("CurrentPassword","noPass");
        //no user exists, create one
        Log.d("validate Function","storedUser="+storedUser+", storedPass="+storedPass);
        assert storedUser != null;
        assert storedPass != null;//should always have a non-null return, specificalyl "noPass" and "noUser"
        if(storedUser.equals("noUser") || storedPass.equals("noPass")) {
            return NO_STORED_CREDENTIALS;
        }else if (storedUser.equals(u) && storedPass.equals(p)){ //valid credentials, allowed in
            return VALID_CREDENTIALS;
        }else{ //Invalid combo, not equal, not new, not empty
            return INVALID_USERNAME_PASSWORD;
        }
    }

    private void createAccount(String username, String password){
        SharedPreferences loginPrefs = getSharedPreferences(loginPrefsFileName, MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = loginPrefs.edit();
        prefEdit.clear();
        prefEdit.putString("CurrentUsername",username);
        prefEdit.putString("CurrentPassword",password);
        prefEdit.apply(); //using .apply() instead of .commit(), apply runs in background, commit writes immediately
    }
    public void onRegisterClicked(final View v){
        final String username = usernameMaster.getText().toString();
        //less secure to have string even temp store password but deal with it later
        final String password = passwordMaster.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.registerAlertTitle);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if(validate(username,password)>-2){
                    createAccount(username,password);
                    //TODO jump to main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(LoginActivity.this,getResources().getString(R.string.invalidUserPass),Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}