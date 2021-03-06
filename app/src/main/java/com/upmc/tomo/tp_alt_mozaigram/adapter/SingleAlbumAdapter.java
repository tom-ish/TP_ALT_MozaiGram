package com.upmc.tomo.tp_alt_mozaigram.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
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
    final static String TAG = SingleAlbumAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<String> data;

    public SingleAlbumAdapter(Context context, ArrayList<String> d) {
        this.context = context;
        this.data = d;
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
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_single_image_row, parent, false);
            holder.image = convertView.findViewById(R.id.galleryImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String fname = data.get(position);
        try {

            Glide.with(context)
                    .load(new File(fname)) // Uri of the picture
                    .into(holder.image);

        } catch (Exception e) {
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
    }
}