package com.asever.weavestory.ui.activity;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.ContentData;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.TouchImageView;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by Asever on 2016-02-11.
 */
public class PhotoEditActivity extends AppCompatActivity {
    private int selectPosition;
    private int viewPosition;
    private ImageView btn_LRotate;
    private ImageView btn_RRotate;
    private TouchImageView photoView;

    private TextView tv_Date;
    private TextView tv_Content;
    private int mRotate;

    boolean isViewWidth = false;
    boolean isImageDrawed = false;

    float viewWidth;
    float imageWidth;
    float imageHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        findViewById(R.id.btn_cancel).setOnClickListener(btnClickListener);
        findViewById(R.id.btn_done).setOnClickListener(btnClickListener);

        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
        viewPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_VIEW_POSITION, -1);

        btn_LRotate = (ImageView) findViewById(R.id.photo_crop_btn_left);
        btn_RRotate = (ImageView) findViewById(R.id.photo_crop_btn_right);
        btn_LRotate.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_rotate_left).color(Color.WHITE).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_rotate_left).color(Color.DKGRAY).sizeDp(16)));
        btn_LRotate.setOnClickListener(btnRotateClickListener);
        btn_RRotate.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_rotate_right).color(Color.WHITE).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_rotate_right).color(Color.DKGRAY).sizeDp(16)));
        btn_RRotate.setOnClickListener(btnRotateClickListener);

        photoView = (TouchImageView) findViewById(R.id.photo_edit_item_img1);

        tv_Date = (TextView) findViewById(R.id.photo_edit_item_tv_date);
        tv_Date.setText(AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getImgDate());
        tv_Content = (TextView) findViewById(R.id.photo_edit_item_content1);
        tv_Content.setText(AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getImgContent());

        if (isViewWidth == false) {
            photoView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    viewWidth = photoView.getWidth();
                    isViewWidth = true;
                    setPhotoViewZoom();
                    photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
        Glide.with(getApplicationContext())
                .load(AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getImgPath()).fitCenter().error(R.drawable.no_image).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                imageWidth = resource.getIntrinsicWidth();
                imageHeight = resource.getIntrinsicHeight();
                isImageDrawed = true;
                setPhotoViewZoom();
                return false;
            }
        }).into(photoView);
    }

    View.OnClickListener btnRotateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.photo_crop_btn_left) {
                if (mRotate == 0)
                    mRotate = 270;
                else
                    mRotate -= 90;
            } else if (v.getId() == R.id.photo_crop_btn_right) {
                if (mRotate == 270)
                    mRotate = 0;
                else
                    mRotate += 90;
            }
            photoView.setRotation(mRotate);
        }
    };

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_cancel) {
                setResult(ActivityCallKey.NOT_EDIT_RESULT);
                finish();
            } else if (v.getId() == R.id.btn_done) {
                AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setScale(photoView.getCurrentZoom());
                AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setRotation(photoView.getRotation());
                AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setScale_img_Width(imageWidth);
                AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setScale_img_Height(imageHeight);
                PointF pointF = photoView.getScrollPosition();
                AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setScalePosition(pointF.x, pointF.y);
                FileManager.changeAlbumdata(AppConfig.allAlbumdata.get(selectPosition));
                setResult(ActivityCallKey.EDIT_RESULT);
                finish();
            }
        }
    };

    private void setPhotoViewZoom() {
        if (isViewWidth && isImageDrawed) {
            ContentData contentData = AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition);
            if (contentData.isEdit()) {
                photoView.setRotation(contentData.getRotation());
                photoView.setZoom(contentData.getScale());
                photoView.setScrollPosition(contentData.getPositionX(), contentData.getPositionY());
            } else {
                try {
                    float scale = viewWidth / imageWidth;
                    photoView.setMinZoom(scale);
                    photoView.setZoom(scale);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
