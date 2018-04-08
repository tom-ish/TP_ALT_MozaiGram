package com.upmc.tomo.tp_alt_mozaigram.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.adapter.SingleAlbumAdapter;
import com.upmc.tomo.tp_alt_mozaigram.bridge.IFragmentInteractionListener;
import com.upmc.tomo.tp_alt_mozaigram.model.DisplayState;
import com.upmc.tomo.tp_alt_mozaigram.task.LoadImagesTask;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Tomo on 28/03/2018.
 */

public class GalleryFragment extends Fragment {
    final String TAG = GalleryFragment.class.getSimpleName();

    GridView galleryGridView;
    ArrayList<String> imageList = new ArrayList<>();
    SingleAlbumAdapter adapter;
    ProgressDialog progress;

    IFragmentInteractionListener fragmentInteractionListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        passData(DisplayState.GALLERY);
        View view = inflater.inflate(R.layout.activity_gallery, container, false);
        galleryGridView = view.findViewById(R.id.galleryGridView);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int iDisplayWidth = metrics.widthPixels;
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if (dp < 360) {
            dp = (dp - 17) / 2;
            float px = Utils.convertDpToPixel(dp, getActivity());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        adapter = new SingleAlbumAdapter(view.getContext(), imageList);
        galleryGridView.setAdapter(adapter);
        new LoadImagesTask(imageList, adapter, galleryGridView, getFragmentManager()).execute();
        galleryGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {

                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
                pictureDialog.setTitle(getString(R.string.choose_action));
                String[] pictureDialogItems = {
                        getString(R.string.delete_option),
                        getString(R.string.wallpaper_option)};
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        String path = imageList.get(index);
                                        File file = new File(path);
                                        boolean deleted = false;
                                        if (file.exists()) {
                                            deleted = file.delete();
                                            imageList.remove(path);
                                        }
                                        if (deleted) {
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getActivity(), getString(R.string.msg_delete_picture), Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                    case 1:

                                        progress = new ProgressDialog(getActivity());

                                        progress.setMessage(getString(R.string.title_progress_dialog));
                                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progress.show();

                                        final Thread t = new Thread() {
                                            @Override
                                            public void run() {
                                                Uri uri = Uri.fromFile(new File(imageList.get(index)));
                                                Bitmap bitmap = null;
                                                try {
                                                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                                                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity());
                                                    wallpaperManager.setBitmap(bitmap);
                                                    progress.dismiss();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        t.start();
                                        break;
                                }
                            }
                        });
                pictureDialog.show();
                return true;
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentInteractionListener) {
            fragmentInteractionListener = (IFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentInteractionListener = null;
    }

    public void passData(DisplayState state) {
        fragmentInteractionListener.onDataPass(state);
    }
}
