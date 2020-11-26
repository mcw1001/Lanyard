package com.cp470.lanyard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

public class AccountItemCreator extends AppCompatActivity {
    public static final String ACTIVITY_NAME="AccountItemCreator";

//    protected AccountItem account;
    Button genPassword;
    ImageButton genSettings;
    Button cancel;
    Button ok;
    EditText titleText;
    EditText username;
    EditText password;
    String settingPrefsFileName;


    private Boolean upperCheck;
    private Boolean lowerCheck;
    private Boolean numCheck;
    private Boolean symbolCheck;
    private int passwordSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_item_creator);
        genPassword = findViewById(R.id.passGenButton);
        genSettings = findViewById(R.id.genSettingsButton);
        cancel = findViewById(R.id.cancelButton);
        ok = findViewById(R.id.okButton);

        titleText = findViewById(R.id.textAccountTitle);
        username = findViewById(R.id.textUsername);
        password = findViewById(R.id.textPassword);
        settingPrefsFileName = getString(R.string.settingPrefsName);
        pullPrefs();
    }

    public void pullPrefs(){
        SharedPreferences accountPrefs = getSharedPreferences(settingPrefsFileName, MODE_PRIVATE);
        upperCheck=accountPrefs.getBoolean("upperCheck",false);
        lowerCheck=accountPrefs.getBoolean("lowerCheck",true);
        numCheck=accountPrefs.getBoolean("numCheck",true);
        symbolCheck=accountPrefs.getBoolean("symbolCheck",false);
        passwordSize=accountPrefs.getInt("passwordSize",10);
//        Log.i(ACTIVITY_NAME,"pullPrefs: upperCheck:"+upperCheck+" lowerCheck:"+lowerCheck+" symbolCheck:"+symbolCheck+" numCheck:"+numCheck+" passwordSize:"+passwordSize);
    }

    public void onOK(View view){
        String title = titleText.getText().toString();
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if(title.length()<1){
            Snackbar.make(view, getResources().getString(R.string.emptyTitleWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else if (user.length()<1){
            Snackbar.make(view, getResources().getString(R.string.emptyUserWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else if (pass.length()<=1){
            Snackbar.make(view, getResources().getString(R.string.emptyPassWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else{
            AccountItem account = new AccountItem(R.drawable.placeholder,title,user,pass);
            Intent data = new Intent();
            data.putExtra("NewAccountItem", account);
            setResult(10,data);
//            Log.i(ACTIVITY_NAME,"new account, Title="+title+" user="+user+" pass="+pass);
            finish();

        }
    }
    public void onCancel(View view){
        finish();//nothing sent, simply go back to main
    }

    public void onGenerateClicked(View view){
        pullPrefs();
        String p = PasswordGenerator.generate(upperCheck,lowerCheck,symbolCheck,numCheck,passwordSize);
        password.setText(p);
    }

    public void onSettingsClicked(View view){
        //either inflate settings or do an additional transition to settings, used sharedprefs?
        Intent intent = new Intent(AccountItemCreator.this,GenerationSettingsActivity.class);
        startActivity(intent);
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