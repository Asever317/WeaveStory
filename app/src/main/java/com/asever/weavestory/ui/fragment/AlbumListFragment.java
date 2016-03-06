package com.asever.weavestory.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.AlbumData;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.activity.ActivityCallKey;
import com.asever.weavestory.ui.activity.DetailActivity;
import com.asever.weavestory.ui.adapter.AlbumListAdapter;
import com.asever.weavestory.util.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

/**
 * Created by Asever on 2016-01-18.
 */
public class AlbumListFragment extends Fragment {
    public static SparseArray<Bitmap> photoCache = new SparseArray<>(1);
    private AlbumListAdapter mImageAdapter;
    private ImageView img_noAlbum;
    private RecyclerView mImageRecycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
        img_noAlbum = (ImageView) rootView.findViewById(R.id.fragment_images_noalbum);
        img_noAlbum.setImageDrawable(new IconicsDrawable(getActivity().getApplicationContext(), FontAwesome.Icon.faw_image).color(Color.DKGRAY).sizeDp(266));
        mImageRecycler = (RecyclerView) rootView.findViewById(R.id.fragment_last_images_recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        mImageRecycler.setLayoutManager(gridLayoutManager);
        mImageRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

//        mImageAdapter = new AlbumListAdapter();
        mImageAdapter = new AlbumListAdapter(getActivity(), Glide.with(this));
        mImageAdapter.setOnItemClickListener(recyclerRowClickListener);
        mImageRecycler.setAdapter(mImageAdapter);

        if (!AppConfig.allAlbumdata.isEmpty() && !AppConfig.isCreate) {
            showAll();
            AppConfig.isCreate = true;
        } else if (!AppConfig.allAlbumdata.isEmpty())
            showAll();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showAll() {
        if (AppConfig.getAllAlbumdata() != null && AppConfig.getAllAlbumdata().size() > 0) {
            updateAdapter(AppConfig.allAlbumdata);
            img_noAlbum.setVisibility(View.GONE);
            mImageRecycler.setVisibility(View.VISIBLE);
        } else {
            img_noAlbum.setVisibility(View.VISIBLE);
            mImageRecycler.setVisibility(View.GONE);
        }
    }

    private OnItemClickListener recyclerRowClickListener = new OnItemClickListener() {

        @Override
        public void onClick(View v, int position) {

            Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
            detailIntent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, position);

            if (AppConfig.getAllAlbumdata().get(position).getSwatch() != null) {
                detailIntent.putExtra("swatch_title_text_color", AppConfig.getAllAlbumdata().get(position).getSwatch().getTitleTextColor());
                detailIntent.putExtra("swatch_rgb", AppConfig.getAllAlbumdata().get(position).getSwatch().getRgb());
            }

            ImageView coverImage = (ImageView) v.findViewById(R.id.item_image_img);
            if (coverImage == null) {
                coverImage = (ImageView) ((View) v.getParent()).findViewById(R.id.item_image_img);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                if (coverImage.getParent() != null) {
                    ((ViewGroup) coverImage.getParent()).setTransitionGroup(false);
                }
            }

            if (coverImage != null && coverImage.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) coverImage.getDrawable()).getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    photoCache.put(position, bitmap);

                    // Setup the transition to the detail activity
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), coverImage, "cover");
                    startActivityForResult(detailIntent, ActivityCallKey.DETAIL_ACTIVITY_REQUEST, options.toBundle());
                }
            }

        }

        @Override
        public void onClick(int checkConut) {

        }
    };

    private void updateAdapter(ArrayList<AlbumData> images) {
//        mCurrentImages = images;
        mImageAdapter.updateData(images);
        mImageRecycler.scrollToPosition(0);
        /*
        mImageAdapter = new ImageAdapter(images);
        mImageAdapter.setOnItemClickListener(recyclerRowClickListener);
        mImageRecycler.setAdapter(mImageAdapter);
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCallKey.DETAIL_ACTIVITY_REQUEST) {
            if (resultCode == ActivityCallKey.EDIT_RESULT) {
                showAll();
            }

        }
    }
}
