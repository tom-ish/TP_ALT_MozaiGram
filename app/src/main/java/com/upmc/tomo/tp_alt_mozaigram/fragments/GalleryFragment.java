package com.upmc.tomo.tp_alt_mozaigram.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.adapter.SingleAlbumAdapter;
import com.upmc.tomo.tp_alt_mozaigram.task.LoadImagesTask;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Tomo on 28/03/2018.
 */

public class GalleryFragment extends Fragment {
    final String TAG = GalleryFragment.class.getSimpleName();

    GridView galleryGridView;
    LoadImagesTask loadImagesTask;
    ArrayList<String> imageList = new ArrayList<>();
    String album_name = "My Mozaik";
    SingleAlbumAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SingleAlbumAdapter(getActivity(), imageList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_gallery, container, false);
        galleryGridView = view.findViewById(R.id.galleryGridView);
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if (dp < 360) {
            dp = (dp - 17) / 2;
            float px = Utils.convertDpToPixel(dp, getActivity());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        new LoadImagesTask(imageList, adapter, galleryGridView, getFragmentManager()).execute();
        adapter.notifyDataSetChanged();
        return view;
    }
}
