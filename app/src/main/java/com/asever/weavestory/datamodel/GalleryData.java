package com.asever.weavestory.datamodel;

import java.util.ArrayList;

/**
 * Created by Asever on 2016-01-11.
 */
public class GalleryData {
    public String photoDate;
    public ArrayList<GalleryDataPath> dataPath;

    public GalleryData() {
        // TODO Auto-generated constructor stub
        dataPath = new ArrayList<>();
    }
}
