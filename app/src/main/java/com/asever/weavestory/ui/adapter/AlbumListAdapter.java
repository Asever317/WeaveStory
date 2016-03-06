package com.asever.weavestory.ui.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.AlbumData;
import com.asever.weavestory.util.OnItemClickListener;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.util.Util;

import java.util.ArrayList;

/**
 * Created by Asever on 2016-01-18.
 */
public class AlbumListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private Context mContext;

    private ArrayList<AlbumData> mImages;
    public final static int COLOR_ANIMATION_DURATION = 1000;
    private BitmapRequestBuilder<String, PaletteBitmap> glideRequest;

    private int mDefaultTextColor;
    private int mDefaultBackgroundColor;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AlbumListAdapter() {
    }

    public AlbumListAdapter(Context context, RequestManager glide) {
        this.glideRequest = glide
                .fromString()
                .asBitmap()
                .transcode(new PaletteBitmapTranscoder(context), PaletteBitmap.class)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public void updateData(ArrayList<AlbumData> images) {
        this.mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View rowView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_list_item, viewGroup, false);

        //set the mContext
        this.mContext = viewGroup.getContext();

        //get the colors
        mDefaultTextColor = mContext.getResources().getColor(R.color.text_without_palette);
        mDefaultBackgroundColor = mContext.getResources().getColor(R.color.image_without_palette);

        //get the screenWidth :D optimize everything :D
//        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        return new ListViewHolder(rowView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder imagesViewHolder, final int position) {

        imagesViewHolder.imageAuthor.setText(mImages.get(position).getTitle());
        imagesViewHolder.imageDate.setText(mImages.get(position).getPublishedAt());
        imagesViewHolder.imageCount.setText(mImages.get(position).getAlbumSize() + "장");
        //imagesViewHolder.imageView.setDrawingCacheEnabled(true);
        imagesViewHolder.imageView.setImageBitmap(null);

        //reset colors so we prevent crazy flashes :D
        imagesViewHolder.imageAuthor.setTextColor(mDefaultTextColor);
        imagesViewHolder.imageDate.setTextColor(mDefaultTextColor);
        imagesViewHolder.imageCount.setTextColor(mDefaultTextColor);
        imagesViewHolder.imageTextContainer.setBackgroundColor(mDefaultBackgroundColor);

        //cancel any loading images on this view
        Glide.clear(imagesViewHolder.imageView);
        //load the image
        glideRequest
                .load(mImages.get(position).getCoverImg())
                .fitCenter()
                .centerCrop()
                .into(new ImageViewTarget<PaletteBitmap>(imagesViewHolder.imageView) {
                    @Override
                    protected void setResource(PaletteBitmap resource) {
                        super.view.setImageBitmap(resource.bitmap);
                        mImages.get(position).setSwatch(resource.palette);
                        imagesViewHolder.imageAuthor.setTextColor(resource.palette.getTitleTextColor());
                        imagesViewHolder.imageDate.setTextColor(resource.palette.getTitleTextColor());
                        imagesViewHolder.imageCount.setTextColor(resource.palette.getTitleTextColor());
                        animateViewColor(imagesViewHolder.imageTextContainer, mDefaultBackgroundColor, resource.palette.getRgb());
                    }
                });

    }

    public static void animateViewColor(View v, int startColor, int endColor) {

        ObjectAnimator animator = ObjectAnimator.ofObject(v, "backgroundColor",
                new ArgbEvaluator(), startColor, endColor);

        if (Build.VERSION.SDK_INT >= 21) {
            animator.setInterpolator(new PathInterpolator(0.4f, 0f, 1f, 1f));
        }
        animator.setDuration(COLOR_ANIMATION_DURATION);
        animator.start();
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }
}

class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected final FrameLayout imageTextContainer;
    protected final ImageView imageView;
    protected final TextView imageAuthor;
    protected final TextView imageDate;
    protected final TextView imageCount;

    private final OnItemClickListener onItemClickListener;

    public ListViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;

        imageTextContainer = (FrameLayout) itemView.findViewById(R.id.item_image_text_container);
        imageView = (ImageView) itemView.findViewById(R.id.item_image_img);
        imageAuthor = (TextView) itemView.findViewById(R.id.item_image_author);
        imageDate = (TextView) itemView.findViewById(R.id.item_image_date);
        imageCount = (TextView) itemView.findViewById(R.id.item_image_count);

        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onClick(v, getPosition());
    }
}

class PaletteBitmap {
    public final Palette.Swatch palette;
    public final Bitmap bitmap;

    public PaletteBitmap(@NonNull Bitmap bitmap, @NonNull Palette.Swatch palette) {
        this.bitmap = bitmap;
        this.palette = palette;
    }
}

class PaletteBitmapResource implements Resource<PaletteBitmap> {
    private final PaletteBitmap paletteBitmap;
    private final BitmapPool bitmapPool;

    public PaletteBitmapResource(@NonNull PaletteBitmap paletteBitmap, @NonNull BitmapPool bitmapPool) {
        this.paletteBitmap = paletteBitmap;
        this.bitmapPool = bitmapPool;
    }

    @Override
    public PaletteBitmap get() {
        return paletteBitmap;
    }

    @Override
    public int getSize() {
        return Util.getBitmapByteSize(paletteBitmap.bitmap);
    }

    @Override
    public void recycle() {
        if (!bitmapPool.put(paletteBitmap.bitmap)) {
            paletteBitmap.bitmap.recycle();
        }
    }
}

class PaletteBitmapTranscoder implements ResourceTranscoder<Bitmap, PaletteBitmap> {
    private final BitmapPool bitmapPool;

    public PaletteBitmapTranscoder(@NonNull Context context) {
        this.bitmapPool = Glide.get(context).getBitmapPool();
    }

    @Override
    public Resource<PaletteBitmap> transcode(Resource<Bitmap> toTranscode) {
        Bitmap bitmap = toTranscode.get();
        Palette palette = new Palette.Builder(bitmap).generate();
        Palette.Swatch s = palette.getVibrantSwatch();
        if (s == null) {
            s = palette.getDarkVibrantSwatch();
        }
        if (s == null) {
            s = palette.getLightVibrantSwatch();
        }
        if (s == null) {
            s = palette.getMutedSwatch();
        }
        PaletteBitmap result = new PaletteBitmap(bitmap, s);
        return new PaletteBitmapResource(result, bitmapPool);
    }

    @Override
    public String getId() {
        return PaletteBitmapTranscoder.class.getName();
    }
}