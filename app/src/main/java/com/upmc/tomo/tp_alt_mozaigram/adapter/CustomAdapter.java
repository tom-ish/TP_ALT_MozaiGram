package com.upmc.tomo.tp_alt_mozaigram.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.upmc.tomo.tp_alt_mozaigram.R;
import com.upmc.tomo.tp_alt_mozaigram.task.DownloadImgBitmapTask;

import java.util.List;

/**
 * Created by Tomo on 12/03/2018.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<Bitmap> bitmaps;

    public CustomAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.images_grid_view_item_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_imageView);
        imageView.setImageBitmap(bitmaps.get(i));
        return view;
    }
}
