package com.asever.weavestory.helper;

import android.graphics.Matrix;
import android.graphics.PointF;

import com.asever.weavestory.datamodel.AlbumData;

import java.util.ArrayList;

/**
 * Created by Asever on 2015-12-14.
 */
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://52.68.223.166/android_login_api/";

    // Server user register url
    public static String URL_REGISTER = "http://52.68.223.166/android_login_api/";

    public static ArrayList<AlbumData> allAlbumdata;

    public static ArrayList<AlbumData> getAllAlbumdata() {
        if (allAlbumdata == null) {
            allAlbumdata = new ArrayList<>();
        }
        return allAlbumdata;
    }

    public static void initAllAlbumdata() {
        if (allAlbumdata == null) {
            allAlbumdata = new ArrayList<>();
        }
    }

    public static boolean isCreate = false;
    public static boolean isAlbumRead = false;
    public static boolean isSaveAlbum = false;

}
