package com.cp470.lanyard;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;



public class AccountListActivity extends AppCompatActivity {
//    private RecyclerView mRecyclerView;
//
//    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference accountRef = FirebaseFirestore.getInstance().collection("accounts");
    private AccountListAdapter mAdapter;

    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    // For Firebase user logout
    //------------------------------------
    Button logoutButton;
    FirebaseAuth mAuth;
    //------------------------------------

    ArrayList<AccountItem> viewItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        
        setUpRecyclerView();

        // For Firebase user logout
        //------------------------------------
        logoutButton = findViewById(R.id.logoutMaster);
        mAuth = FirebaseAuth.getInstance();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(AccountListActivity.this, LoginActivity.class);
                // so you cannot go back to login screen
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(AccountListActivity.this, "You are logged out!", Toast.LENGTH_SHORT);
            }
        });
        //------------------------------------


        mAdapter.setOnItemClickListener(new AccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //handle click on card item pass position of card clicked from RecyclerView adapter
                handleCardClick(position);
            }
        });
    }

    private void setUpRecyclerView() {
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
    }

    @Override
    protected void onStart(){
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

    private void handleCardClick(int postion){
        AccountItem account = viewItemsList.get(postion);//get item at same position as card clicked\
        String title = account.getTitle();
        String uName = account.getUserName();
        String pass = account.getPassword();

        //TEMPORARY print to log and toast
        String clickMessage=title+" clicked";
        Toast toast = Toast.makeText(this , clickMessage, Toast.LENGTH_SHORT);
        toast.show(); //display your message box
        Log.i("handleCardClick", "handleCardClick: user name is "+uName);
        Log.i("handleCardClick", "handleCardClick: password is "+pass);

        //TODO pass account object to detail view actvity
        //use intent



    }

    //copies password to clipboard
    public void onCopyClick(View view){

        View card=(View) view.getParent();
        //get title of account
        TextView titleView=(TextView) card.findViewById(R.id.titleAccountItem);
        String title=titleView.getText().toString();
        //get password
        TextView passView = (TextView)card.findViewById(R.id.passAccountItem);
        String password = passView.getText().toString();

        //copy to clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(title+" password",password);
        clipboard.setPrimaryClip(clip);

        //print message it toast
        String copyMessage=title+" "+getString(R.string.copyMsg);
        Toast toast = Toast.makeText(this , copyMessage, Toast.LENGTH_SHORT);
        toast.show(); //display your message box

    }

    public void onNewAccountClick(View view){
        //TODO change MainActivity to activity that allows user to make new account
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, 10);
    }

}