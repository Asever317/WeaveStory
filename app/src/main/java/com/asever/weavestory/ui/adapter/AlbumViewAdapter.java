package com.asever.weavestory.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.AlbumData;
import com.asever.weavestory.ui.TouchImageView;
import com.asever.weavestory.util.OnItemClickListener;
import com.asever.weavestory.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;


/**
 * Created by Asever on 2015/12/20.
 */
public class AlbumViewAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private AlbumData albumData;
    private int itemlayout;
    private Context mContext;

    private OnItemClickListener onItemClickListener;


    public AlbumViewAdapter(AlbumData data, int itemlayout, Context mContext) {
        this.albumData = data;
        this.itemlayout = itemlayout;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup viewgroup, int viewtype) {
        View view = LayoutInflater.from(viewgroup.getContext()).inflate(itemlayout, viewgroup, false);
        return new AlbumViewHolder(view, onItemClickListener, mContext);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder viewholder, final int position) {
        String imgPath = albumData.getContentData(position).getImgPath();
        String imgContent = albumData.getContentData(position).getImgContent();
        String imgDate = albumData.getContentData(position).getImgDate();

        viewholder.textView_Content.setText(imgContent);
        viewholder.tv_Date.setText(imgDate);
        //reset
        viewholder.imageView_Photo.resetZoom();
        viewholder.imageView_Photo.setRotation(0);

        if (albumData.getContentData(position).isEdit()) {
            float imageWidth = albumData.getContentData(position).getScale_img_Width();
            float imageHeight = albumData.getContentData(position).getScale_img_Height();
//            Glide.with(mContext).load(imgPath).error(R.drawable.no_image).fitCenter().error(R.drawable.no_image).listener(new RequestListener<String, GlideDrawable>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    viewholder.imageView_Photo.setRotation(albumData.getContentData(position).getRotation());
//                    viewholder.imageView_Photo.setZoom(albumData.getContentData(position).getScale());
//                    viewholder.imageView_Photo.setScrollPosition(albumData.getContentData(position).getPositionX(), albumData.getContentData(position).getPositionY());
//                    return false;
//                }
//            }).into(viewholder.imageView_Photo);
//            Glide.with(mContext).load(imgPath).fitCenter().error(R.drawable.no_image).into(new SimpleTarget<GlideDrawable>() {
//                @Override
//                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    viewholder.imageView_Photo.d(resource);
//                    viewholder.imageView_Photo.setRotation(albumData.getContentData(position).getRotation());
//                    viewholder.imageView_Photo.setZoom(albumData.getContentData(position).getScale());
//                    viewholder.imageView_Photo.setScrollPosition(albumData.getContentData(position).getPositionX(), albumData.getContentData(position).getPositionY());
//                }
//            });
            Glide.with(mContext).load(imgPath).fitCenter().error(R.drawable.no_image).into(new SimpleTarget<GlideDrawable>((int) imageWidth, (int) imageHeight) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    viewholder.imageView_Photo.setImageDrawable(resource);
                    viewholder.imageView_Photo.setRotation(albumData.getContentData(position).getRotation());
                    viewholder.imageView_Photo.setZoom(albumData.getContentData(position).getScale());
                    viewholder.imageView_Photo.setScrollPosition(albumData.getContentData(position).getPositionX(), albumData.getContentData(position).getPositionY());
                }
            });
        } else
            Glide.with(mContext).load(imgPath).error(R.drawable.no_image).fitCenter().centerCrop().error(R.drawable.no_image).into(viewholder.imageView_Photo);

        viewholder.itemView.setTag(imgPath);

    }


    @Override
    public int getItemCount() {
        return albumData.getAlbumSize();
    }

    public void refreshAlbum(AlbumData albumData) {
        this.albumData = albumData;
    }


}

class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CardView cv;
    public TouchImageView imageView_Photo;
    public TextView textView_Content;
    public TextView tv_Date;
    public ImageView btn_moreContent;
    private final OnItemClickListener onItemClickListener;

    public AlbumViewHolder(View itemView, OnItemClickListener onItemClickListener, Context mContext) {
        super(itemView);

        this.onItemClickListener = onItemClickListener;
        cv = (CardView) itemView.findViewById(R.id.album_view_item_card_view);
        imageView_Photo = (TouchImageView) itemView.findViewById(R.id.album_view_item_img1);
        imageView_Photo.setOnClickListener(this);
        imageView_Photo.setZoomable(false);

        textView_Content = (TextView) itemView.findViewById(R.id.album_view_item_content1);
        tv_Date = (TextView) itemView.findViewById(R.id.album_view_item_tv_date);

        btn_moreContent = (ImageView) itemView.findViewById(R.id.album_view_item_btn_more_content);
        btn_moreContent.setImageDrawable(Utils.btnSelector(new IconicsDrawable(mContext, FontAwesome.Icon.faw_commenting).color(Color.DKGRAY).sizeDp(18), new IconicsDrawable(mContext, FontAwesome.Icon.faw_commenting).color(Color.GRAY).sizeDp(18)));
        btn_moreContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onClick(v, getLayoutPosition());
    }
}

