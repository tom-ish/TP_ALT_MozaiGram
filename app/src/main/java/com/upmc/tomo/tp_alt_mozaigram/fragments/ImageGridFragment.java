package com.upmc.tomo.tp_alt_mozaigram.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.adapter.CustomAdapter;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.task.DownloadImgBitmapTask;

import org.androidannotations.annotations.EFragment;

import java.util.List;

/**
 * Created by Tomo on 12/03/2018.
 */

public class ImageGridFragment extends Fragment {
    static final String TAG = ImageGridFragment.class.getSimpleName();
    GridView imagesGridView;

    List<String> imagesURLs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_grid_fragment_layout, container, false);
        imagesGridView = view.findViewById(R.id.imagesGrid);
        imagesURLs = Persists.currentUser.getImageURLs();

        for(String s : imagesURLs)
            Log.e(TAG, s);

        new DownloadImgBitmapTask(
                imagesURLs,
                new DownloadImgBitmapTask.IDownloadImgBitmapTaskResponse() {
                    @Override
                    public void processFinish(List<Bitmap> response) {
                        initImagesGridView(response);
                    }
                }
        ).execute();
        return view;
    }

    private void initImagesGridView(List<Bitmap> bitmaps) {
        if(bitmaps == null) return;
        CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext(), bitmaps);
        imagesGridView.setAdapter(customAdapter);
    }

}
