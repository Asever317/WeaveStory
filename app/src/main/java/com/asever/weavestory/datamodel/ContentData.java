package com.asever.weavestory.datamodel;

import com.asever.weavestory.util.Utils;

import java.io.Serializable;

/**
 * Created by Asever on 2016-02-14.
 */
public class ContentData implements Serializable {
    private String originalImgPath;
    private String filterImgPath;
    private String imgContent;
    private String imgDate;
    private Utils.Filter filter;
    //Crop And Rotate
    private boolean isEdit;
    private float scale;
    private float positionX;
    private float positionY;
    private float rotation;

    private float scale_img_Width;
    private float scale_img_Height;

    public ContentData(String originalImgPath, String imgContent, String imgDate, Utils.Filter filter) {
        this.originalImgPath = originalImgPath;
        this.imgContent = imgContent;
        this.imgDate = imgDate;
        this.filter = filter;
        this.filterImgPath = null;
        isEdit = false;
        scale = 1.0f;
        rotation = 0;

    }

    public String getImgPath() {
        if (filterImgPath != null)
            return filterImgPath;
        else
            return originalImgPath;
    }

    public String getOriginalImgPath() {
        return this.originalImgPath;
    }

    public String getFilterImgPath() {
        return filterImgPath;
    }

    public String getImgContent() {
        return this.imgContent;
    }

    public String getImgDate() {
        return imgDate;
    }

    public Utils.Filter getFilter() {
        return filter;
    }

    public float getScale() {
        return scale;
    }

    public float getRotation() {
        return rotation;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getScale_img_Width() {
        return scale_img_Width;
    }

    public float getScale_img_Height() {
        return scale_img_Height;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setOriginalImgPath(String imgPath) {
        this.originalImgPath = imgPath;
    }

    public void setFilterImgPath(String imgPath) {
        this.filterImgPath = imgPath;
    }

    public void setImgContent(String content) {
        this.imgContent = content;
    }

    public void setFilter(Utils.Filter filter) {
        this.filter = filter;
    }

    public void setScale(float scale) {
        this.scale = scale;
        isEdit = true;
    }

    public void setScalePosition(float scalePositionX, float scalePositionY) {
        this.positionX = scalePositionX;
        this.positionY = scalePositionY;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        isEdit = true;
    }

    public void setScale_img_Width(float scale_img_Width) {
        this.scale_img_Width = scale_img_Width;
    }

    public void setScale_img_Height(float scale_img_Height) {
        this.scale_img_Height = scale_img_Height;
    }

    public boolean isEdit() {
        return isEdit;
    }

}
