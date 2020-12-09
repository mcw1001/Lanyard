package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

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
    Button loginButton;
    Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        inputUsernameMaster = findViewById(R.id.editTextEmailAddress);
        inputPasswordMaster = findViewById(R.id.editTextPassword);

        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        inputUsernameMaster.addTextChangedListener(loginRegisterTextWatcher);
        inputPasswordMaster.addTextChangedListener(loginRegisterTextWatcher);


        // initialize_auth
        //----------------------------------
        mAuth = FirebaseAuth.getInstance();
        //----------------------------------

        mLoadingBar = new ProgressDialog(LoginActivity.this);
    }
    private TextWatcher loginRegisterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputUsernameMasterValue = inputUsernameMaster.getText().toString().trim();
            String inputPasswordMasterValue = inputPasswordMaster.getText().toString().trim();

            loginButton.setEnabled(!inputUsernameMasterValue.isEmpty() && !inputPasswordMasterValue.isEmpty());
            registerButton.setEnabled(!inputUsernameMasterValue.isEmpty() && !inputPasswordMasterValue.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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

        mLoadingBar.setTitle(getString(R.string.loginTitle));
        mLoadingBar.setMessage(getString(R.string.loggingIn));
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.signInWithEmailAndPassword(usernameMaster, passwordMaster).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.loggedIn), Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AccountListActivity.class);
                    // so you cannot go back to login screen
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else {
                    // This hides the keyboard in the case it is left open and they click the login button
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    String errorMessage = parseLoginError(task.getException());
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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

        mLoadingBar.setTitle(getString(R.string.loginTitle));
        mLoadingBar.setMessage(getString(R.string.creatingAccount));
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();

        mAuth.createUserWithEmailAndPassword(usernameMaster, passwordMaster).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.accountCreated), Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, AccountListActivity.class);
                    // so you cannot go back to login screen
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // This hides the keyboard in the case it is left open and they click the register button
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    String errorMessage = parseLoginError(task.getException());
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    inputUsernameMaster.setText("");
                    inputPasswordMaster.setText("");
                    mLoadingBar.dismiss();
                }
            }
        });
    }

    // Helper function to parse an auth exception from Firebase and return a fitting error message
    // If no message exists for some error code, a generic error message is returned
    private String parseLoginError(Exception e) {
        String errorMessage = getString(R.string.generic_auth_error);
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            switch(((FirebaseAuthInvalidCredentialsException) e).getErrorCode()) {
                case "ERROR_WRONG_PASSWORD":
                    errorMessage = getString(R.string.error_invalid_credentials);
                    break;
                case "ERROR_INVALID_EMAIL":
                    errorMessage = getString(R.string.error_invalid_email);
                    break;
                case "ERROR_WEAK_PASSWORD":
                    errorMessage = getString(R.string.error_weak_password);
                    break;
            }
        } else if (e instanceof FirebaseAuthInvalidUserException) {
            if (((FirebaseAuthInvalidUserException) e).getErrorCode() == "ERROR_USER_NOT_FOUND") {
                errorMessage = getString(R.string.error_user_does_not_exist);
            }
        }
        else if (e instanceof FirebaseAuthUserCollisionException) {
            if (((FirebaseAuthUserCollisionException) e).getErrorCode() == "ERROR_EMAIL_ALREADY_IN_USE") {
                errorMessage = getString(R.string.error_user_already_exists);
            }
        }
        return errorMessage;
    }
}