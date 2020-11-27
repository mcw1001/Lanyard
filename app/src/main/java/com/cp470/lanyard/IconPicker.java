package com.cp470.lanyard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class IconPicker extends AppCompatDialogFragment {
    GridView gv;
    int[] iconVals = {R.drawable.placeholder,R.drawable.ic_iconfinder_1_youtube_colored_svg_5296521,R.drawable.ic_iconfinder_2018_social_media_popular_app_logo_facebook_3225194,R.drawable.ic_iconfinder_2018_social_media_popular_app_logo_instagram_3225191,R.drawable.ic_iconfinder_2018_social_media_popular_app_logo_reddit_3225187,
            R.drawable.ic_iconfinder_387_xbox_logo_4375141, R.drawable.ic_iconfinder_amazon_2062062,R.drawable.ic_iconfinder_card_credit_mastercard_bank_debit_2908223,R.drawable.ic_iconfinder_credit_bank_card_532624,R.drawable.ic_iconfinder_disney_plus_2_7033669,R.drawable.ic_iconfinder_gmail_1220340,R.drawable.ic_iconfinder_netflix_221245,
            R.drawable.ic_iconfinder_new_google_favicon_682665,R.drawable.ic_iconfinder_outlook_3069714,R.drawable.ic_iconfinder_playstation_5_seeklogo_com_5_7048152,R.drawable.ic_iconfinder_social_57_1591872,R.drawable.ic_iconfinder_steam_logo_4177739,R.drawable.ic_iconfinder_twitter_circle_294709,R.drawable.ic_iconfinder_we_chat_social_2492622,R.drawable.list_icon3};
    private int iconVal;
    private IconDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        iconVal=0;
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.icon_list,null);

        builder.setView(view).setTitle(R.string.iconPickerTitle).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int resourceId=iconVal;
                listener.applyIcon(resourceId);//pass id to caller activity
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing just return
            }
        });

        //set gridView
        gv=(GridView) view.findViewById(R.id.iconGrid);
        //create adapter and pass it
        IconAdapter adapter =new IconAdapter(getActivity(),iconVals);
        gv.setAdapter(adapter);

        //set on click listener
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(),"Selected "+iconVals[position],Toast.LENGTH_SHORT).show();
                iconVal=iconVals[position];
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(IconDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement IconDialogListener");
        }
    }

    public interface IconDialogListener{
        void applyIcon(int resourceId);
    }

}
