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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class AccountListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AccountListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<AccountItem> viewItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        viewItemsList= new ArrayList<AccountItem>();

        loadItemList();//load viewItemsList from database

        //initializes all parts needed for addapter class
        mRecyclerView=findViewById(R.id.accountListRecyclerView);
        mRecyclerView.setHasFixedSize(true);//needed to increase performance
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AccountListAdapter(viewItemsList);//pass item object list
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //handle click on card item pass position of card clicked from RecyclerView adapter
                handleCardClick(position);
            }
        });
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
    private void loadItemList(){
        //TODO load viewItemsList from database or file storage system

        //for now just manually add items to arraylist
        //load array list
        viewItemsList.add(new AccountItem(R.drawable.ic_baseline_mail_24,"Fake Mail","jeffBob@fake.com","fyt6fygt7ygy76yg"));
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"Twitter","@Jbob4232","juhbjnhgbgyhub"));
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"Facebook","Jeff Bob","Idontcare124"));
        viewItemsList.add(new AccountItem(R.drawable.list_icon3,"Fake R'us bank","565-48-0350","loooooooooooooooooooooongggPassword"));//35 chars
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"Xbox X","jeffIsTheBest","password"));
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"PS5","jeffIsTheBest","passwordPS5"));
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"Steam","PC_OVERLORDjeff","passwordPC"));
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"Epic","PC_OVERLORDjeff","IhateApple"));
        viewItemsList.add(new AccountItem(R.drawable.placeholder,"wlu","bobx9090","CP340"));
        
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