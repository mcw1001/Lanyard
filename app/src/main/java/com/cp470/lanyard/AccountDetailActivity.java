package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.accounts.Account;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class AccountDetailActivity extends AppCompatActivity {
    protected static final int EDIT_SUCCESS = 1;
    protected TextView accountTitle;
    protected ImageView accountIcon;
    protected TextView accountUsername;
    protected TextView accountPassword;
    protected TextView accountCreation;
    protected TextView accountTimesCopied;
    protected ProgressBar loadingBar;
    protected ConstraintLayout accountDetails;
    protected ImageView togglePassword;

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String documentId;    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        documentId = intent.getExtras().getString("documentId");
        setContentView(R.layout.activity_account_detail);
        accountTitle = findViewById(R.id.detailTitleAccountItem);
        accountIcon = findViewById(R.id.detailIconAccountItem);
        accountUsername = findViewById(R.id.detailUserNameAccountItem);
        accountPassword = findViewById(R.id.detailPassAccountItem);
        accountPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        accountCreation = findViewById(R.id.detailCreationDate);
        accountTimesCopied = findViewById(R.id.detailPassCopy);
        togglePassword = findViewById(R.id.detail_toggle_password);

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

                        accountTitle.setText(accountItem.getTitle());
                        accountIcon.setImageResource(accountItem.getImageResource());

                        accountUsername.setText(accountItem.getUserName());
                        accountPassword.setText(accountItem.getPassword());
                        Date creationDate = accountItem.getTimestamp().toDate();
                        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, YYYY");
                        accountCreation.setText(format.format(creationDate));
                        accountTimesCopied.setText(Integer.toString(accountItem.getPriority()));
                        loadingBar = findViewById(R.id.detail_loading);
                        accountDetails = findViewById(R.id.account_details);
                        loadingBar.setVisibility(View.INVISIBLE);
                        accountDetails.setVisibility(View.VISIBLE);
                        Date expiryDate = accountItem.getExpirationDate().toDate();
                        checkPasswordLastUpdated(expiryDate);

                    } else {
                        Toast.makeText(AccountDetailActivity.this, "Error loading password", Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }

    public void goToEditAccount() {
        Intent i = new Intent(this, AccountListItemDetailView.class);
        i.putExtra("documentId", documentId);
        startActivityForResult(i, 15);
    }

    public void checkPasswordLastUpdated(Date expiryDate) {
        // User's password has not been updated in awhile, suggest an update
        Date now = new Date();
        if (expiryDate.compareTo(now) < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.password_reminder_dialog, null));
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToEditAccount();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    // Toggles password visibility
    public void onTogglePasswordView(View view) {
        if (accountPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            accountPassword.setTransformationMethod(null);
            togglePassword.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        } else {
            accountPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            togglePassword.setImageResource(R.drawable.ic_baseline_visibility_24);
        }
    }

    public void onEditAccountClick(View view) {
        goToEditAccount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Account details changed successfully; reload with new data
        if (resultCode == EDIT_SUCCESS) {
            loadingBar.setVisibility(View.VISIBLE);
            accountDetails.setVisibility(View.INVISIBLE);
            loadAccountItem();
        }
    }
}