package com.upmc.tomo.tp_alt_mozaigram;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class GalleryActivity extends Activity {
    private final String TAG = GalleryActivity.class.getSimpleName();
    private GridView galleryGridView;
    LoadImages loadImagesTask;
    ArrayList<String> imageList = new ArrayList<>();
    String album_name = "My Mozaik";
    SingleAlbumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        adapter = new SingleAlbumAdapter(GalleryActivity.this, imageList);
        galleryGridView = (GridView) findViewById(R.id.galleryGridView);
        galleryGridView = (GridView) findViewById(R.id.galleryGridView);
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if (dp < 360) {
            dp = (dp - 17) / 2;
            float px = Utils.convertDpToPixel(dp, getApplicationContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        loadImagesTask = new LoadImages();
        loadImagesTask.execute();
        adapter.notifyDataSetChanged();

    }


    public class LoadImages extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageList.clear();
        }

        @Override
        protected Void doInBackground(String... args) {
            File directory = new File(Environment.getExternalStorageDirectory(), Persists.IMAGE_DIRECTORY);
            if (directory.exists()) {
                for (String fname : directory.list()) {
                    String path = Environment.getExternalStorageDirectory() + File.separator + Persists.IMAGE_DIRECTORY + File.separator + File.separator + fname;
                    imageList.add(path);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            //SingleAlbumAdapter adapter = new SingleAlbumAdapter(GalleryActivity.this, imageList);
            galleryGridView.setAdapter(adapter);
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    Intent intent = new Intent(GalleryActivity.this, GalleryPreviewActivity.class);
                    intent.putExtra("path", imageList.get(+position));
                    startActivity(intent);
                }
            });
        }
    }

    class SingleAlbumAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<String> data;

        public SingleAlbumAdapter(Activity a, ArrayList<String> d) {
            activity = a;
            data = d;
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.activity_single_image_row, parent, false);

                holder = convertView.findViewById(R.id.galleryImage);

                convertView.setTag(holder);
            } else {
                holder = (ImageView) convertView.getTag();
            }

            String fname = data.get(position);
            try {

                Glide.with(activity)
                        .load(new File(fname)) // Uri of the picture
                        .into(holder);

            } catch (Exception e) {
            }
            return convertView;
        }
    }
}



