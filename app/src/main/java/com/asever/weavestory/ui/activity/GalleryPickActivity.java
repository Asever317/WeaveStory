package com.asever.weavestory.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.datamodel.GalleryData;
import com.asever.weavestory.datamodel.GalleryDataPath;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.adapter.GalleryListAdapter;
import com.asever.weavestory.util.OnItemClickListener;
import com.asever.weavestory.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * Created by Asever on 2015-12-13.
 */
public class GalleryPickActivity extends AppCompatActivity {
    public static AppCompatActivity GALLERYPICKACTIVITY;

    private ImageView btn_Back;
    private Button btn_Check;
    private TextView tv_Title;

    ExpandableListView explv_gallery;
    GalleryListAdapter adapter;
    ArrayList<GalleryData> listData;

    boolean isFirst = true;
    private int what_action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GALLERYPICKACTIVITY = this;
        setContentView(R.layout.activity_gallery_pick);
        what_action = getIntent().getIntExtra(ActivityCallKey.ACTIVITY_WHAT_ACTION, -1);
        init();
    }


    private void init() {
        //확장 리스트 뷰를 설정합니다.
        btn_Back = (ImageView) findViewById(R.id.gallery_pick_btn_back);
        btn_Back.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(16)));
        btn_Back.setOnClickListener(btnOnclickListner);

        btn_Check = (Button) findViewById(R.id.gallery_pick_btn_check);
        btn_Check.setOnClickListener(btnOnclickListner);
        btn_Check.setEnabled(false);

        tv_Title = (TextView) findViewById(R.id.gallery_pick_tv_top_title);
        tv_Title.setText("앨범 가져오기");

        explv_gallery = (ExpandableListView) findViewById(R.id.lvExp);
        explv_gallery.setGroupIndicator(null);
        explv_gallery.setChildIndicator(null);
        explv_gallery.setDivider(null);

        //저장된 사진에 접근하여 데이터를 가져옵니다.(권한 설정필수)
        getGalleryPhotos();

        //가져온 데이터를 어뎁터와 연결하고 리스트뷰에 어뎁터를 설정합니다.
        adapter = new GalleryListAdapter(getApplicationContext(), listData);
        adapter.setOnItemClickListener(onItemClickListener);
        explv_gallery.setAdapter(adapter);

    }

    private View.OnClickListener btnOnclickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.gallery_pick_btn_back)
                finish();
            else if (v.getId() == R.id.gallery_pick_btn_check) {
                if (what_action == ActivityCallKey.ALBUM_MAKE) {
                    Intent intent = new Intent(getApplicationContext(), AlbumMakeConfirmActivity.class);
                    intent.putExtra(ActivityCallKey.ACTIVITY_WHAT_ACTION, ActivityCallKey.ALBUM_MAKE);
                    intent.putExtra(ActivityCallKey.SELECT_IMG_PATHS, adapter.getAllpath());
                    intent.putExtra(ActivityCallKey.SELECT_IMG_DATE, adapter.getPhotoDates());
                    startActivity(intent);
                } else if (what_action == ActivityCallKey.ALBUM_EDIT) {
                    int selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
                    ArrayList<String> selectImgPath = adapter.getAllpath();
                    ArrayList<String> selectImgDate = adapter.getPhotoDates();
                    ArrayList<String> originalImgPath = AppConfig.allAlbumdata.get(selectPosition).getAllImgPath();
                    ArrayList<String> addedSelctImgPath = new ArrayList<>();
                    ArrayList<String> addedSelctImgDate = new ArrayList<>();
                    int select_size = selectImgPath.size();
                    for (int i = 0; i < select_size; i++) {
                        if (!originalImgPath.contains(selectImgPath.get(i))) {
                            addedSelctImgPath.add(selectImgPath.get(i));
                            addedSelctImgDate.add(selectImgDate.get(i));
                        }
                    }

                    if (addedSelctImgPath.size() != 0) {
                        Intent intent = new Intent();
                        intent.putExtra(ActivityCallKey.SELECT_IMG_PATHS, addedSelctImgPath);
                        intent.putExtra(ActivityCallKey.SELECT_IMG_DATE, addedSelctImgDate);
                        setResult(Activity.RESULT_OK, intent);
                    }
                    finish();
                }
            }

        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onClick(View v, int position) {

        }

        @Override
        public void onClick(int checkConut) {
            if (checkConut > 0) {
                btn_Check.setEnabled(true);
                btn_Check.setText("확인 (" + checkConut + ")장");
            } else {
                btn_Check.setEnabled(false);
                btn_Check.setText("확인");
            }
        }
    };


    private void getGalleryPhotos() {
        //각각 날짜별로 보여줄 리스트 그룹 헤더와, 사진들을 보여줄 데이터리스트 입니다.
        listData = new ArrayList<>();

        try {
            //사진에 접근하기 위한 쿼리 입니다. (가져올 데이터는 데이터경로,id,만든날짜)
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATE_TAKEN};
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC"; //날짜 순으로 내림차순 정렬입니다.

            DateFormat sdFormat = new SimpleDateFormat("yyyy.MM.dd");

            //커서를 통해 쿼리된 데이터에 접근합니다.
            Cursor imagecursor = managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {
                GalleryData data = new GalleryData();
                GalleryDataPath dataPath = new GalleryDataPath();
                //가져온 각각의 데이터의 끝까지 처리를 합니다.
                while (imagecursor.moveToNext()) {
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);

                    Long dateTaken = imagecursor.getLong(imagecursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                    String dateFormet = sdFormat.format(dateTaken);

                    if (isFirst) {
                        data.photoDate = dateFormet;
                        dataPath.setdataPath1(imagecursor.getString(dataColumnIndex));
                        dataPath.setImgDate1(dateFormet);
                        dataPath.setViewType(1);
                        isFirst = false;
                    } else if (!data.photoDate.equalsIgnoreCase(dateFormet)) {
                        data.dataPath.add(dataPath);
                        listData.add(data);

                        data = new GalleryData();
                        dataPath = new GalleryDataPath();
                        data.photoDate = dateFormet;
                        dataPath.setdataPath1(imagecursor.getString(dataColumnIndex));
                        dataPath.setImgDate1(dateFormet);
                        dataPath.setViewType(1);

                    } else if (data.photoDate.equalsIgnoreCase(dateFormet)) {
                        if (dataPath.getDataPath2() == null) {
                            dataPath.setdataPath2(imagecursor.getString(dataColumnIndex));
                            dataPath.setImgDate2(dateFormet);
                            dataPath.setViewType(2);
                        } else if (dataPath.getDataPath3() == null) {
                            dataPath.setdataPath3(imagecursor.getString(dataColumnIndex));
                            dataPath.setImgDate3(dateFormet);
                            dataPath.setViewType(3);
                        } else if (!dataPath.getDataPath3().isEmpty()) {
                            data.dataPath.add(dataPath);
                            dataPath = new GalleryDataPath();
                            dataPath.setdataPath1(imagecursor.getString(dataColumnIndex));
                            dataPath.setImgDate1(dateFormet);
                            dataPath.setViewType(1);
                        }
                    }
                }
                data.dataPath.add(dataPath);
                listData.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
