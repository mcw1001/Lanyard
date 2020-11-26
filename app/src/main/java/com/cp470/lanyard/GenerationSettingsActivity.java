package com.cp470.lanyard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class GenerationSettingsActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME="GenerationSettingsActivity";
    TextView passwordLength;
    TextView examplePassword;
    String passCharSet;
    CheckBox uppers;
    CheckBox lowers;
    CheckBox nums;
    CheckBox symbols;
    Boolean upperCheck;
    Boolean lowerCheck;
    Boolean numCheck;
    Boolean symbolCheck;
    int passwordSize;
    SeekBar passwordLengthBar;

    String settingPrefsFileName;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generation_settings);

        uppers = findViewById(R.id.upperCheck);
        upperCheck=uppers.isChecked();
        lowers = findViewById(R.id.lowerCheck);
        lowerCheck=lowers.isChecked();
        nums =findViewById(R.id.numberCheck);
        numCheck=nums.isChecked();
        symbols = findViewById(R.id.symbolCheck);
        symbolCheck=symbols.isChecked();

        passwordLengthBar = findViewById(R.id.passwordLengthSeekBar);
        passwordLengthBar.setOnSeekBarChangeListener(seekBarChangeListener);
        int progress = passwordLengthBar.getProgress();
        passwordSize = progress;
        passwordLength = findViewById(R.id.textPasswordLength);
        passwordLength.setText(getString(R.string.passwordSize)+": "+progress);
        examplePassword = findViewById(R.id.textExamplePassword);
//        updateExample();

        settingPrefsFileName = getString(R.string.settingPrefsName);
        pullPrefs();
    }

    public void pullPrefs(){
        SharedPreferences accountPrefs = getSharedPreferences(settingPrefsFileName, MODE_PRIVATE);
        upperCheck=accountPrefs.getBoolean("upperCheck",false);
        uppers.setChecked(upperCheck);
        lowerCheck=accountPrefs.getBoolean("lowerCheck",true);
        lowers.setChecked(lowerCheck);
        numCheck=accountPrefs.getBoolean("numCheck",true);
        nums.setChecked(numCheck);
        symbolCheck=accountPrefs.getBoolean("symbolCheck",false);
        symbols.setChecked(symbolCheck);
        passwordSize=accountPrefs.getInt("passwordSize",10);
        passwordLengthBar.setProgress(passwordSize);
        updateExample();
//        Log.i(ACTIVITY_NAME,"pullPrefs: upperCheck:"+upperCheck+" lowerCheck:"+lowerCheck+" symbolCheck:"+symbolCheck+" numCheck:"+numCheck+" passwordSize:"+passwordSize);
    }
    public void onUpperCheck(View v){
        upperCheck = ((CheckBox) v).isChecked();
        checkAllChecks(v);
    }
    public void onLowerCheck(View v){
        lowerCheck= ((CheckBox) v).isChecked();
        checkAllChecks(v);
    }
    public void onNumCheck(View v){
        numCheck= ((CheckBox) v).isChecked();
        checkAllChecks(v);
    }
    public void onSymbolCheck(View v){
        symbolCheck= ((CheckBox) v).isChecked();
        checkAllChecks(v);
    }
    public void checkAllChecks(View v){
        if(!upperCheck && !lowerCheck && !symbolCheck && !numCheck){
            Snackbar.make(v, getResources().getString(R.string.noChecksWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else{
            updateExample();
        }
    }

    public void updateExample(){
        String s = PasswordGenerator.generate(upperCheck,lowerCheck,symbolCheck,numCheck,passwordSize);
//        Log.i(ACTIVITY_NAME,"upperCheck:"+upperCheck+" symbolCheck:"+symbolCheck+" numCheck:"+numCheck+" passwordSize:"+passwordSize);
        examplePassword.setText(s);
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            passwordSize=progress;
            passwordLength.setText(getString(R.string.passwordSize)+": "+passwordSize);
            updateExample();
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };
    public void okClicked(View v){
        if(!upperCheck && !lowerCheck && !symbolCheck && !numCheck){
            Snackbar.make(v, getResources().getString(R.string.noChecksWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else if(passwordSize<5){
            Snackbar.make(v, getResources().getString(R.string.passSizeWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else{
            //TODO save shared prefs for main screen
            SharedPreferences accountPrefs = getSharedPreferences(settingPrefsFileName, MODE_PRIVATE);
            SharedPreferences.Editor prefEdit = accountPrefs.edit();
            prefEdit.putBoolean("upperCheck",upperCheck);
            prefEdit.putBoolean("lowerCheck",lowerCheck);
            prefEdit.putBoolean("numCheck",numCheck);
            prefEdit.putBoolean("symbolCheck",symbolCheck);
            prefEdit.putInt("passwordSize",passwordSize);
            prefEdit.apply();
            finish();
        }
    }

}