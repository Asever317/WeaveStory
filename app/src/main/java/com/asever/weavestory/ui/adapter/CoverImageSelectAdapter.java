package com.asever.weavestory.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asever.weavestory.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Asever on 2016-02-02.
 */
public class CoverImageSelectAdapter extends RecyclerView
        .Adapter<CoverImageSelectAdapter
        .CoverImageViewHolder> {
    private ArrayList<String> mAlbumData;
    private static CoverImageClickListener myClickListener;
    private Context mContext;

    public static class CoverImageViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView albumImage;

        public CoverImageViewHolder(View itemView) {
            super(itemView);
            albumImage = (ImageView) itemView.findViewById(R.id.cover_image_select_item_imgs_album);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(CoverImageClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CoverImageSelectAdapter(ArrayList<String> mAlbumData) {
        this.mAlbumData = mAlbumData;
    }

    @Override
    public CoverImageViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cover_image_select_item, parent, false);
        this.mContext = parent.getContext();
        CoverImageViewHolder coverImageViewHolder = new CoverImageViewHolder(view);
        return coverImageViewHolder;
    }

    @Override
    public void onBindViewHolder(CoverImageViewHolder holder, int position) {
//        Picasso.with(mContext).load(mAlbumData.get(position)).fit().error(R.drawable.no_image).into(holder.albumImage);
        Glide.with(mContext).load(mAlbumData.get(position)).fitCenter().centerCrop().error(R.drawable.no_image).into(holder.albumImage);
    }

    @Override
    public int getItemCount() {
        return mAlbumData.size();
    }


    public interface CoverImageClickListener {
        public void onItemClick(int position, View v);
    }
}