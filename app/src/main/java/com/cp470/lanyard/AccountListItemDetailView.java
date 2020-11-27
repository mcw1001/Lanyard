package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class AccountListItemDetailView extends AppCompatActivity {

    private static final String TAG = "AccountListItemDetailView";

    // Firebase stuff
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String documentId;

    private EditText editTextAccountTitle;
    private EditText editTextAccountUserName;
    private EditText editTextAccountPassword;
    private ImageView imageViewAccountImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list_item_detail_view);

        Intent intent = getIntent();
        documentId = intent.getExtras().getString("documentId");

        editTextAccountTitle = findViewById(R.id.edit_text_account_title_DETAILVIEW);
        editTextAccountUserName = findViewById(R.id.edit_text_account_userName_DETAILVIEW);
        editTextAccountPassword = findViewById(R.id.edit_text_account_password_DETAILVIEW);
        imageViewAccountImage = findViewById(R.id.image_view_account_image_DETAILVIEW);

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
                            imageViewAccountImage.setTag(accountItem.getImageResource());//use this because there is no get imageResource
                        } else {
                            Toast.makeText(AccountListItemDetailView.this, "Error loading password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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

        AccountItem accountItem = new AccountItem(currentUser, imageResource, accountTitle, accountUserName, accountPassword, timestamp);

        db.collection("accounts").document(documentId).set(accountItem, SetOptions.merge());
        Toast.makeText(AccountListItemDetailView.this, "Password Updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AccountListItemDetailView.this, AccountListActivity.class);
        // so you cannot go back to login screen
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}