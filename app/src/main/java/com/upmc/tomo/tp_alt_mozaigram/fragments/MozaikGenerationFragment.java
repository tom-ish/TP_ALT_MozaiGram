package com.upmc.tomo.tp_alt_mozaigram.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.bridge.IFragmentInteractionListener;
import com.upmc.tomo.tp_alt_mozaigram.model.DisplayState;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.task.GenerateMozaikTask;
import com.upmc.tomo.tp_alt_mozaigram.task.SaveGeneratedMozaikTask;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_CANCELED;
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

    @ViewById
    SeekBar mozaikGrainSeekbar;

    AlertDialog pictureDialog;

    Bitmap bitmap, generatedMozaik;

    String mCurrentPhotoPath;

    IFragmentInteractionListener fragmentInteractionListener;

    double grain;

    @AfterViews
    public void afterViews() {
        passData(DisplayState.GENERATOR);
        generateMozaikButton.setVisibility(GONE);
        saveGeneratedMozaikButton.setVisibility(GONE);
        grain = mozaikGrainSeekbar.getProgress()/1000.0;
    }

    @Click
    public void chooseOrTakeBtn() {
        pickedImage.setVisibility(VISIBLE);
        saveGeneratedMozaikButton.setVisibility(GONE);
        mozaikImage.setVisibility(GONE);
        mozaikImage.setImageBitmap(null);

        String[] permissions_tab = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!Utils.hasPermissions(getActivity(), permissions_tab)) {
            ActivityCompat.requestPermissions(getActivity(), permissions_tab, Persists.PERMISSIONS);
        } else {
            showPictureDialog();
        }

    }

    private void showPictureDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_picture_dialog, null);
        dialogView.findViewById(R.id.choiceGallery).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhotoFromGallery();
            }
        });
        dialogView.findViewById(R.id.choiceCamera).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        });
        dialogBuilder.setView(dialogView);
        pictureDialog = dialogBuilder.create();
        pictureDialog.setContentView(dialogView);
        pictureDialog.show();

    }

    public void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Show only images, no videos or anything else
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Persists.GALLERY);
    }

    public void takePhotoFromCamera() {
        //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, CAMERA);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Persists.CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Persists.APP_SIGNATURE + timeStamp;
        //File storageDir = new File(Environment.getExternalStorageDirectory(), Persists.IMAGE_DIRECTORY);
        File storageDir = getActivity().getCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                Persists.IMG_EXTENSION,    /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Click
    public void generateMozaikButton() {
        try {
            this.generatedMozaik = new GenerateMozaikTask(pickedImage, mozaikImage, generateMozaikButton, saveGeneratedMozaikButton, mozaikGrainSeekbar, grain, getActivity()).execute(bitmap).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Click
    public void saveGeneratedMozaikButton() {
        if (generatedMozaik != null) {
            Log.d(TAG, "storing generated Mozaik to device storage");
            try {
                String storedMozaikPath = new SaveGeneratedMozaikTask(getActivity().getApplicationContext()).execute(generatedMozaik).get();
                getFragmentManager().beginTransaction()
                        .replace(R.id.currentFragment, new GalleryFragment())
                        .addToBackStack(null)
                        .commit();
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
        if (requestCode == RESULT_CANCELED) return;
        else if (requestCode == Persists.GALLERY) {
            if (data != null && data.getData() != null) {
                try {
                    Uri uri = data.getData();
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                    Log.e(TAG, uri.getPath());
                    pickedImage.setImageBitmap(bitmap);
                    generateMozaikButton.setVisibility(VISIBLE);
                    saveGeneratedMozaikButton.setVisibility(GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == Persists.CAMERA) {
            if (mCurrentPhotoPath != "") {
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                pickedImage.setImageBitmap(bitmap);
                generateMozaikButton.setVisibility(VISIBLE);
                saveGeneratedMozaikButton.setVisibility(GONE);
            }
        }
    }

    @SeekBarProgressChange(R.id.mozaikGrainSeekbar)
    public void onProgressChangeOnMozaikGrainSeekbar (SeekBar seekBar, int progress, boolean fromUser) {
        grain = (seekBar.getMax()-progress)/1000.0;
    }

    @SeekBarTouchStart(R.id.mozaikGrainSeekbar)
    public void onTouchStartMozaikGrainSeekbar () {

    }

    @SeekBarTouchStop(R.id.mozaikGrainSeekbar)
    public void onTouchStopMozaikGrainSeekbar () {
        Toast.makeText(getActivity(), "Seek bar progress is :" + grain,
                Toast.LENGTH_SHORT).show();
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
