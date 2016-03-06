package com.asever.weavestory.ui;

import android.content.Context;
import android.graphics.Bitmap;

import com.asever.weavestory.imagefilter.FilterImage;
import com.asever.weavestory.imagefilter.IImageFilter;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Asever on 2016-03-03.
 */
public class FilterBitmapTransformation extends BitmapTransformation {
    IImageFilter filter;
    int width, height;
    boolean isMain = false;

    public FilterBitmapTransformation(Context context, IImageFilter filter) {
        super(context);
        this.filter = filter;
    }

    public FilterBitmapTransformation(Context context, IImageFilter filter, int witdh, int height) {
        super(context);
        this.filter = filter;
        this.width = witdh;
        this.height = height;
        isMain = true;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap squaredBitmap = null;

        if (isMain == false) {
            int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
            int x = (toTransform.getWidth() - size) / 2;
            int y = (toTransform.getHeight() - size) / 2;
            squaredBitmap = Bitmap.createBitmap(toTransform, x, y, size, size);
        } else {
            squaredBitmap = toTransform;
        }

        FilterImage img = null;
        try {
            img = new FilterImage(squaredBitmap);
            if (filter != null) {
                img = filter.process(img);
                img.copyPixelsFromBuffer();
            }

            return img.getImage();
        } catch (Exception e) {
            if (img != null && img.destImage.isRecycled()) {
                toTransform.recycle();
                img.destImage.recycle();
                img.destImage = null;
                System.gc();
            }
        } finally {
            if (img != null && img.image.isRecycled()) {
                toTransform.recycle();
                img.image.recycle();
                squaredBitmap.recycle();
                img.image = null;
                System.gc();
            }
        }
        return null;
    }

    @Override
    public String getId() {
        return "Filter" + filter.toString();
    }
}
