package com.upmc.tomo.tp_alt_mozaigram;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class PageMozaikActivity extends AppCompatActivity {
    static final String TAG = PageMozaikActivity.class.getSimpleName();
    //
    private static final int CAMERA = 10;
    private static final int GALLERY = 20;
    private static final int PERMISSIONS = 200;
    private ImageView imageview;
    String mCurrentPhotoPath;

    private static int index_img = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_mozaik);

        imageview = findViewById(R.id.imageView);
        Button choosePhotoBtn = findViewById(R.id.chooseOrTakeBtn);

        choosePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions_tab = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                if(!Utils.hasPermissions(PageMozaikActivity.this, permissions_tab)){
                    ActivityCompat.requestPermissions(PageMozaikActivity.this, permissions_tab, PERMISSIONS);
                }else{
                    showPictureDialog();
                }
            }
        });

        Button accessGallery = findViewById(R.id.accessGalleryBtn);
        accessGallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String[] permissions_tab = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                if(!Utils.hasPermissions(PageMozaikActivity.this, permissions_tab)){
                    ActivityCompat.requestPermissions(PageMozaikActivity.this, permissions_tab, PERMISSIONS);
                }else{
                    Intent intent = new Intent(PageMozaikActivity.this, GalleryActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(PageMozaikActivity.this, "Image Saved to : " + path, Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(PageMozaikActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            //TODO : keep the photo code, by default we keep every photo taken
            Bitmap imageTaken = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageview.setImageBitmap(imageTaken);
            File photo = new File(mCurrentPhotoPath);
            File storageDir = new File(Environment.getExternalStorageDirectory(),Persists.IMAGE_DIRECTORY);
            try{
                Utils.moveFile(photo,storageDir);
            }catch (Exception e){

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    showPictureDialog();
                } else {
                    Toast.makeText(this, "You must accept permissions.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    public void takePhotoFromCamera() {
        //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, CAMERA);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    public String saveImage(Bitmap bitmap) {
        //ContextWrapper cw = new ContextWrapper(getApplicationContext());
        //File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        File directory = new File(Environment.getExternalStorageDirectory(), Persists.IMAGE_DIRECTORY);
        if (!directory.exists() && !directory.mkdir()) {
            Log.wtf(TAG,"Failed to create directory: "+directory.getAbsolutePath());
        }
        Long time = System.currentTimeMillis()/1000;
        File mypath = new File(directory, "IMG_"+time.toString()+".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Log.d(TAG, mypath.getAbsolutePath());
            return mypath.getAbsolutePath();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }

        return "";
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        //File storageDir = new File(Environment.getExternalStorageDirectory(), Persists.IMAGE_DIRECTORY);
        File storageDir = this.getCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",    /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
