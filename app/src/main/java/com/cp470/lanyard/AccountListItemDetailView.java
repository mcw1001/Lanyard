package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountListItemDetailView extends AppCompatActivity implements IconPicker.IconDialogListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AccountListItemDetailView";

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String documentId;

    private EditText editTextAccountTitle;
    private EditText editTextAccountUserName;
    private EditText editTextAccountPassword;
    private ImageButton imageViewAccountImage;
    private TextView expireDate_DETAIL;
    private int imageResource;// resource int for icon in list view
    private int priority;

    private Calendar calendar;
    private Date date;

    private String settingPrefsFileName;
    private Boolean upperCheck;
    private Boolean lowerCheck;
    private Boolean numCheck;
    private Boolean symbolCheck;
    private int passwordSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list_item_detail_view);

        Intent intent = getIntent();
        documentId = intent.getExtras().getString("documentId");

        editTextAccountTitle = findViewById(R.id.edit_text_account_title_DETAILVIEW);
        editTextAccountUserName = findViewById(R.id.edit_text_account_userName_DETAILVIEW);
        editTextAccountPassword = findViewById(R.id.edit_text_account_password_DETAILVIEW);
        imageViewAccountImage = findViewById(R.id.image_button_account_image_DETAILVIEW);
        expireDate_DETAIL = findViewById(R.id.expireDate_DETAILVIEW);
        settingPrefsFileName = getString(R.string.settingPrefsName);
        calendar = Calendar.getInstance();
        pullPrefs();

        loadAccountItem();
    }

    private void loadAccountItem() {
        /**
         -------------------------------------------------------
         Loads AccountItems from the Firebase Firestore
         -------------------------------------------------------
         */
        db.collection("accounts").document(documentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            AccountItem accountItem = documentSnapshot.toObject(AccountItem.class);

                            editTextAccountTitle.setText(accountItem.getTitle());
                            editTextAccountUserName.setText(accountItem.getUserName());
                            editTextAccountPassword.setText(accountItem.getPassword());
                            imageViewAccountImage.setImageResource(accountItem.getImageResource());
                            imageViewAccountImage.setTag(accountItem.getImageResource());//use this because there is no getter for imageResource
                            imageResource = accountItem.getImageResource();
                            Timestamp timestamp = accountItem.getExpirationDate();
                            priority = accountItem.getPriority();
                            // If expiry date is within 6 months or has passed, default new expiry date to 6 months from now
                            calendar.add(Calendar.MONTH, 6);
                            Date sixMonthsFromNow = calendar.getTime();
                            date = timestamp.toDate();
                            if (sixMonthsFromNow.compareTo(date) > 0) {
                                date = sixMonthsFromNow;
                            }
                            calendar.setTime(date);
                            String currentDateString = getString(R.string.expirationDateShort) + " "
                                    + DateFormat.getDateInstance().format(calendar.getTime());
                            expireDate_DETAIL.setText(currentDateString);

                        } else {
                            Toast.makeText(AccountListItemDetailView.this,
                                    getString(R.string.passwordError), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void pullPrefs(){
        SharedPreferences accountPrefs = getSharedPreferences(settingPrefsFileName, MODE_PRIVATE);
        upperCheck=accountPrefs.getBoolean("upperCheck",false);
        lowerCheck=accountPrefs.getBoolean("lowerCheck",true);
        numCheck=accountPrefs.getBoolean("numCheck",true);
        symbolCheck=accountPrefs.getBoolean("symbolCheck",false);
        passwordSize=accountPrefs.getInt("passwordSize",10);
//        Log.i(TAG,"pullPrefs: upperCheck:"+upperCheck+" lowerCheck:"+lowerCheck+" symbolCheck:"+symbolCheck+" numCheck:"+numCheck+" passwordSize:"+passwordSize);
    }

    public void onGenerateClicked(android.view.View view){
        pullPrefs();
        String p = PasswordGenerator.generate(upperCheck,lowerCheck,symbolCheck,numCheck,passwordSize);
        editTextAccountPassword.setText(p);
    }

    public void onSettingsClicked(View view){
        Intent intent = new Intent(AccountListItemDetailView.this,GenerationSettingsActivity.class);
        startActivity(intent);
    }

    public void editPassword(View view) {
        /**
         -------------------------------------------------------
         Edits/Updates an AccountItem stored in the Firebase
         Firestore.
         -------------------------------------------------------
         Parameters:
         View view - a view
         -------------------------------------------------------
         */
        String accountTitle = editTextAccountTitle.getText().toString();
        String accountUserName = editTextAccountUserName.getText().toString();
        String accountPassword = editTextAccountPassword.getText().toString();
        int imageResource = (int) imageViewAccountImage.getTag();
        FirebaseUser currentUser = mAuth.getInstance().getCurrentUser();

        Timestamp timestamp = Timestamp.now();

        date = calendar.getTime();
        Timestamp expirationDate = new Timestamp(date);


        AccountItem accountItem = new AccountItem(currentUser, imageResource, accountTitle, accountUserName, accountPassword, timestamp, expirationDate, priority);

        db.collection("accounts").document(documentId).set(accountItem, SetOptions.merge());
        Toast.makeText(AccountListItemDetailView.this, getString(R.string.passwordUpdated),
                Toast.LENGTH_SHORT).show();
        // TODO give proper result code
        this.setResult(1);
        finish();

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
        imageViewAccountImage.setImageResource(imageResource);
        imageViewAccountImage.setTag(resourceId);//use this because there is no getter for imageResource
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = getString(R.string.expirationDateShort) + " "
                + DateFormat.getDateInstance().format(calendar.getTime());
        expireDate_DETAIL.setText(currentDateString);
    }
}