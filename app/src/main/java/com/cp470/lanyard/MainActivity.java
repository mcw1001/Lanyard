package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

public class MainActivity extends AppCompatActivity implements IconPicker.IconDialogListener {
    private static final String TAG = "MainActivity";

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText editTextAccountTitle;
    private EditText editTextAccountUserName;
    private EditText editTextAccountPassword;

    private Button genPasswordButton;
    private ImageButton genSettingsButton;
    private ImageButton accountIconButton;
    private int imageResource;// resource int for icon in list view
    private String settingPrefsFileName;



    private Boolean upperCheck;
    private Boolean lowerCheck;
    private Boolean numCheck;
    private Boolean symbolCheck;
    private int passwordSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageResource=R.drawable.ic_iconfinder_new_google_favicon_682665;//default icon
        editTextAccountTitle = findViewById(R.id.edit_text_account_title);
        editTextAccountUserName = findViewById(R.id.edit_text_account_userName);
        editTextAccountPassword = findViewById(R.id.edit_text_account_password);
        genPasswordButton = findViewById(R.id.passGenButton);
        genSettingsButton = findViewById(R.id.passGenSettingsButton);
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

    public void onGenerateClicked(android.view.View view){
        pullPrefs();
        String p = PasswordGenerator.generate(upperCheck,lowerCheck,symbolCheck,numCheck,passwordSize);
        editTextAccountPassword.setText(p);
    }

    public void onSettingsClicked(View view){
        //either inflate settings or do an additional transition to settings, used sharedprefs?
        Intent intent = new Intent(MainActivity.this,GenerationSettingsActivity.class);
        startActivity(intent);
    }

    public void savePassword(android.view.View view) {
        /**
         -------------------------------------------------------
         Saves a AccountItem to the Firebase Firestore db
         -------------------------------------------------------
         Parameters:
         view - a view item
         -------------------------------------------------------
         */

        String accountTitle = editTextAccountTitle.getText().toString();
        String accountUserName = editTextAccountUserName.getText().toString();
        String accountPassword = editTextAccountPassword.getText().toString();
        if(accountTitle.length()<1){
            Snackbar.make(view, getResources().getString(R.string.emptyTitleWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else if (accountUserName.length()<1){
            Snackbar.make(view, getResources().getString(R.string.emptyUserWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else if (accountPassword.length()<=1){
            Snackbar.make(view, getResources().getString(R.string.emptyPassWarning),Snackbar.LENGTH_LONG)
                    .setAction("Action",null).show();
        }else {
            FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

            Timestamp timestamp = Timestamp.now();



        //int imageResource = 0;
        AccountItem accountItem = new AccountItem(currentUser, imageResource, accountTitle, accountUserName, accountPassword, timestamp);

            // Add a new document with a generated ID
            db.collection("accounts")
                    .add(accountItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
            Toast.makeText(MainActivity.this, "Password Stored", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AccountListActivity.class);
            // so you cannot go back to login screen
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void showIconPicker(View view) {
        /**
         * --------------------------------------
         Launches the icon picker fragment
         --------------------------------------
         */
        IconPicker iconPicker = new IconPicker();
        iconPicker.show(getSupportFragmentManager(),"Icon picker");
    }

    @Override
    public void applyIcon(int resourceId) {
        /**
         * --------------------------------------
         Interfaces with icon picker fragment
         --------------------------------------
         */
        imageResource=resourceId;//update resorce id
        ImageButton imageButton = (ImageButton) findViewById(R.id.iconPickerBt);
        imageButton.setImageResource(imageResource);
    }
}
