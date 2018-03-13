package com.upmc.tomo.tp_alt_mozaigram.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomo on 12/03/2018.
 */

public class DownloadImgBitmapTask extends AsyncTask<List<String>, Void, List<Bitmap>> {

    List<String> imgURLs;

    public interface IDownloadImgBitmapTaskResponse {
        void processFinish(List<Bitmap> response);
    }

    public IDownloadImgBitmapTaskResponse delegate = null;

    public DownloadImgBitmapTask(List<String> imagesURLs, IDownloadImgBitmapTaskResponse delegate) {
        this.imgURLs = imagesURLs;
        this.delegate = delegate;
    }

    @Override
    protected List<Bitmap> doInBackground(List<String>... arrayLists) {
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for(String url : imgURLs) {
            InputStream inputStream = null;
            try {
                inputStream = new URL(url).openStream();
                bitmaps.add(BitmapFactory.decodeStream(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmaps;
    }

    protected void onPostExecute(List<Bitmap> rslt) {
        delegate.processFinish(rslt);
    }
}
