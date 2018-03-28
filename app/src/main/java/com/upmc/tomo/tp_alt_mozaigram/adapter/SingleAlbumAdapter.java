package com.upmc.tomo.tp_alt_mozaigram.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.upmc.tomo.tp_alt_mozaigram.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Tomo on 28/03/2018.
 */

public class SingleAlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<String> data;

    public SingleAlbumAdapter(Activity a, ArrayList<String> d) {
        activity = a;
        data = d;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.activity_single_image_row, parent, false);

            holder = convertView.findViewById(R.id.galleryImage);

            convertView.setTag(holder);
        } else {
            holder = (ImageView) convertView.getTag();
        }

        String fname = data.get(position);
        try {

            Glide.with(activity)
                    .load(new File(fname)) // Uri of the picture
                    .into(holder);

        } catch (Exception e) {
        }
        return convertView;
    }
}