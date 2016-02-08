package com.zeevd.picshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zeevd.picshare.pixabayAPI.RestCaller;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView imageGrid = (GridView) this.findViewById(R.id.gridView);
        imageGrid.setAdapter(new PicAdapter(this));

    }

    class PicAdapter extends BaseAdapter {
        private Context context;

        public PicAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 4;
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
            if (convertView == null) {
                convertView = new ImageView(context);
            }

            ((ImageView)convertView).setImageResource(R.drawable.testpreview);
            return convertView;
        }
    }
}
