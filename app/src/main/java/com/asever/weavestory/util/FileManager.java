package com.asever.weavestory.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.asever.weavestory.datamodel.AlbumData;
import com.asever.weavestory.helper.AppConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Asever on 2016-01-18.
 */
public class FileManager {
    public static ArrayList<String> fileNeme;

    public static String writeAlbumData(AlbumData albumData) {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        File file;
        File dir;
        String fileName = calendar.getTime().toString();
        String ext = Environment.getExternalStorageState();
        String path;
        if (ext.equals(Environment.MEDIA_MOUNTED))
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        else
            path = Environment.MEDIA_UNMOUNTED;

        dir = new File(path + "/WeaveStory/" + fileName);
        if (!dir.exists())
            dir.mkdirs();

        file = new File(dir + "/Album.dat");
        albumData.setSavedAlbumPath(dir.toString() + "/");

        try {
            ObjectOutputStream outStream;
            outStream = new ObjectOutputStream(new FileOutputStream(file));
            outStream.writeObject(albumData);
            outStream.close();
            return dir.toString() + "/";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean changeAlbumdata(AlbumData albumData) {
        File changePosition = new File(albumData.getSavedAlbumPath() + "Album.dat");
        try {
            ObjectOutputStream outStream;
            outStream = new ObjectOutputStream(new FileOutputStream(changePosition));
            outStream.writeObject(albumData);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean readImagePath() {
        String path;
        String ext = Environment.getExternalStorageState();
        fileNeme = new ArrayList<>();
        if (ext.equals(Environment.MEDIA_MOUNTED))
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        else
            path = Environment.MEDIA_UNMOUNTED;


        File fList = new File(path + "/WeaveStory");
        File[] files = fList.listFiles();

        if (files == null) {
            return false;
        }
        if (files.length < 1) {
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(files[i] + "/Album.dat")));
                AppConfig.allAlbumdata.add((AlbumData) ois.readObject());
                fileNeme.add(files[i].toString() + "/Album.dat");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (ois != null)
                        ois.close();
                } catch (Exception es) {

                }
            } finally {
                try {
                    if (ois != null)
                        ois.close();
                } catch (Exception es) {

                }
            }

        }
        return true;
    }

    public static void deleteAlbum(String deletePath) {
        File deleteFile = new File(deletePath);
        File[] tempFile = deleteFile.listFiles();

        if (tempFile.length > 0) {

            for (int i = 0; i < tempFile.length; i++) {

                if (tempFile[i].isFile()) {
                    tempFile[i].delete();
                } else {
                    deleteAlbum(tempFile[i].getPath());
                }
                tempFile[i].delete();

            }
            deleteFile.delete();

        }
//        if (deleteFile.exists()) {
//            if (deleteFile.delete()) {
//                Log.d("deleteAlbum", "Success");
//            } else
//                Log.d("deleteAlbum", "Fail");
//        }
    }

    public static void removeFilterImg(String path) {
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
            if (deleteFile.delete()) {
                Log.d("deleteAlbum", "Success");
            } else
                Log.d("deleteAlbum", "Fail");
        }
    }

    public static String saveFilterImage(Bitmap bitmap, AlbumData albumdata) {
        File file;
        File dir;
        Calendar calendar = Calendar.getInstance();
        String fileName = calendar.getTime().toString() + ".jpeg";

        dir = new File(albumdata.getSavedAlbumPath() + "FilterImage");
        if (!dir.exists())
            dir.mkdirs();
        file = new File(dir + "/" + fileName);

        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return file.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {

            }
        }
    }
}