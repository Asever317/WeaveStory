package com.asever.weavestory.datamodel;


import android.support.v7.graphics.Palette;

import com.asever.weavestory.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Asever on 2015-12-02.
 */

public class AlbumData implements Serializable {
    String title;
    String ID;
    String publishedAt;
    String coverImg;
    private String savedAlbumPath;

    transient private Palette.Swatch swatch;

    private ArrayList<ContentData> contentDatas;

    public AlbumData(String title, String id,
                     String publishedAt) {
        super();
        this.title = title;
        this.ID = id;
        this.publishedAt = publishedAt;
    }

    public AlbumData() {
        super();
        contentDatas = new ArrayList<>();
    }

    public ContentData getContentData(int position) {
        return contentDatas.get(position);
    }

    public void setContentDatas(ArrayList<String> imgPaths, ArrayList<String> imgDates) {
        int size = imgPaths.size();
        for (int i = 0; i < size; i++) {
            ContentData contentData = new ContentData(imgPaths.get(i), "", imgDates.get(i), Utils.Filter.ORIGINAL);
            contentDatas.add(contentData);
        }
    }

    public void setSavedAlbumPath(String path) {
        this.savedAlbumPath = path;
    }

    public String getSavedAlbumPath() {
        return this.savedAlbumPath;
    }

    public void setCoverImg(String imgPath) {
        this.coverImg = imgPath;
    }

    public String getCoverImg() {
        return this.coverImg;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishedAt() {
        return this.publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSwatch(Palette.Swatch swatch) {
        this.swatch = swatch;
    }

    public int getAlbumSize() {
        return this.contentDatas.size();
    }

    public ArrayList<String> getAllImgPath() {
        int roopIndex = getAlbumSize();
        ArrayList<String> allImgPath = new ArrayList<>();
        for (int i = 0; i < roopIndex; i++) {
            allImgPath.add(getContentData(i).getOriginalImgPath());
        }
        return allImgPath;
    }

    public void removeContent(int removePosition) {
        contentDatas.remove(removePosition);
    }

    public Palette.Swatch getSwatch() {
        return swatch;
    }
}