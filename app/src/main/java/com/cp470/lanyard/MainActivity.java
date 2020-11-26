package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements IconPicker.IconDialogListener {
    private static final String TAG = "MainActivity";

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText editTextAccountTitle;
    private EditText editTextAccountUserName;
    private EditText editTextAccountPassword;

    private int imageResource;// resource int for icon in list view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageResource=R.drawable.placeholder;//default icon
        editTextAccountTitle = findViewById(R.id.edit_text_account_title);
        editTextAccountUserName = findViewById(R.id.edit_text_account_userName);
        editTextAccountPassword = findViewById(R.id.edit_text_account_password);


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

    public void showIconPicker(View view) {
        IconPicker iconPicker = new IconPicker();
        iconPicker.show(getSupportFragmentManager(),"Icon picker");
    }

    @Override
    public void applyIcon(int resourceId) {
        imageResource=resourceId;//update resorce id
        ImageButton imageButton = (ImageButton) findViewById(R.id.iconPickerBt);
        imageButton.setImageResource(imageResource);
    }
}
