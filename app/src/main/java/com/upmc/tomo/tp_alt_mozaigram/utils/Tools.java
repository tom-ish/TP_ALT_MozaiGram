package com.upmc.tomo.tp_alt_mozaigram.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;

import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Tomo on 27/03/2018.
 */

public class Tools {
    public static String saveToInternalStorage(Bitmap bitmapImage, Context context){
        Long currentTimestamp = System.currentTimeMillis()/1000;
        String currentFilename = Persists.APP_SIGNATURE + currentTimestamp.toString() + Persists.IMG_EXTENSION;
        ContextWrapper cw = new ContextWrapper(context);


        // path to /data/data/yourapp/app_data/mozaigram_gallery
        File directory = cw.getDir(Persists.APP_IMAGES_STORAGE_DIR_PATH, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,currentFilename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public static String saveToExternalStorage(Bitmap bitmapImage, Context context){
        Long currentTimestamp = System.currentTimeMillis()/1000;
        String currentFilename = Persists.APP_SIGNATURE + currentTimestamp.toString() + Persists.IMG_EXTENSION;
        ContextWrapper cw = new ContextWrapper(context);


        // path to /data/data/yourapp/app_data/mozaigram_gallery
        File directory = new File(Environment.getExternalStorageDirectory(), Persists.APP_IMAGES_STORAGE_DIR_PATH);
        if(!directory.exists())
            directory.mkdir();

        // Create imageDir
        File mypath = new File(directory,currentFilename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }
}
