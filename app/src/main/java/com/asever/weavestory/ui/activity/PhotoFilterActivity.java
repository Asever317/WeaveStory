package com.asever.weavestory.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asever.weavestory.R;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.imagefilter.AutoAdjustFilter;
import com.asever.weavestory.imagefilter.BlackWhiteFilter;
import com.asever.weavestory.imagefilter.ColorToneFilter;
import com.asever.weavestory.imagefilter.GaussianBlurFilter;
import com.asever.weavestory.imagefilter.IImageFilter;
import com.asever.weavestory.imagefilter.RainBowFilter;
import com.asever.weavestory.imagefilter.SaturationModifyFilter;
import com.asever.weavestory.imagefilter.SepiaFilter;
import com.asever.weavestory.imagefilter.SoftGlowFilter;
import com.asever.weavestory.ui.FilterBitmapTransformation;
import com.asever.weavestory.ui.adapter.PhotoFilterAdapter;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.OnItemClickListener;
import com.asever.weavestory.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
/**
 * Created by Asever on 2016-02-01.
 */
public class PhotoFilterActivity extends AppCompatActivity {

    private ImageView ivMainPhoto;
    private ImageView btn_Back;
    private ImageView btn_Save;
    private PhotoFilterAdapter mPhotoFilterAdapter;

    private TextView tv_Ttile;
    private ArrayList<IImageFilter> filterList;
    private ArrayList<String> filterList_Name;

    private Bitmap filterBitmap;
    Utils.Filter filter;

    private int selectPosition;
    private int viewPosition;
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter);

        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
        viewPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_VIEW_POSITION, -1);

        tv_Ttile = (TextView) findViewById(R.id.photo_filter_top_lbl);
        tv_Ttile.setText("사진 편집");
        btn_Back = (ImageView) findViewById(R.id.photo_filter_back_button);
        btn_Save = (ImageView) findViewById(R.id.photo_filter_btn_save);
        btn_Save.setEnabled(false);

        btn_Back.setOnClickListener(btnClickListener);
        btn_Save.setOnClickListener(btnClickListener);

        btn_Back.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(16)));
        btn_Save.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16));

        ivMainPhoto = (ImageView) findViewById(R.id.photo_filter_mainphoto);
        initFilterBox();
        Glide.with(getApplicationContext()).load(AppConfig.allAlbumdata.get(selectPosition).getContentData(viewPosition).getImgPath()).into(ivMainPhoto);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.photo_filter_back_button:
                    finish();
                    break;
                case R.id.photo_filter_btn_save:
                    if (filter == Utils.Filter.ORIGINAL) {
                        if (AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getFilterImgPath() != null) {
                            FileManager.removeFilterImg(AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getFilterImgPath().substring(7));
                        }
                        AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setFilterImgPath(null);
                        AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setFilter(filter);
                    } else {
                        if (AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getFilterImgPath() != null) {
                            FileManager.removeFilterImg(AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getFilterImgPath().substring(7));
                        }

                        String filterPath = "file://" + FileManager.saveFilterImage(filterBitmap, AppConfig.allAlbumdata.get(selectPosition));
                        if (filterPath != null) {
                            AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setFilterImgPath(filterPath);
                            AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).setFilter(filter);
                            FileManager.changeAlbumdata(AppConfig.allAlbumdata.get(selectPosition));
                        }
                    }
                    setResult(ActivityCallKey.EDIT_RESULT);
                    finish();
                    break;
            }
        }
    };

    private OnItemClickListener recyclerRowClickListener = new OnItemClickListener() {

        @Override
        public void onClick(View v, int position) {
            switch (position) {
                case 0:
                    Toast.makeText(getApplicationContext(), "원본", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.ORIGINAL;
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "자동조절", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.AUTOADJUST;
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "흑백", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.BLACKWHITE;
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "늦은오후", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.LATEAFTERNOON;
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "그린", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.GREEN;
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "아쿠아", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.AQUA;
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "앤티크", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.ANTIQUE;
                    break;
                case 7:
                    Toast.makeText(getApplicationContext(), "화사", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.BRIGHT;
                    break;
                case 8:
                    Toast.makeText(getApplicationContext(), "선명", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.CLEAR;
                    break;
                case 9:
                    Toast.makeText(getApplicationContext(), "세피아", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.SEPIA;
                    break;
                case 10:
                    Toast.makeText(getApplicationContext(), "블러", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.BLUR;
                    break;
                case 11:
                    Toast.makeText(getApplicationContext(), "레인보우", Toast.LENGTH_SHORT).show();
                    filter = Utils.Filter.RAINBOW;
                    break;
            }
            if (filter == AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition).getFilter()) {
                btn_Save.setEnabled(false);
                btn_Save.setImageDrawable(new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16));
            } else {
                btn_Save.setEnabled(true);
                btn_Save.setImageDrawable(Utils.btnSelector(new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
            }
            if (filter == Utils.Filter.ORIGINAL) {
                Glide.with(getApplicationContext()).load(AppConfig.allAlbumdata.get(selectPosition).getContentData(viewPosition).getOriginalImgPath()).error(R.drawable.no_image).into(ivMainPhoto);
            } else {
                FilterBitmapTransformation filterBitmapTransformation = new FilterBitmapTransformation(getApplicationContext(), filterList.get(filter.ordinal()), width, height);
                Glide.with(getApplicationContext()).load(AppConfig.allAlbumdata.get(selectPosition).getContentData(viewPosition).getOriginalImgPath()).asBitmap().transform(filterBitmapTransformation).error(R.drawable.no_image).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivMainPhoto.setImageBitmap(resource);
                        filterBitmap = resource;
                    }
                });
            }
        }

        @Override
        public void onClick(int checkConut) {

        }
    };


    private void initFilterBox() {
        filterList = new ArrayList<>();
        filterList.add(new AutoAdjustFilter());
        filterList.add(new BlackWhiteFilter());
        filterList.add(new ColorToneFilter(Color.rgb(33, 168, 254), 192));
        filterList.add(new ColorToneFilter(0x00FF00, 100));
        filterList.add(new ColorToneFilter(0xFF0000, 100));
        filterList.add(new ColorToneFilter(0x00FFFF, 100));
        filterList.add(new SoftGlowFilter(10, 0.1f, 0.1f));
        filterList.add(new SaturationModifyFilter());
        filterList.add(new SepiaFilter());
        filterList.add(new GaussianBlurFilter());
        filterList.add(new RainBowFilter());

        filterList_Name = new ArrayList<>();
        filterList_Name.add("원본");
        filterList_Name.add("자동조정");
        filterList_Name.add("흑백");
        filterList_Name.add("늦은오후");
        filterList_Name.add("그린");
        filterList_Name.add("아쿠아");
        filterList_Name.add("앤티크");
        filterList_Name.add("화사");
        filterList_Name.add("선명");
        filterList_Name.add("세피아");
        filterList_Name.add("블러");
        filterList_Name.add("레인보우");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.photo_filter_rcv_fillter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mPhotoFilterAdapter = new PhotoFilterAdapter(getApplicationContext(), AppConfig.getAllAlbumdata().get(selectPosition).getContentData(viewPosition), filterList, filterList_Name);
        mPhotoFilterAdapter.setOnItemClickListener(recyclerRowClickListener);
        recyclerView.setAdapter(mPhotoFilterAdapter);
    }

}
