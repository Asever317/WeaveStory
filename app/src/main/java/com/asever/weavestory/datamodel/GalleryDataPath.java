package com.asever.weavestory.datamodel;

/**
 * Created by Asever on 2015-12-23.
 */
public class GalleryDataPath {

    private String dataPath1;
    private String dataPath2;
    private String dataPath3;
    private String imgDate1;
    private String imgDate2;
    private String imgDate3;

    private int VIEW_TYPE;


    public boolean isSeleted = false;
    public boolean isSeleted2 = false;
    public boolean isSeleted3 = false;

    public void setViewType(int viewType) {
        this.VIEW_TYPE = viewType;
    }

    public void setdataPath1(String imgPath) {
        this.dataPath1 = "file://" + imgPath;
    }

    public void setdataPath2(String imgPath) {
        this.dataPath2 = "file://" + imgPath;
    }

    public void setdataPath3(String imgPath) {
        this.dataPath3 = "file://" + imgPath;
    }

    public void setImgDate1(String date) {
        this.imgDate1 = date;
    }

    public void setImgDate2(String date) {
        this.imgDate2 = date;
    }

    public void setImgDate3(String date) {
        this.imgDate3 = date;
    }

    public String getDataPath1() {
        return this.dataPath1;
    }

    public String getDataPath2() {
        return this.dataPath2;
    }

    public String getDataPath3() {
        return this.dataPath3;
    }

    public String getImgDate1() {
        return imgDate1;
    }

    public String getImgDate2() {
        return imgDate2;
    }

    public String getImgDate3() {
        return imgDate3;
    }

    public int getVIEW_TYPE() {
        return this.VIEW_TYPE;
    }
}
