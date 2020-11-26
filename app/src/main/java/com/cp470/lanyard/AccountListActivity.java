package com.cp470.lanyard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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
    private int checkedItem;
    private String searchStr;


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
        checkedItem=0;//sets default clicked radio button in dialog
        searchStr="";
    }

    private void setUpRecyclerView() {
        /**
         -------------------------------------------------------
         Sets up the recyclerView using Firebase Firestore UI.
         -------------------------------------------------------
         */

        String currentUser = mAuth.getInstance().getCurrentUser().getUid();
        Query query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("title", Query.Direction.ASCENDING);

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
                int pos=viewHolder.getAdapterPosition();
                mAdapter.deleteAccountItem(pos);

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

        mAdapter.setOnItemLongClickListener(new AccountListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
                AccountItem accountItem = documentSnapshot.toObject(AccountItem.class); //the name of the item in the db
                //Log.d("copyclick", "onItemLongClick: press");
                onCopyClick(accountItem.getTitle(), accountItem.getPassword());
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

    public void onCopyClick(String title, String password) {

        //View card = (View) view.getParent();
        //get title of account
        //TextView titleView = (TextView) card.findViewById(R.id.titleAccountItem);
        //String title = titleView.getText().toString();
        //get password
        //TextView passView = (TextView) card.findViewById(R.id.passAccountItem);
        //String password = passView.getText().toString();

        //copy to clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(title + " password", password);
        clipboard.setPrimaryClip(clip);
        //Log.d("copyclick", "onCopyClick: "+title+" "+password);
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

    public void logout() {
        /**
         -------------------------------------------------------
         Logs a user out of the app using Firebase Auth
         -------------------------------------------------------

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

    //====================== MENUBAR, SORT, AND HELP BUTTON ========================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         ---------------------------------------------------
         override options menu to hold sort and help buttons
         -------------------------------------------------
         */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu,menu);

        //get search bar and set up listener
        MenuItem searchItem = menu.findItem(R.id.accountListSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //listener for search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //only search on submit
                Log.i("searchBar", "onQueryTextSubmit: "+query);
                searchStr=query;
                executeQuery(checkedItem,searchStr);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //listens for when search is cleared
                if(newText.isEmpty()) {
                    Log.i("searchBar", "onQueryTextSubmit: empty");
                    searchStr="";
                    executeQuery(checkedItem,searchStr);
                }

                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /**
         ---------------------------------------------------
         Listener for menu buttons
         -------------------------------------------------
         */
        switch (item.getItemId()){
            //check which menu item is selected
            case R.id.menuSortBt:
                //Log.i("AccountList_menu", "onOptionsItemSelected: sort");
                onSortClick();
                return true;
            case R.id.menuLogoutBt:
                //Log.i("AccountList_menu", "onOptionsItemSelected: log out");
                logout();
                return true;
            case R.id.menuInfoBt:
                //Log.i("AccountList_menu", "onOptionsItemSelected: info");
                showInfoBox(R.string.menuInfoTitle,R.array.infoBoxText);
                return true;
            case R.id.menuHelptBt:
                //Log.i("AccountList_menu", "onOptionsItemSelected: help");
                showInfoBox(R.string.menuHelpTitle,R.array.helpBoxText);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void onSortClick(){
        /**
        ------------------------------------------------------------------------------
        Display a radio option dialog box with sort options. Then sort list accordingly
         -----------------------------------------------------------------------------
        */
        AlertDialog.Builder sortDialog = new AlertDialog.Builder(AccountListActivity.this);
        sortDialog.setTitle(R.string.menuPopSortTitle);
        Resources res = getResources();
        final String[] sortOps = res.getStringArray(R.array.sortOptions);

        sortDialog.setSingleChoiceItems(sortOps, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle sort selection by resorting accordingly
                switch (which){
                    case 0://Account name A-Z
                        //Toast.makeText(AccountListActivity.this,"Clicked on sort by: "+sortOps[0],Toast.LENGTH_LONG).show();
                        checkedItem = 0;

                        break;
                    case 1://Account name Z-A
                        //Toast.makeText(AccountListActivity.this,"Clicked on sort by: "+sortOps[1],Toast.LENGTH_LONG).show();
                        checkedItem = 1;
                        break;
                    case 2://User name A-Z
                        //Toast.makeText(AccountListActivity.this,"Clicked on sort by: "+sortOps[2],Toast.LENGTH_LONG).show();
                        checkedItem = 2;
                        break;
                    case 3://User name Z-A
                        //Toast.makeText(AccountListActivity.this,"Clicked on sort by: "+sortOps[3],Toast.LENGTH_LONG).show();
                        checkedItem = 3;
                        break;
                    case 4://Date
                        //Toast.makeText(AccountListActivity.this,"Clicked on sort by: "+sortOps[4],Toast.LENGTH_LONG).show();
                        checkedItem = 4;
                        break;
                    case 5:
                        //Toast.makeText(AccountListActivity.this,"Clicked on sort by: "+sortOps[5],Toast.LENGTH_LONG).show();
                        checkedItem = 5;
                        break;
                }
                //run query
                executeQuery(checkedItem,searchStr);
            }
        });
        sortDialog.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //just close dialog bsox
            }
        });

        AlertDialog sortBox = sortDialog.create();
        sortBox.setCanceledOnTouchOutside(false);
        sortBox.show();
    }

    private void showInfoBox(int titleId,int messageId){
        /**
        --------------------------------------
        Displays info dialog box
        Used for "help" and "App Info" dialog
        Pass title and message id as args
        message id is id of string array
         --------------------------------------
     */
        Resources res = getResources();
        final String[] text = res.getStringArray(messageId);
        //concat and add newlines
        String textString="";
        for (int i=0;i<text.length;i++){
            if(text[i].charAt(0)=='/'){
                textString = textString +"  "+ text[i].substring(1) + "\n";
            }else {
                textString = textString + text[i] + "\n";
            }
        }

        AlertDialog.Builder infoDialogBuilder = new AlertDialog.Builder(AccountListActivity.this);
        infoDialogBuilder.setTitle(titleId);
        infoDialogBuilder.setMessage(textString);
        infoDialogBuilder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //just close dialog bsox
            }
        });
        AlertDialog dialog = infoDialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    //====================== SORT AND SEARCH FUNCTIONS ========================================
    private void executeQuery(int sortMode, String searchStr){
        /**
         -----------------------------------------------------------------------------------
         Builds and runs query based of sort and search parameters
         ------------------------------------------------------------------------------------
         Parameters:
         int sortMode - matches the value of checkedItem in sort dialog. Specifies sort mode
         String searchStr - the string user searched for. If empty don't search for any string
         ------------------------------------------------------------------------------------
         */

        String currentUser = mAuth.getInstance().getCurrentUser().getUid();
        Query query= accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("title", Query.Direction.DESCENDING);

        if (searchStr.isEmpty()) {
            switch (sortMode) {
                case 0://Account name A-Z
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("title", Query.Direction.ASCENDING);
                    break;
                case 1://Account name Z-A
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("title", Query.Direction.DESCENDING);
                    break;
                case 2://User name A-Z
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("userName", Query.Direction.ASCENDING);
                    break;
                case 3://User name Z-A
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("userName", Query.Direction.DESCENDING);
                    break;

                case 4://Date

                    query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("timestamp", Query.Direction.DESCENDING);
                    checkedItem = 4;
                    break;
                case 5:
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).orderBy("timestamp", Query.Direction.ASCENDING);
                    checkedItem = 5;
                    break;


            }
        }else {
            switch (sortMode) {
                case 0://Account name A-Z
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).whereEqualTo("title", searchStr).orderBy("title", Query.Direction.ASCENDING);
                    break;
                case 1://Account name Z-A
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).whereEqualTo("title", searchStr).orderBy("title", Query.Direction.DESCENDING);
                    break;
                case 2://User name A-Z
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).whereEqualTo("title", searchStr).orderBy("userName", Query.Direction.ASCENDING);
                    break;
                case 3://User name Z-A
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).whereEqualTo("title", searchStr).orderBy("userName", Query.Direction.DESCENDING);
                    break;

                case 4://Date

                    query = accountRef.whereEqualTo("userIdMaster", currentUser).whereEqualTo("title", searchStr).orderBy("timestamp", Query.Direction.DESCENDING);
                    checkedItem = 4;
                    break;
                case 5:
                    query = accountRef.whereEqualTo("userIdMaster", currentUser).whereEqualTo("title", searchStr).orderBy("timestamp", Query.Direction.ASCENDING);
                    checkedItem = 5;
                    break;


            }
        }
        FirestoreRecyclerOptions<AccountItem> options = new FirestoreRecyclerOptions.Builder<AccountItem>()
                .setQuery(query, AccountItem.class)
                .build();
        //update adapter
        mAdapter.updateOptions(options);


    }
}