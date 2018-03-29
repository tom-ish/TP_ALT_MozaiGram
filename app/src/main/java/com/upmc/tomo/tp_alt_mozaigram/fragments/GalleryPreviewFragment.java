package com.upmc.tomo.tp_alt_mozaigram.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;

import java.io.File;

/**
 * Created by Tomo on 28/03/2018.
 */

public class GalleryPreviewFragment extends Fragment {
    final static String TAG = GalleryPreviewFragment.class.getSimpleName();

    ImageView GalleryPreviewImg;
    String path;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gallery_preview, container, false);
        path = getArguments().getString(Persists.SELECTED_MOZAIK_PATH);
        Log.e(TAG, "trying to display " + path);
        Log.e(TAG, path);
        GalleryPreviewImg = view.findViewById(R.id.GalleryPreviewImg);
        Glide.with(this)
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);
        return view;
    }
}
