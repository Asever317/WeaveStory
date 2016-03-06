package com.asever.weavestory.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.ContentData;
import com.asever.weavestory.imagefilter.IImageFilter;
import com.asever.weavestory.ui.FilterBitmapTransformation;
import com.asever.weavestory.util.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Asever on 2016-03-04.
 */
public class PhotoFilterAdapter extends RecyclerView.Adapter<PhotoFilterHolder> {
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private int itemlayout;

    private ContentData contentData;
    private ArrayList<IImageFilter> filter_List;
    private ArrayList<String> filter_List_Name;

    public PhotoFilterAdapter(Context mContext, ContentData contentData, ArrayList<IImageFilter> filter_List, ArrayList<String> filter_List_Name) {
        this.mContext = mContext;
        this.contentData = contentData;
        this.filter_List = filter_List;
        this.filter_List_Name = filter_List_Name;
        itemlayout = R.layout.photo_filter_rcv_item;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PhotoFilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemlayout, parent, false);
        return new PhotoFilterHolder(view, onItemClickListener, mContext);
    }

    @Override
    public void onBindViewHolder(PhotoFilterHolder holder, int position) {
        if (position == 0) {
            Glide.with(mContext).load(contentData.getOriginalImgPath()).fitCenter().centerCrop().error(R.drawable.no_image).into(holder.imageView_Filter);
            holder.textView_Content.setText(filter_List_Name.get(position));
        } else {
            FilterBitmapTransformation filterBitmapTransformation = new FilterBitmapTransformation(mContext, filter_List.get(position-1));
            Glide.with(mContext).load(contentData.getOriginalImgPath()).fitCenter().transform(filterBitmapTransformation).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                            "onException(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                            "onResourceReady(%s, %s, %s, %s, %s)", resource, model, target, isFromMemoryCache, isFirstResource));
                    return false;
                }
            }).error(R.drawable.no_image).into(holder.imageView_Filter);
            holder.textView_Content.setText(filter_List_Name.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return filter_List_Name.size();
    }
}


class PhotoFilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView imageView_Filter;
    public TextView textView_Content;
    public RelativeLayout filterContainer;
    private final OnItemClickListener onItemClickListener;

    public PhotoFilterHolder(View itemView, OnItemClickListener onItemClickListener, Context mContext) {
        super(itemView);

        this.onItemClickListener = onItemClickListener;
        filterContainer = (RelativeLayout) itemView.findViewById(R.id.filter_item_container);
        filterContainer.setOnClickListener(this);
        imageView_Filter = (ImageView) itemView.findViewById(R.id.filter_item_img);

        textView_Content = (TextView) itemView.findViewById(R.id.filter_item_tv);
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onClick(v, getLayoutPosition());
    }
}
