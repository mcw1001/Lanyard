package com.cp470.lanyard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class IconPickerFragment extends DialogFragment {
    GridView gv;
    int[] iconVals = {R.drawable.placeholder,R.drawable.iconfinder_1_youtube_colored_svg_5296521,R.drawable.iconfinder_2018_social_media_popular_app_logo_facebook_3225194,R.drawable.iconfinder_2018_social_media_popular_app_logo_instagram_3225191,R.drawable.iconfinder_2018_social_media_popular_app_logo_reddit_3225187,
            R.drawable.iconfinder_387_xbox_logo_4375141,R.drawable.iconfinder_amazon_2062062,R.drawable.iconfinder_card_credit_mastercard_bank_debit_2908223,R.drawable.iconfinder_credit_bank_card_532624,R.drawable.iconfinder_disney_plus_2_7033669,R.drawable.iconfinder_gmail_1220340,R.drawable.iconfinder_icon_game_controller_b_211668,
            R.drawable.iconfinder_netflix_221245,R.drawable.iconfinder_new_google_favicon_682665,R.drawable.iconfinder_outlook_3069714,R.drawable.iconfinder_playstation_5_seeklogo_com_5_7048152,R.drawable.iconfinder_social_57_1591872,R.drawable.iconfinder_steam_logo_4177739,R.drawable.iconfinder_twitter_circle_294709,R.drawable.list_icon3,
            R.drawable.iconfinder_we_chat_social_2492622};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.icon_list,null);

        //set gridView
        gv=(GridView) rootView.findViewById(R.id.iconGrid);
        getDialog().setTitle(R.string.iconPickerTitle);

        //create adapter and pass it
        IconAdapter adapter =new IconAdapter(getActivity(),iconVals);
        gv.setAdapter(adapter);

        //set on click listener
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),iconVals[position],Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
