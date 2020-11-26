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
    protected TextView accountTitle;
    protected ImageView accountIcon;
    protected TextView accountUsername;
    protected TextView accountPassword;
    protected TextView accountCreation;
    protected TextView accountNotes;
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
        accountNotes = findViewById(R.id.detailPassNotes);
        togglePassword = findViewById(R.id.detail_toggle_password);
        accountNotes.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Phasellus sem quam, maximus eu vehicula id, sagittis quis sem. In luctus ultricies lacus nec lobortis. Aliquam sed porttitor mauris. " +
                "Etiam sit amet imperdiet ante. Morbi suscipit ultrices ante, ut euismod massa rhoncus et. " +
                "Curabitur libero lacus, scelerisque a orci laoreet, viverra iaculis arcu. " +
                "Duis fermentum lorem in justo blandit eleifend. Curabitur et tincidunt ex. Integer id dolor libero. " +
                "Aenean est sapien, commodo vel est accumsan, tincidunt pellentesque diam. " +
                "Praesent libero quam, vestibulum nec efficitur in, lacinia ut nunc. Suspendisse malesuada euismod nunc vitae euismod. "
        );

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
                        // accountIcon.setImageResource(accountItem.getImageResource());
                        // TODO swap out for actual icon
                        accountIcon.setImageResource(R.drawable.placeholder);
                        accountUsername.setText(accountItem.getUserName());
                        accountPassword.setText(accountItem.getPassword());
                        Date creationDate = accountItem.getTimestamp().toDate();
                        SimpleDateFormat format = new SimpleDateFormat("MMMM dd, YYYY");
                        accountCreation.setText(format.format(creationDate));
                        // TODO remove me I am just for testing
                        try {
                            Thread.sleep(1000);
                        } catch(Exception e){

                        }
                        loadingBar = findViewById(R.id.detail_loading);
                        accountDetails = findViewById(R.id.account_details);
                        loadingBar.setVisibility(View.INVISIBLE);
                        accountDetails.setVisibility(View.VISIBLE);
                        checkPasswordLastUpdated(creationDate);

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

    public void checkPasswordLastUpdated(Date lastUpdated) {
        // TODO user should be able to modify this setting
        int MONTHS_BEFORE_EDIT = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -MONTHS_BEFORE_EDIT);
        Date beforeNow = calendar.getTime();
        // User's password has not been updated in awhile, suggest an update
        if (beforeNow.compareTo(lastUpdated) > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // TODO put strings in values
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
        if (resultCode == 1) {
            loadingBar.setVisibility(View.VISIBLE);
            accountDetails.setVisibility(View.INVISIBLE);
            loadAccountItem();
        }
    }
}