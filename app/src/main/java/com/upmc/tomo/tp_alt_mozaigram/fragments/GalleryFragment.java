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
import android.widget.TextView;
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

    AlertDialog actionDialog;

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

        if(imageList != null) {
            adapter = new SingleAlbumAdapter(view.getContext(), imageList);
            galleryGridView.setAdapter(adapter);
            new LoadImagesTask(imageList, adapter, galleryGridView, getFragmentManager(), getActivity()).execute();
        }
        galleryGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {

                showPictureDialog(index);
                return true;
            }
        });

        return view;
    }

    private void showPictureDialog(final int index) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_picture_dialog, null);
        TextView firstChoice = dialogView.findViewById(R.id.firstChoice);
        TextView secondChoice = dialogView.findViewById(R.id.secondChoice);
        firstChoice.setText(getString(R.string.delete_option));
        secondChoice.setText(getString(R.string.wallpaper_option));
        firstChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePicture(index);
            }
        });
        dialogBuilder.setView(dialogView);
        secondChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWallpaper(index);
            }
        });
        actionDialog = dialogBuilder.create();
        TextView dialogTitle = (TextView) getActivity().getLayoutInflater().inflate(R.layout.dialog_title, null);
        dialogTitle.setText(getString(R.string.choose_action));
        actionDialog.setCustomTitle(dialogTitle);
        actionDialog.setContentView(dialogView);
        actionDialog.show();
    }

    private void deletePicture(final int index) {
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
    }

    private void setWallpaper(final int index) {
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
