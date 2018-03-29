package com.upmc.tomo.tp_alt_mozaigram.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.upmc.tomo.tp_alt_mozaigram.mozaik_process.MozaikGenerator;

import static android.view.View.*;

/**
 * Created by Tomo on 25/03/2018.
 */

public class GenerateMozaikTask extends AsyncTask<Bitmap, Void, Bitmap> {
    static final String TAG = GenerateMozaikTask.class.getSimpleName();
    static final double GRAIN_PERCENT = 0.01;

    Bitmap image;
    ImageView originalBitmapImageView, mozaikContentImageView;
    Button generateMozaikButton, saveGeneratedMozaikButton;
    double grain;

    public GenerateMozaikTask(ImageView originalBitmapImageView, ImageView mozaikContentImageView, Button generateMozaikButton, Button saveGeneratedMozaikButton, double grain) {
        this.originalBitmapImageView = originalBitmapImageView;
        this.mozaikContentImageView = mozaikContentImageView;
        this.generateMozaikButton = generateMozaikButton;
        this.saveGeneratedMozaikButton = saveGeneratedMozaikButton;
        this.grain = grain;
    }

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {
        return MozaikGenerator.getMosaicsBitmaps(bitmaps[0], grain);
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.e(TAG, "Mozaik Bitmap generated...");
        this.originalBitmapImageView.setImageBitmap(null);
        this.originalBitmapImageView.setVisibility(GONE);
        this.mozaikContentImageView.setImageBitmap(bitmap);
        this.mozaikContentImageView.setVisibility(VISIBLE);
        this.saveGeneratedMozaikButton.setVisibility(VISIBLE);
        this.generateMozaikButton.setVisibility(GONE);

        Log.e(TAG, "bitmap : [" + bitmap.getWidth() + "," + bitmap.getHeight() + "]");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
