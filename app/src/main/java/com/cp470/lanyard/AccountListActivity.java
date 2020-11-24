package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;


public class AccountListActivity extends AppCompatActivity {

    private static final CollectionReference accountRef = FirebaseFirestore.getInstance().collection("accounts");
    private AccountListAdapter mAdapter;


    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    // For Firebase user logout
    //------------------------------------
    FirebaseAuth mAuth;
    //------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        /**
         -------------------------------------------------------
         Sets up the recyclerView using Firebase Firestore UI.
         -------------------------------------------------------
         */
        String currentUser = mAuth.getInstance().getCurrentUser().getUid();
        Query query = accountRef.whereEqualTo("userIdMaster", currentUser);

        FirestoreRecyclerOptions<AccountItem> options = new FirestoreRecyclerOptions.Builder<AccountItem>()
                .setQuery(query, AccountItem.class)
                .build();
        mAdapter = new AccountListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.accountListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.deleteAccountItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        mAdapter.setOnItemClickListener(new AccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String documentId = documentSnapshot.getId(); //the name of the item in the db
                System.out.println(documentId);
                Toast.makeText(AccountListActivity.this, "click", Toast.LENGTH_SHORT);
                Intent intent = new Intent(AccountListActivity.this, AccountListItemDetailView.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //start listening for firestore db changes
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop listening for firestore db changes
        mAdapter.stopListening();
    }

    public void onCopyClick(View view) {

        View card = (View) view.getParent();
        //get title of account
        TextView titleView = (TextView) card.findViewById(R.id.titleAccountItem);
        String title = titleView.getText().toString();
        //get password
        TextView passView = (TextView) card.findViewById(R.id.passAccountItem);
        String password = passView.getText().toString();

        //copy to clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(title + " password", password);
        clipboard.setPrimaryClip(clip);

        //print message it toast
        String copyMessage = title + " " + getString(R.string.copyMsg);
        Toast toast = Toast.makeText(this, copyMessage, Toast.LENGTH_SHORT);
        toast.show(); //display your message box
    }

    public void onNewAccountClick(View view) {
        /**
         -------------------------------------------------------
         Triggers an AccountItem create intent
         -------------------------------------------------------
         Parameters:
         View view - a view
         -------------------------------------------------------
         */
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 10);
    }

    public void logout(View view) {
        /**
         -------------------------------------------------------
         Logs a user out of the app using Firebase Auth
         -------------------------------------------------------
         Parameters:
         View view - a view
         -------------------------------------------------------
         */
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Intent intent = new Intent(AccountListActivity.this, LoginActivity.class);
        // so you cannot go back to login screen
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(AccountListActivity.this, "You are logged out!", Toast.LENGTH_SHORT).show();
    }
}