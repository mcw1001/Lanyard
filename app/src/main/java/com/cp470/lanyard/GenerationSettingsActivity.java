package com.cp470.lanyard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class GenerationSettingsActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME="GenerationSettingsActivity";
    TextView passwordLength;
    TextView examplePassword;
    String passCharSet;
    RadioButton uppers;
    Boolean upperCheck;
    RadioButton lowers;
    Boolean lowerCheck;
    RadioButton nums;
    Boolean numCheck;
    RadioButton symbols;
    Boolean symbolCheck;
    int passwordSize;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generation_settings);

        uppers = findViewById(R.id.uppercaseRadio);
        upperCheck=uppers.isChecked();
        lowers = findViewById(R.id.lowercaseRadio);
        lowerCheck=lowers.isChecked();
        nums =findViewById(R.id.numberRadio);
        numCheck=nums.isChecked();
        symbols = findViewById(R.id.symbolRadio);
        symbolCheck=symbols.isChecked();

        SeekBar passwordLengthBar = findViewById(R.id.passwordLengthSeekBar);
        passwordLengthBar.setOnSeekBarChangeListener(seekBarChangeListener);
        int progress = passwordLengthBar.getProgress();
        passwordSize = progress;
        passwordLength = findViewById(R.id.textPasswordLength);
        passwordLength.setText(getString(R.string.passwordSize)+": "+progress);
        examplePassword = findViewById(R.id.textExamplePassword);
        updateExample();
    }
    public void onUpperCheck(View v){
//        uppers.toggle();
        upperCheck = ((RadioButton) v).isChecked();
        updateExample();
    }
    public void onLowerCheck(View v){
        lowerCheck= ((RadioButton) v).isChecked();
        updateExample();
    }
    public void onNumCheck(View v){
        numCheck= ((RadioButton) v).isChecked();
        updateExample();
    }
    public void onSymbolCheck(View v){
        symbolCheck= ((RadioButton) v).isChecked();
        updateExample();
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
        finish();
    }

}