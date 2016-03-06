package com.asever.weavestory.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.asever.weavestory.R;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.BlurBehind;
import com.asever.weavestory.ui.adapter.AlbumViewAdapter;
import com.asever.weavestory.util.OnBlurCompleteListener;
import com.asever.weavestory.util.OnItemClickListener;
import com.asever.weavestory.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by Asever on 2015-12-20
 */

public class AlbumViewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private AlbumViewAdapter mAlbumViewadapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int selectPosition;
    private int viewPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);
        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
        init();
    }


    public void init() {
        //커스텀 툴바를 설정 하고(xml) 액션바로 연결합니다.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(20), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(20)));
        ActionBar actionBar = getSupportActionBar();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.album_view_swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //액션바에 대한 설정을 합니다.
        try {
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(AppConfig.allAlbumdata.get(selectPosition).getTitle());
            actionBar.setDisplayShowTitleEnabled(true);
        } catch (Exception ignored) {
        }


        mAlbumViewadapter = new AlbumViewAdapter(AppConfig.allAlbumdata.get(selectPosition), R.layout.album_view_item_layout1, getApplicationContext());
        mAlbumViewadapter.setOnItemClickListener(recyclerRowClickListener);
        recyclerView.setAdapter(mAlbumViewadapter);

    }

    private OnItemClickListener recyclerRowClickListener = new OnItemClickListener() {

        @Override
        public void onClick(View v, int position) {
            viewPosition = position;
            if (v.getId() == R.id.album_view_item_img1) {
                Intent intent = new Intent(getApplicationContext(), MoreViewActivity.class);
                intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                intent.putExtra(ActivityCallKey.SELECT_VIEW_POSITION, position);
                startActivityForResult(intent, ActivityCallKey.MORE_VIEW_ACTIVITY_REQUEST);
            } else if (v.getId() == R.id.album_view_item_btn_more_content) {
                BlurBehind.getInstance().execute(AlbumViewActivity.this, new OnBlurCompleteListener() {
                    @Override
                    public void onBlurComplete() {
                        Intent intent = new Intent(getApplicationContext(), AlbumViewMoreContentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                        intent.putExtra(ActivityCallKey.MORE_CONTENT, ActivityCallKey.VIEW_CONTENT);
                        intent.putExtra(ActivityCallKey.SELECT_VIEW_POSITION, viewPosition);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onClick(int checkConut) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album_view, menu);
        MenuItem galleryitem = menu.findItem(R.id.action_edit);
        galleryitem.setIcon(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_book).color(Color.DKGRAY).sizeDp(20), new IconicsDrawable(this, FontAwesome.Icon.faw_book).color(Color.WHITE).sizeDp(24)));
        if (galleryitem != null) {
            galleryitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_edit) {
                        Intent intent = new Intent(AlbumViewActivity.this,
                                AlbumMakeConfirmActivity.class);
                        intent.putExtra(ActivityCallKey.ACTIVITY_WHAT_ACTION, ActivityCallKey.ALBUM_EDIT);
                        intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                        startActivityForResult(intent, ActivityCallKey.ALBUM_MAKE_CONFIRM_ACTIVITY_REQUEST);
                    }
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void refreshList() {
        mAlbumViewadapter.refreshAlbum(AppConfig.allAlbumdata.get(selectPosition));
        mAlbumViewadapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
//        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCallKey.MORE_VIEW_ACTIVITY_REQUEST) {
            if (resultCode == ActivityCallKey.EDIT_RESULT) {
                refreshList();
            }
        } else if (requestCode == ActivityCallKey.ALBUM_MAKE_CONFIRM_ACTIVITY_REQUEST) {
            if (resultCode == ActivityCallKey.EDIT_RESULT) {
                refreshList();
            }
        }

    }

    @Override
    protected void onDestroy() {
        mAlbumViewadapter = null;
        super.onDestroy();
    }
}

