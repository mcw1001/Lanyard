package com.cp470.lanyard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;



public class AccountListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
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

}