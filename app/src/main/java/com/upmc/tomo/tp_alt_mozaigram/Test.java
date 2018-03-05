package com.upmc.tomo.tp_alt_mozaigram;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.upmc.tomo.tp_alt_mozaigram.persists.Persists;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Tomo on 23/02/2018.
 */

@EActivity(R.layout.test)
public class Test extends Activity {

    @ViewById
    TextView testTV;

    @AfterViews
    public void afterViews() {
        Log.e("TEST", Persists.currentUser.toString());
        testTV.setText(Persists.currentUser.toString());
    }

}
