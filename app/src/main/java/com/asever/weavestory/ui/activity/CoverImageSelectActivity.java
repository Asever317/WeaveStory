package com.asever.weavestory.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.asever.weavestory.R;
import com.asever.weavestory.ui.adapter.CoverImageSelectAdapter;
import com.asever.weavestory.util.Utils;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
/**
 * Created by Asever on 2016-01-11.
 */
public class CoverImageSelectActivity extends AppCompatActivity {
    private ImageView iv_MainCover;
    private ImageView btn_Back;
    private ImageView btn_Check;
    private RecyclerView mRecyclerView;
    private ArrayList<String> selectImage;
    private int width, height;
    private String coverImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_image_select);
        iv_MainCover = (ImageView) findViewById(R.id.cover_image_select_maincover);
        selectImage = (ArrayList<String>) getIntent().getSerializableExtra(ActivityCallKey.SELECT_IMG_PATHS);
        coverImg = selectImage.get(0);

        btn_Back = (ImageView) findViewById(R.id.cover_image_select_btn_back);
        btn_Back.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(16)));
        btn_Back.setOnClickListener(btnClickListener);

        btn_Check = (ImageView) findViewById(R.id.cover_image_select_btn_check);
        btn_Check.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
        btn_Check.setOnClickListener(btnClickListener);

        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();  // deprecated
        height = display.getHeight();  // deprecated
//        Picasso.with(getApplicationContext()).load(coverImg).resize(width, height / 2).error(R.drawable.no_image).into(iv_MainCover);
        Glide.with(getApplicationContext()).load(coverImg).fitCenter().error(R.drawable.no_image).into(iv_MainCover);

        CoverImageSelectAdapter coverImageSelectAdapter = new CoverImageSelectAdapter(selectImage);
        coverImageSelectAdapter.setOnItemClickListener(coverItemClickListener);
        mRecyclerView = (RecyclerView) findViewById(R.id.cover_image_select_recv_images);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(coverImageSelectAdapter);


    }

    private CoverImageSelectAdapter.CoverImageClickListener coverItemClickListener = new CoverImageSelectAdapter.CoverImageClickListener() {
        @Override
        public void onItemClick(int position, View v) {
            coverImg = selectImage.get(position);
//            Picasso.with(getApplicationContext()).load(coverImg).resize(width, height / 2).error(R.drawable.no_image).into(iv_MainCover);
            Glide.with(getApplicationContext()).load(coverImg).fitCenter().error(R.drawable.no_image).into(iv_MainCover);
        }
    };

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cover_image_select_btn_back) {
                finish();

            } else if (v.getId() == R.id.cover_image_select_btn_check) {
                Intent intent = new Intent();
                intent.putExtra(ActivityCallKey.SELECT_IMG_COVER, coverImg);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };
}