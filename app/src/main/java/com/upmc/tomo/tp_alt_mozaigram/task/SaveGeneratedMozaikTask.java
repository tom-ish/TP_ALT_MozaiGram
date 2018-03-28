package com.upmc.tomo.tp_alt_mozaigram.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.utils.Tools;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Tomo on 27/03/2018.
 */

public class SaveGeneratedMozaikTask extends AsyncTask<Bitmap, Void, String> {
    static final String TAG = SaveGeneratedMozaikTask.class.getSimpleName();

    Context context;

    public SaveGeneratedMozaikTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e(TAG, s);
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        return  Tools.saveToExternalStorage(bitmaps[0], context);
    }
}
