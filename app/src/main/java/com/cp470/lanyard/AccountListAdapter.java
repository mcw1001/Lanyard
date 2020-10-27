package com.cp470.lanyard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountViewHolder> {
    private ArrayList<AccountItem> mAccountList;
    private OnItemClickListener mListener;
    public interface  OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public static class AccountViewHolder extends RecyclerView.ViewHolder{
        public ImageView iconView;
        public TextView accountTitleView;
        public TextView passTitleView;
        public TextView uNameTitleView;
        public TextView passView;
        public TextView uNameView;

        public AccountViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            //get all views from item layout
            iconView=itemView.findViewById(R.id.iconAccountItem);
            accountTitleView=itemView.findViewById(R.id.titleAccountItem);
            //passTitleView=itemView.findViewById(R.id.passTitleAccountItem);
            //uNameTitleView=itemView.findViewById(R.id.userNameTitleAccountItem);
            passView=itemView.findViewById(R.id.passAccountItem);
            uNameView=itemView.findViewById(R.id.userNameAccountItem);

            //
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){//need listener to call method
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){//make sure card not deleted
                            listener.onItemClick(position);//get pistion of card in recyclerView and pass to interface
                        }
                    }
                }
            });
        }
    }

    public AccountListAdapter(ArrayList<AccountItem> accountList){
        mAccountList=accountList;
    }
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item,parent,false);
        AccountViewHolder avh = new AccountViewHolder(v,mListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountItem currentItem = mAccountList.get(position);//get the object at position given by addapter
        //set value from  AccountItem object to the layout
        holder.iconView.setImageResource(currentItem.getImageResource());
        holder.accountTitleView.setText(currentItem.getTitle());
        holder.uNameView.setText(currentItem.getUserName());
        holder.passView.setText(currentItem.getPassword());
    }

    @Override
    public int getItemCount() {
        return mAccountList.size();
    }
}
