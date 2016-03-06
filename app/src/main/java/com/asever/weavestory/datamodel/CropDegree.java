package com.asever.weavestory.datamodel;

import java.io.Serializable;

/**
 * Created by Asever on 2016-02-26.
 */
public class CropDegree implements Serializable {
    public int x, y, width, height, originalWidth, originalHeight;

    public CropDegree() {
        x = y = width = height = 0;
    }

    public CropDegree(int x, int y, int width, int height, int originalWidth, int originalHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }
}
