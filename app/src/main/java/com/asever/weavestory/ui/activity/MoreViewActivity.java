package com.asever.weavestory.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.AlbumData;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.BlurBehind;
import com.asever.weavestory.ui.HackyViewPager;
import com.asever.weavestory.ui.TouchImageView;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.OnBlurCompleteListener;
import com.asever.weavestory.util.Utils;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by Asever on 2016-01-11.
 */

public class MoreViewActivity extends AppCompatActivity {
    private static final String ISLOCKED_ARG = "isLocked";
    private int viewPosition;
    private int selectPosition;
    private boolean isEdited = false;
    private ViewPager mViewPager;
    private MenuItem menuLockItem;

    private ImageView btn_Back;
    private ImageView btn_Remove;
    private ImageView btn_Crop;
    private ImageView btn_Filter;
    private ImageView btn_ChangeContent;

    private TextView tv_TopCount;
    private TextView tv_Content;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_view);

        setResult(ActivityCallKey.NOT_EDIT_RESULT);

        tv_TopCount = (TextView) findViewById(R.id.more_view_tv_top_count);
        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
        viewPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_VIEW_POSITION, -1);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

        btn_Back = (ImageView) findViewById(R.id.more_view_btn_back);
        btn_Back.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(16)));
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_Remove = (ImageView) findViewById(R.id.more_view_btn_photo_remove);
        btn_Remove.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_remove).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_remove).color(Color.WHITE).sizeDp(16)));
        btn_Remove.setOnClickListener(btnClickListener);

        btn_Crop = (ImageView) findViewById(R.id.more_view_btn_crop);
        btn_Crop.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_crop).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_crop).color(Color.WHITE).sizeDp(16)));
        btn_Crop.setOnClickListener(btnClickListener);

        btn_Filter = (ImageView) findViewById(R.id.more_view_btn_edit_filter);
        btn_Filter.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_magic).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_magic).color(Color.WHITE).sizeDp(16)));
        btn_Filter.setOnClickListener(btnClickListener);

        btn_ChangeContent = (ImageView) findViewById(R.id.more_view_btn_edit_content);
        btn_ChangeContent.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_pencil).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_pencil).color(Color.WHITE).sizeDp(16)));
        btn_ChangeContent.setOnClickListener(btnClickListener);

        tv_Content = (TextView) findViewById(R.id.more_view_tv_content);
        mViewPager.setAdapter(new SamplePagerAdapter(AppConfig.allAlbumdata.get(selectPosition), getApplicationContext()));
        mViewPager.setCurrentItem(viewPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                try {
                    tv_TopCount.setText(Integer.toString(position + 1) + "/" + AppConfig.allAlbumdata.get(selectPosition).getAlbumSize());
                    tv_Content.setText(AppConfig.allAlbumdata.get(selectPosition).getContentData(position).getImgContent());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    viewPosition = position;
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCallKey.MORE_VIEW_CONTENT_ACTIVITY_REQUEST) {
            if (resultCode == ActivityCallKey.EDIT_RESULT) {
                tv_Content.setText(AppConfig.allAlbumdata.get(selectPosition).getContentData(viewPosition).getImgContent());
                isEdited = true;
            }
        } else if (requestCode == ActivityCallKey.PHOTO_FILTER_ACTIVITY_REQUEST) {
            if (resultCode == ActivityCallKey.EDIT_RESULT) {
                mViewPager.getAdapter().notifyDataSetChanged();
                isEdited = true;
            }
        } else if (requestCode == ActivityCallKey.PHOTO_CROP_ACTIVITY_REQUEST) {
            if (resultCode == ActivityCallKey.EDIT_RESULT) {
                isEdited = true;
                onBackPressed();
            }
        }
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.more_view_btn_photo_remove:
                    if (AppConfig.getAllAlbumdata().get(selectPosition).getAlbumSize() > 1) {
                        AppConfig.getAllAlbumdata().get(selectPosition).removeContent(viewPosition);
                        FileManager.changeAlbumdata(AppConfig.getAllAlbumdata().get(selectPosition));
                        mViewPager.getAdapter().notifyDataSetChanged();
                        isEdited = true;
                    } else {
                        Toast.makeText(MoreViewActivity.this, "1장 이하의 사진은 지울 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.more_view_btn_crop:
                    Intent crop_intent = new Intent(MoreViewActivity.this,
                            PhotoEditActivity.class);
                    crop_intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                    crop_intent.putExtra(ActivityCallKey.SELECT_VIEW_POSITION, viewPosition);
                    startActivityForResult(crop_intent, ActivityCallKey.PHOTO_CROP_ACTIVITY_REQUEST);
                    break;
                case R.id.more_view_btn_edit_filter:
                    Intent filter_intent = new Intent(MoreViewActivity.this,
                            PhotoFilterActivity.class);
                    filter_intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                    filter_intent.putExtra(ActivityCallKey.SELECT_VIEW_POSITION, viewPosition);
                    startActivityForResult(filter_intent, ActivityCallKey.PHOTO_FILTER_ACTIVITY_REQUEST);
                    break;
                case R.id.more_view_btn_edit_content:
                    BlurBehind.getInstance().execute(MoreViewActivity.this, new OnBlurCompleteListener() {
                        @Override
                        public void onBlurComplete() {
                            Intent blur_intent = new Intent(getApplicationContext(), AlbumViewMoreContentActivity.class);
                            blur_intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            blur_intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                            blur_intent.putExtra(ActivityCallKey.MORE_CONTENT, ActivityCallKey.EDIT_CONTENT);
                            blur_intent.putExtra(ActivityCallKey.SELECT_VIEW_POSITION, viewPosition);
                            startActivityForResult(blur_intent, ActivityCallKey.MORE_VIEW_CONTENT_ACTIVITY_REQUEST);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (isEdited)
            setResult(ActivityCallKey.EDIT_RESULT);
//        finish();
        super.onBackPressed();
    }

    static class SamplePagerAdapter extends PagerAdapter {
        private AlbumData albumData;
        private Context mContext;

        public SamplePagerAdapter(AlbumData albumData, Context mContext) {
            this.albumData = albumData;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return albumData.getAlbumSize();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            TouchImageView touchImageView = new TouchImageView(container.getContext());
            Glide.with(mContext).load(albumData.getContentData(position).getImgPath()).fitCenter().error(R.drawable.no_image).into(touchImageView);
            container.addView(touchImageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return touchImageView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuLockItem = menu.findItem(R.id.menu_lock);
        toggleLockBtnTitle();
        menuLockItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleViewPagerScrolling();
                toggleLockBtnTitle();
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void toggleViewPagerScrolling() {
        if (isViewPagerActive()) {
            ((HackyViewPager) mViewPager).toggleLock();
        }
    }

    private void toggleLockBtnTitle() {
        boolean isLocked = false;
        if (isViewPagerActive()) {
            isLocked = ((HackyViewPager) mViewPager).isLocked();
        }
        String title = (isLocked) ? getString(R.string.menu_unlock) : getString(R.string.menu_lock);
        if (menuLockItem != null) {
            menuLockItem.setTitle(title);
        }
    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }
}
