package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.upmc.tomo.tp_alt_mozaigram.fragments.ImageGridFragment;
import com.upmc.tomo.tp_alt_mozaigram.fragments.MozaikGenerationFragment;
import com.upmc.tomo.tp_alt_mozaigram.fragments.MozaikGenerationFragment_;
import com.upmc.tomo.tp_alt_mozaigram.model.DisplayState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;

/**
 * Created by Tomo on 26/03/2018.
 */

@EActivity(R.layout.home_activity_layout)
public class HomeActivity extends AppCompatActivity {

    @FragmentById
    Fragment currentFragment;

    FragmentManager fragmentManager;

    DisplayState displayedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    public void afterViews() {
        this.fragmentManager = getFragmentManager();
        MozaikGenerationFragment mozaikGenerationFragment = new MozaikGenerationFragment_();
        this.fragmentManager.beginTransaction().replace(R.id.currentFragment, mozaikGenerationFragment).commit();
        this.displayedFragment = DisplayState.GENERATOR;
    }

    @Click
    public void mozaikGenerationFragmentButton() {
        if(displayedFragment == DisplayState.GALLERY) {
            MozaikGenerationFragment mozaikGenerationFragment = new MozaikGenerationFragment_();
            this.fragmentManager.beginTransaction()
                    .replace(R.id.currentFragment, mozaikGenerationFragment)
                    .commit();
            this.displayedFragment = DisplayState.GENERATOR;
        }
    }

    @Click
    public void mozaikGalleryFragmentButton() {
        if(displayedFragment == DisplayState.GENERATOR) {
            ImageGridFragment mozaikGalleryFragment = new ImageGridFragment();
            this.fragmentManager.beginTransaction()
                    .replace(R.id.currentFragment, mozaikGalleryFragment)
                    .commit();
            this.displayedFragment = DisplayState.GALLERY;
        }
    }

}
