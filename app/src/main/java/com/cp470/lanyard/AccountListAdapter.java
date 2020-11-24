package com.cp470.lanyard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AccountListAdapter extends FirestoreRecyclerAdapter<AccountItem,AccountListAdapter.AccountViewHolder> {
    private ArrayList<AccountItem> mAccountList;
    private OnItemClickListener mListener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AccountListAdapter(@NonNull FirestoreRecyclerOptions<AccountItem> options) {
        super(options);
    }

    public interface  OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }
    public static class AccountViewHolder extends RecyclerView.ViewHolder{
        public ImageView iconView;
        public TextView accountTitleView;
        public TextView passView;
        public TextView uNameView;

        public AccountViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            //get all views from item layout
            iconView = itemView.findViewById(R.id.iconAccountItem);
            accountTitleView = itemView.findViewById(R.id.titleAccountItem);
            passView = itemView.findViewById(R.id.passAccountItem);
            uNameView = itemView.findViewById(R.id.userNameAccountItem);


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


    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item,parent,false);
        AccountViewHolder avh = new AccountViewHolder(v,mListener);
        return avh;
    }

    @Override
    protected void onBindViewHolder(@NonNull AccountViewHolder holder, int position, @NonNull AccountItem model) {
        //holder.iconView.setImageResource(model.getImageResource());
        holder.accountTitleView.setText(model.getTitle());
        holder.uNameView.setText(model.getUserName());
        holder.passView.setText(model.getPassword());
    }

//    @Override
//    public int getItemCount() {
//        return mAccountList.size();
//    }
}
