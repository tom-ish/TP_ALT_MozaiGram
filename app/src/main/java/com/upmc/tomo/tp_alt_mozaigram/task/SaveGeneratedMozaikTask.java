package com.upmc.tomo.tp_alt_mozaigram.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.upmc.tomo.tp_alt_mozaigram.utils.Tools;

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
        return  Tools.saveToInternalStorage(bitmaps[0], context);
    }
}
