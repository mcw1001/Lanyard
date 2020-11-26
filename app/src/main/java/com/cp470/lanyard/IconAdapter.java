package com.cp470.lanyard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

public class IconAdapter extends BaseAdapter {
    Context c;
    private int[] mIcons;

    public IconAdapter(Context ctx, int[] icons){
        mIcons=icons;
        c=ctx;
    }
    @Override
    public int getCount() {
        return mIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return mIcons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.icon_button,null);
        }

        ImageView iconButton = (ImageView) convertView.findViewById(R.id.iconImgBt);
        //assign the icon to the view
        iconButton.setImageResource(mIcons[position]);
        return convertView;
    }
}
