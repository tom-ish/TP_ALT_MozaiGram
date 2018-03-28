package com.upmc.tomo.tp_alt_mozaigram.task;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.adapter.SingleAlbumAdapter;
import com.upmc.tomo.tp_alt_mozaigram.fragments.GalleryPreviewFragment;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;

import java.io.File;
import java.util.List;

/**
 * Created by Tomo on 28/03/2018.
 */

public class LoadImagesTask extends AsyncTask<String, Void, Void> {

    Context context;
    FragmentManager fragmentManager;
    List<String> imageList;
    SingleAlbumAdapter adapter;
    GridView galleryGridView;

    public LoadImagesTask(List<String> imageList, SingleAlbumAdapter adapter, GridView galleryGridView, FragmentManager fragmentManager) {
        this.imageList = imageList;
        this.adapter = adapter;
        this.galleryGridView = galleryGridView;
        this.fragmentManager = fragmentManager;
    }

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
        galleryGridView.setAdapter(adapter);
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                GalleryPreviewFragment galleryPreviewFragment = new GalleryPreviewFragment();
                Bundle args = new Bundle();
                args.putString(Persists.SELECTED_MOZAIK_PATH, imageList.get(+position));
                galleryPreviewFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.currentFragment, galleryPreviewFragment);
            }
        });
    }
}