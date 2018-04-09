package com.upmc.tomo.tp_alt_mozaigram;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.upmc.tomo.tp_alt_mozaigram.bridge.IFragmentInteractionListener;
import com.upmc.tomo.tp_alt_mozaigram.fragments.GalleryFragment;
import com.upmc.tomo.tp_alt_mozaigram.fragments.MozaikGenerationFragment;
import com.upmc.tomo.tp_alt_mozaigram.fragments.MozaikGenerationFragment_;
import com.upmc.tomo.tp_alt_mozaigram.model.DisplayState;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;
import com.upmc.tomo.tp_alt_mozaigram.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Tomo on 26/03/2018.
 */

@EActivity(R.layout.home_activity_layout)
public class HomeActivity extends AppCompatActivity implements IFragmentInteractionListener {
    final static String TAG = HomeActivity.class.getSimpleName();

    @FragmentById
    Fragment currentFragment;

    @ViewById
    Button mozaikGenerationFragmentButton;

    @ViewById
    Button mozaikGalleryFragmentButton;

    AlertDialog alertDialog;

    FragmentManager fragmentManager;

    DisplayState displayedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == Persists.PERMISSION_REQUEST){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED
                            && grantResults[1] != PackageManager.PERMISSION_GRANTED){
                this.finish();
                System.exit(0);
            }
        }
    }

    @AfterViews
    public void afterViews() {
        displayDescriptionDialog();
        fragmentManager = getFragmentManager();

        Utils.setupPermissions(this);

        MozaikGenerationFragment mozaikGenerationFragment = new MozaikGenerationFragment_();
        this.fragmentManager.beginTransaction()
                .replace(R.id.currentFragment, mozaikGenerationFragment)
                .addToBackStack(null)
                .commit();
        updateState(DisplayState.GENERATOR);
    }

    @Click
    public void mozaikGenerationFragmentButton() {
        if(displayedFragment != DisplayState.GENERATOR) {
            MozaikGenerationFragment mozaikGenerationFragment = new MozaikGenerationFragment_();
            fragmentManager.beginTransaction()
                    .replace(R.id.currentFragment, mozaikGenerationFragment)
                    .addToBackStack(null)
                    .commit();
            updateState(DisplayState.GENERATOR);
        }
    }

    @Click
    public void mozaikGalleryFragmentButton() {
        if(displayedFragment != DisplayState.GALLERY) {
            GalleryFragment mozaikGalleryFragment = new GalleryFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.currentFragment, mozaikGalleryFragment)
                    .addToBackStack(null)
                    .commit();
            updateState(DisplayState.GALLERY);
        }
    }

    private void displayDescriptionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        dialogView.findViewById(R.id.welcomeOkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        TextView welcomeHeader = (TextView) inflater.inflate(R.layout.welcome_header, null);
        welcomeHeader.setText(getString(R.string.welcome_txt));
        alertDialog = dialogBuilder.create();
        alertDialog.setMessage(getString(R.string.app_description));
        alertDialog.setCustomTitle(welcomeHeader);
        alertDialog.setContentView(dialogView);
        alertDialog.show();
    }


    private void updateState(DisplayState state) {
        this.displayedFragment = state;
        if(state == DisplayState.GENERATOR) {
            mozaikGenerationFragmentButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
            mozaikGalleryFragmentButton.setBackground(getResources().getDrawable(R.drawable.button_normal));
        }
        else if(state == DisplayState.GALLERY) {
            mozaikGalleryFragmentButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
            mozaikGenerationFragmentButton.setBackground(getResources().getDrawable(R.drawable.button_normal));
        }
    }

    @Override
    public void onBackPressed() {
        int count = fragmentManager.getBackStackEntryCount();
        if(count == 0) {
            super.onBackPressed();
        }
        else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onDataPass(DisplayState state) {
        updateState(state);
    }
}
