package com.asever.weavestory.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.AlbumData;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.adapter.PhotoSelectorAdapter;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.Utils;
import com.bumptech.glide.Glide;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Asever on 2015-12-30
 */
public class AlbumMakeConfirmActivity extends AppCompatActivity {
    private GridView gvPhotos;
    private LinearLayout llIncludeBox;
    private PhotoSelectorAdapter photoSelectorAdapter;
    private ArrayList<String> selectImage;
    private ArrayList<String> imageDates;
    private ArrayList<String> addedImg;
    private ArrayList<String> addedDate;
    private ImageView btn_ChangeCover;
    private ImageView btn_Back;
    private ImageView btn_Save;
    private ImageView iv_Cover;
    private TextView tv_MakealbumDate;
    private EditText edt_AlbumTitle;

    private String coverImg;
    private String publishedAt;

    private int selectPosition;
    private boolean isAddedEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_make_confirm);

        initLayout();
        int what_action = getIntent().getIntExtra(ActivityCallKey.ACTIVITY_WHAT_ACTION, -1);

        if (what_action == ActivityCallKey.ALBUM_MAKE) {
            initSetMake();
        } else if (what_action == ActivityCallKey.ALBUM_EDIT) {
            initSetEdit();
        }

    }

    private void initLayout() {
        llIncludeBox = (LinearLayout) findViewById(R.id.in_confim);

        btn_Back = (ImageView) llIncludeBox.findViewById(R.id.album_make_confirm_btn_back);
        btn_Back.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(16)));
        btn_Back.setOnClickListener(btnClickListener);

        btn_Save = (ImageView) llIncludeBox.findViewById(R.id.album_make_confirm_btn_save);
        btn_Save.setOnClickListener(btnClickListener);

        btn_ChangeCover = (ImageView) llIncludeBox.findViewById(R.id.album_make_confirm_btn_changecover);
        btn_ChangeCover.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_photo).color(Color.DKGRAY).sizeDp(28), new IconicsDrawable(this, FontAwesome.Icon.faw_photo).color(Color.WHITE).sizeDp(28)));
        btn_ChangeCover.setOnClickListener(btnClickListener);

        iv_Cover = (ImageView) llIncludeBox.findViewById(R.id.album_make_confirm_img_cover);
        edt_AlbumTitle = (EditText) llIncludeBox.findViewById(R.id.album_make_confirm_etv_albumname);

        gvPhotos = (GridView) llIncludeBox.findViewById(R.id.album_make_confirm_gv_selectimg);

    }

    private void initSetMake() {
        selectImage = (ArrayList<String>) getIntent().getSerializableExtra(ActivityCallKey.SELECT_IMG_PATHS);
        imageDates = (ArrayList<String>) getIntent().getSerializableExtra(ActivityCallKey.SELECT_IMG_DATE);

        coverImg = selectImage.get(0);

        btn_Save.setVisibility(View.GONE);
        Glide.with(getApplicationContext()).load(coverImg).fitCenter().centerCrop().error(R.drawable.no_image).into(iv_Cover);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        publishedAt = CurDateFormat.format(date);
        String currentDate = "생성일 " + publishedAt + " | " + selectImage.size() + "장";
        tv_MakealbumDate = (TextView) llIncludeBox.findViewById(R.id.album_make_confirm_tv_makedate);
        tv_MakealbumDate.setText(currentDate);


        photoSelectorAdapter = new PhotoSelectorAdapter(getApplicationContext(), selectImage, Utils.getWidthPixels(this));
        gvPhotos.setAdapter(photoSelectorAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(28), new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(28)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumData albumData = new AlbumData();
                albumData.setTitle(edt_AlbumTitle.getText().toString());
                albumData.setCoverImg(coverImg);
                albumData.setPublishedAt(publishedAt);
                albumData.setContentDatas(selectImage, imageDates);

                saveAlbum(albumData);
                AppConfig.allAlbumdata.get(selectPosition).setSavedAlbumPath(FileManager.writeAlbumData(albumData));

                showProgressDeterminateDialog();
            }
        });
    }

    private void initSetEdit() {
        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
        AlbumData albumData = AppConfig.allAlbumdata.get(selectPosition);
        selectImage = albumData.getAllImgPath();

        btn_Save.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.GRAY).sizeDp(16));
        btn_Save.setVisibility(View.VISIBLE);
        btn_Save.setEnabled(false);

        coverImg = albumData.getCoverImg();

        Glide.with(getApplicationContext()).load(coverImg).fitCenter().centerCrop().error(R.drawable.no_image).into(iv_Cover);
        publishedAt = albumData.getPublishedAt();
        String currentDate = "생성일 " + publishedAt + " | " + albumData.getAlbumSize() + "장";
        tv_MakealbumDate = (TextView) llIncludeBox.findViewById(R.id.album_make_confirm_tv_makedate);
        tv_MakealbumDate.setText(currentDate);

        edt_AlbumTitle.setText(albumData.getTitle());
        edt_AlbumTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_Save.setImageDrawable(Utils.btnSelector(new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
                btn_Save.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        photoSelectorAdapter = new PhotoSelectorAdapter(getApplicationContext(), selectImage, Utils.getWidthPixels(this));
        gvPhotos.setAdapter(photoSelectorAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_photo).color(Color.DKGRAY).sizeDp(28), new IconicsDrawable(this, FontAwesome.Icon.faw_photo).color(Color.WHITE).sizeDp(28)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlbumMakeConfirmActivity.this,
                        GalleryPickActivity.class);
                intent.putExtra(ActivityCallKey.ACTIVITY_WHAT_ACTION, ActivityCallKey.ALBUM_EDIT);
                intent.putExtra(ActivityCallKey.SELECT_ALBUM_POSITION, selectPosition);
                startActivityForResult(intent, ActivityCallKey.GALLERY_PICK_ACTVITY_REQUEST);
            }
        });
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.album_make_confirm_btn_back) {
                finish();
            } else if (v.getId() == R.id.album_make_confirm_btn_changecover) {
                Intent intent = new Intent(getApplicationContext(), CoverImageSelectActivity.class);
                intent.putExtra(ActivityCallKey.SELECT_IMG_PATHS, selectImage);
                startActivityForResult(intent, ActivityCallKey.COVER_IMAGE_ACTIVITY_COVER_REQUEST);
            } else if (v.getId() == R.id.album_make_confirm_btn_save) {
                AppConfig.allAlbumdata.get(selectPosition).setTitle(edt_AlbumTitle.getText().toString());
                AppConfig.allAlbumdata.get(selectPosition).setCoverImg(coverImg);
                if (isAddedEdit)
                    AppConfig.allAlbumdata.get(selectPosition).setContentDatas(addedImg, addedDate);
                FileManager.changeAlbumdata(AppConfig.allAlbumdata.get(selectPosition));
                setResult(ActivityCallKey.EDIT_RESULT);
                finish();
            }
        }
    };

    private void refreshList() {
        btn_Save.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
//        photoSelectorAdapter.refreshAlbum(selectImage);
//        photoSelectorAdapter.add(addedImg);
        photoSelectorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityCallKey.COVER_IMAGE_ACTIVITY_COVER_REQUEST) {
            if (resultCode == RESULT_OK) {
                btn_Save.setImageDrawable(Utils.btnSelector(new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
                btn_Save.setEnabled(true);
                coverImg = data.getExtras().getString(ActivityCallKey.SELECT_IMG_COVER);
                Glide.with(getApplicationContext()).load(coverImg).fitCenter().error(R.drawable.no_image).into(iv_Cover);
            }
        } else if (requestCode == ActivityCallKey.GALLERY_PICK_ACTVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                isAddedEdit = true;
                addedImg = (ArrayList<String>) data.getSerializableExtra(ActivityCallKey.SELECT_IMG_PATHS);
                addedDate = (ArrayList<String>) data.getSerializableExtra(ActivityCallKey.SELECT_IMG_DATE);
                int loopIndex = addedImg.size();
                for (int i = 0; i < loopIndex; i++) {
                    selectImage.add(addedImg.get(i));
                }
                int size = selectImage.size();
                String currentDate = "생성일 " + publishedAt + " | " + size + "장";
                tv_MakealbumDate.setText(currentDate);
                btn_Save.setImageDrawable(Utils.btnSelector(new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
                btn_Save.setEnabled(true);
                refreshList();
            }
        }
    }

    public void notificationMakeAlbum() {
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(getApplication());
        mCompatBuilder.setSmallIcon(R.mipmap.weavestory_icon);
        mCompatBuilder.setTicker("앨범생성을 완료했습니다.");
        mCompatBuilder.setWhen(System.currentTimeMillis());
        mCompatBuilder.setContentTitle("앨범생성을 완료했습니다.");
        mCompatBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mCompatBuilder.setAutoCancel(true);

        nm.notify(222, mCompatBuilder.build());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                nm.cancel(222);
            }
        }, 1500);
    }

    public void showProgressDeterminateDialog() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("앨범을 저장중입니다.")
                .content("잠시만 기다려주세요...")
                .progress(true, 0)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notificationMakeAlbum();
                        AlbumListActivity.ALBUMLISTACTIVITY.finish();
                        GalleryPickActivity.GALLERYPICKACTIVITY.finish();
                        Intent intent = new Intent(getApplicationContext(), AlbumListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .progressIndeterminateStyle(false)
                .show();

        Handler dialogHandler = new Handler();
        dialogHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                dialog.cancel();
            }
        }, 2000);

    }

    private void saveAlbum(AlbumData data) {
        AppConfig.allAlbumdata.add(data);
    }
}
