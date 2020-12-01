package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
//import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import com.google.android.material.snackbar.Snackbar;
/*
Group Members:
    Michael Child-Wynne-Jones
    Morgenne Besenschek
    James Robertson
    Amardeep Sarang
    Asad Abbas
 */
public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private ProgressDialog mLoadingBar;

    EditText inputUsernameMaster;
    EditText inputPasswordMaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        inputUsernameMaster = findViewById(R.id.editTextEmailAddress);
        inputPasswordMaster = findViewById(R.id.editTextPassword);

        // initialize_auth
        //----------------------------------
        mAuth = FirebaseAuth.getInstance();
        //----------------------------------

        mLoadingBar = new ProgressDialog(LoginActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void login(View view) {
        /**
         -------------------------------------------------------
         Verifies a users email and password in the Firebase
         auth.
         -------------------------------------------------------
         Parameters:
         View view - a view
         -------------------------------------------------------
         */
        String usernameMaster = inputUsernameMaster.getText().toString();
        String passwordMaster = inputPasswordMaster.getText().toString();

        //TODO check here for password and username entry issues, maybe write a function to do this.
        // - check that the usernameMaster and passwordMaster are not empty
        // - the usernameMaster has to look like an email user@emaildomain.something
        // - password has to be length of 7 char or greater (ex 1234567, abcdefg, 1234abc)

        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Please wait, while we verify your login");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.signInWithEmailAndPassword(usernameMaster, passwordMaster).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AccountListActivity.class);
                    // so you cannot go back to login screen
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    // This hides the keyboard in the case it is left open and they click the login button
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    inputUsernameMaster.setText("");
                    inputPasswordMaster.setText("");
                    mLoadingBar.dismiss();
                }
            }
        });
    }

    public void createAccount(View view) {
        /**
         -------------------------------------------------------
         Creates a users in the Firebase auth based on the
         entered email and password.
         -------------------------------------------------------
         Parameters:
         View view - a view
         -------------------------------------------------------
         */
        String usernameMaster = inputUsernameMaster.getText().toString();
        String passwordMaster = inputPasswordMaster.getText().toString();

        //TODO check here for password and username entry issues, maybe write a function to do this.
        // - check that the usernameMaster and passwordMaster are not empty
        // - the usernameMaster has to look like an email user@emaildomain.something
        // - password has to be length of 7 char or greater (ex 1234567, abcdefg, 1234abc)

        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Please wait, while we create your account");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.createUserWithEmailAndPassword(usernameMaster, passwordMaster).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Your account was created", Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AccountListActivity.class);
                    // so you cannot go back to login screen
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // This hides the keyboard in the case it is left open and they click the register button
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    inputUsernameMaster.setText("");
                    inputPasswordMaster.setText("");
                    mLoadingBar.dismiss();
                }
            }
        });
    }
}