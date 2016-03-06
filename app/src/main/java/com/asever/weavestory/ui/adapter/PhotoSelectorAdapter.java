package com.asever.weavestory.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.asever.weavestory.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Asever on 2016-01-27
 */
public class PhotoSelectorAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> imgPaths;
    LayoutInflater inf;

    private int itemWidth;
    private int horizentalNum = 4;
    private LayoutParams itemLayoutParams;

    public PhotoSelectorAdapter(Context context, ArrayList<String> imgPaths, int screenWidth) {
        mContext = context;
        this.imgPaths = imgPaths;
        setItemWidth(screenWidth);
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItemWidth(int screenWidth) {
        int horizentalSpace = mContext.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
        this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1))) / horizentalNum;
        this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
    }

    @Override
    public int getCount() {

        return imgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = null;
        if (convertView == null) {
            convertView = inf.inflate(R.layout.photo_selector_item, null);
        }
        view = (ImageView) convertView.findViewById(R.id.photo_selector_item_img);
        Glide.with(mContext).load(imgPaths.get(position)).fitCenter().centerCrop().error(R.drawable.no_image).into(view);
        return convertView;
    }


    public void add(ArrayList<String> imgPaths) {
        int looIndex = imgPaths.size();
        for (int i = 0; i < looIndex; i++)
            this.imgPaths.add(imgPaths.get(i));
    }
}
