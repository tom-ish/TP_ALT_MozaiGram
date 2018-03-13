package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.upmc.tomo.tp_alt_mozaigram.fragments.ImageGridFragment;
import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


/**
 * Created by Tomo on 06/03/2018.
 */

@EActivity(R.layout.main_container_activity_layout)
public class MainContainerActivity extends Activity {
    static final String TAG = MainContainerActivity.class.getSimpleName();



    // Les infos liees au compte de l'User sont accessibles depuis Persist.currentUser
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, Persists.currentUser.toString());

    }


}
