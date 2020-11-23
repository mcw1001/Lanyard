package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private ProgressDialog mLoadingBar;

    Button loginButton;
    Button registerButton;
    EditText inputUsernameMaster;
    EditText inputPasswordMaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"In onCreate()");

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        inputUsernameMaster = findViewById(R.id.editTextEmailAddress);
        inputPasswordMaster = findViewById(R.id.editTextPassword);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mLoadingBar = new ProgressDialog(LoginActivity.this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkCredentials();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        String usernameMaster = inputUsernameMaster.getText().toString();
        String passwordMaster = inputPasswordMaster.getText().toString();

        // check here for password and username empty

        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Please wait, while we verify your login");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.signInWithEmailAndPassword(usernameMaster, passwordMaster).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT);
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AccountListActivity.class);
                    // so you cannot go back to login screen
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void checkCredentials() {
        String usernameMaster = inputUsernameMaster.getText().toString();
        String passwordMaster = inputPasswordMaster.getText().toString();

        // check here for password and username empty

        mLoadingBar.setTitle("Login");
        mLoadingBar.setMessage("Please wait, while we create your account");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.createUserWithEmailAndPassword(usernameMaster, passwordMaster).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Your account was created", Toast.LENGTH_SHORT);
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AccountListActivity.class);
                    // so you cannot go back to login screen
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT);
                }
            }
        });
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