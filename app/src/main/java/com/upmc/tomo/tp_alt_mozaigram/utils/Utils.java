package com.upmc.tomo.tp_alt_mozaigram.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by nguye on 25/03/2018.
 */

public class Utils {
    static final String TAG = Utils.class.getSimpleName();

    public static  boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static ArrayList<String> listFiles(File path){
        ArrayList<String> fileNames = new ArrayList<String>();

        if(path.exists()){
            for(String file : path.list()){
                fileNames.add(file);
            }
        }

        if (fileNames.size() > 0) {
            for(int i = 0; i < fileNames.size(); i++)
            {
                //Bitmap mBitmap = BitmapFactory.decodeFile(path.getPath()+"/"+ fileNames[i]);
                ///Now set this bitmap on imageview
                Log.i(TAG,fileNames.get(i));
            }
        }
        return  fileNames;
    }

    public static void moveFile(File file, File dir) throws IOException {
        if(!dir.exists())
            dir.mkdir();
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }

    }
}
