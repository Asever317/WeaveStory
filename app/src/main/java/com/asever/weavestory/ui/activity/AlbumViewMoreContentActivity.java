package com.asever.weavestory.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asever.weavestory.R;
import com.asever.weavestory.helper.AppConfig;
import com.asever.weavestory.ui.BlurBehind;
import com.asever.weavestory.util.FileManager;
import com.asever.weavestory.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by Asever on 2016-02-01.
 */
public class AlbumViewMoreContentActivity extends Activity {

    private ImageView btn_Back;
    private ImageView btn_Save;

    private TextView tv_Content;
    private EditText edt_Content;

    private int setFlag;
    private int contentPosition;
    private int selectPosition;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_view_more_textview);

        tv_Content = (TextView) findViewById(R.id.more_textview_tv_content);
        edt_Content = (EditText) findViewById(R.id.more_textview_edt_content);

        selectPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_ALBUM_POSITION, -1);
        setFlag = getIntent().getIntExtra(ActivityCallKey.MORE_CONTENT, 0);
        contentPosition = getIntent().getIntExtra(ActivityCallKey.SELECT_VIEW_POSITION, -1);

        btn_Save = (ImageView) findViewById(R.id.more_textview_btn_save);
        btn_Save.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_check).color(Color.WHITE).sizeDp(16)));
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.getAllAlbumdata().get(selectPosition).getContentData(contentPosition).setImgContent(edt_Content.getText().toString());
                FileManager.changeAlbumdata(AppConfig.allAlbumdata.get(selectPosition));
                setResult(ActivityCallKey.EDIT_RESULT);
                finish();
            }
        });

        if (setFlag == ActivityCallKey.VIEW_CONTENT) {
            edt_Content.setVisibility(View.GONE);
            tv_Content.setVisibility(View.VISIBLE);
            btn_Save.setVisibility(View.GONE);
            tv_Content.setText(AppConfig.allAlbumdata.get(selectPosition).getContentData(contentPosition).getImgContent());
        } else if (setFlag == ActivityCallKey.EDIT_CONTENT) {
            tv_Content.setVisibility(View.GONE);
            edt_Content.setVisibility(View.VISIBLE);
            btn_Save.setVisibility(View.VISIBLE);
            edt_Content.setText(AppConfig.allAlbumdata.get(selectPosition).getContentData(contentPosition).getImgContent());
            edt_Content.setSelection(edt_Content.length());
        }


        btn_Back = (ImageView) findViewById(R.id.more_textview_btn_back);
        btn_Back.setImageDrawable(Utils.btnSelector(new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.DKGRAY).sizeDp(16), new IconicsDrawable(this, FontAwesome.Icon.faw_angle_left).color(Color.WHITE).sizeDp(16)));
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BlurBehind.getInstance()
                .withAlpha(80)
                .withFilterColor(getResources().getColor(R.color.filterbox_background))
                .setBackground(this);

        setResult(ActivityCallKey.NOT_EDIT_RESULT);
    }
}
