package com.upmc.tomo.tp_alt_mozaigram;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class GalleryPreviewActivity extends Activity {

    ImageView GalleryPreviewImg;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_preview);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        Glide.with(GalleryPreviewActivity.this)
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);
    }
}
