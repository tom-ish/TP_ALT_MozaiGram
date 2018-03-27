package com.upmc.tomo.tp_alt_mozaigram.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.task.GenerateMozaikTask;
import com.upmc.tomo.tp_alt_mozaigram.task.SaveGeneratedMozaikTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static android.view.View.*;

/**
 * Created by Tomo on 26/03/2018.
 */

@EFragment(R.layout.mozaik_generation_fragment_layout)
public class MozaikGenerationFragment extends Fragment {
    static final String TAG = MozaikGenerationFragment.class.getSimpleName();

    @ViewById
    Button chooseOrTakeBtn, generateMozaikButton, saveGeneratedMozaikButton;
    @ViewById
    ImageView pickedImage, mozaikImage;

    Bitmap bitmap, generatedMozaik;

    @AfterViews
    public void afterViews() {
        generateMozaikButton.setVisibility(GONE);
        saveGeneratedMozaikButton.setVisibility(GONE);
    }

    @Click
    public void chooseOrTakeBtn() {
        pickedImage.setVisibility(VISIBLE);
        saveGeneratedMozaikButton.setVisibility(GONE);
        mozaikImage.setVisibility(GONE);
        mozaikImage.setImageBitmap(null);

        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Persists.PICK_IMAGE_REQUEST);
    }


    @Click
    public void generateMozaikButton() {
        try {
            this.generatedMozaik = new GenerateMozaikTask(pickedImage, mozaikImage, generateMozaikButton, saveGeneratedMozaikButton).execute(bitmap).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Click
    public void saveGeneratedMozaikButton() {
        if(generatedMozaik != null) {
            Log.d(TAG, "storing generated Mozaik to device storage");
            try {
                String storedMozaikPath = new SaveGeneratedMozaikTask(getActivity().getApplicationContext()).execute(generatedMozaik).get();
                Log.d(TAG, storedMozaikPath);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Persists.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                // bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                Log.e(TAG, uri.getPath());
                pickedImage.setImageBitmap(bitmap);
                generateMozaikButton.setVisibility(VISIBLE);
                saveGeneratedMozaikButton.setVisibility(GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
