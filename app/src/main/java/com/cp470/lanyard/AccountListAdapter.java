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
import com.google.firebase.firestore.DocumentSnapshot;

public class AccountListAdapter extends FirestoreRecyclerAdapter<AccountItem, AccountListAdapter.AccountViewHolder> {
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public AccountListAdapter(@NonNull FirestoreRecyclerOptions<AccountItem> options) {
        super(options);
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {
        this.mLongListener = longListener;
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconView;
        public TextView accountTitleView;
        //public TextView passView;
        public TextView uNameView;

        public AccountViewHolder(@NonNull View itemView, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            super(itemView);
            //get all views from item layout
            iconView = itemView.findViewById(R.id.iconAccountItem);
            accountTitleView = itemView.findViewById(R.id.titleAccountItem);
            //passView = itemView.findViewById(R.id.passAccountItem);
            uNameView = itemView.findViewById(R.id.userNameAccountItem);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {//need listener to call method
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && listener != null) {//make sure card is not -1 position and listener is not null
                            listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longListener != null) {//need listener to call method
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && longListener != null) {//make sure card is not -1 position and listener is not null
                            longListener.onItemLongClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                    return true;
                }
            });

        }
    }


    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        AccountViewHolder avh = new AccountViewHolder(v, mListener, mLongListener);
        return avh;
    }

    @Override
    protected void onBindViewHolder(@NonNull AccountViewHolder holder, int position, @NonNull AccountItem model) {
        holder.iconView.setImageResource(model.getImageResource());
        holder.accountTitleView.setText(model.getTitle());
        holder.uNameView.setText(model.getUserName());
        //holder.passView.setText(model.getPassword());
    }

    public void deleteAccountItem(int position) {
        /**
         -------------------------------------------------------
         Required for an AccountItem delete in the Firebase
         Firestore.
         -------------------------------------------------------
         Parameters:
         int position - The position of the item to be deleted
         -------------------------------------------------------
         */
        getSnapshots().getSnapshot(position).getReference().delete();
    }

//    public void filterList(ArrayList<AccountItem> filteredList){
//        //update list that is shown with new filtered list
//        mAccountList = filteredList;
//        notifyDataSetChanged();
//    }
}
