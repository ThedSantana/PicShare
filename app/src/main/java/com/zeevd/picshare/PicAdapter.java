package com.zeevd.picshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PicAdapter extends BaseAdapter {
    private ArrayList<Bitmap> bitmapList;
    private Context context;

    public PicAdapter(Context context, ArrayList<Bitmap> bitmapList) {
        this.context=context;

        if (bitmapList != null)
            this.bitmapList = bitmapList;
        else
            this.bitmapList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = new ImageView(context);
        ((ImageView) convertView).setImageBitmap(bitmapList.get(position));
        return convertView;
    }

    public void updateImages(ArrayList<Bitmap> images) {
        this.bitmapList = new ArrayList<>();
        this.bitmapList.addAll(images);
        notifyDataSetInvalidated();
    }
}

