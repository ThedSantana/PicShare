package com.zeevd.picshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeevd.picshare.pixabayAPI.JsonParser;
import com.zeevd.picshare.pixabayAPI.RestCaller;
import com.zeevd.picshare.pixabayAPI.UriBuilder;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    OnRestComplete onRestComplete;
    RestCaller restCaller;
    JsonParser jsonParser;
    public GridView imageGrid;
    PicAdapter picAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picAdapter = new PicAdapter(getApplicationContext(),new ArrayList<Bitmap>());

        //create the image grid and its adapter
        imageGrid = (GridView) this.findViewById(R.id.gridView);
        imageGrid.setAdapter(picAdapter);

        //Set editor action listener for the query edit text
        EditText queryEditText = (EditText) this.findViewById(R.id.query_text);
        queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    topLevelProcess(v.getText().toString());
                    return true;
                } else return false;
            }
        });

        onRestComplete = new OnRestComplete() {
            @Override
            public void onRestComplete(String json) {
                try {
                    jsonParser = new JsonParser(json);

                    ArrayList<String> previewImageUrls = jsonParser.getImageURLList(true);
                    if (previewImageUrls == null || previewImageUrls.isEmpty()) return;
                    new DownloadImageTask().execute(previewImageUrls);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


//        //Test to create and imageview based on a downloaded image
//        ImageView im1 = (ImageView) this.findViewById(R.id.imageView);
//        new DownloadImageTask((ImageView) im1).execute("http://www.twinkieminions.com/img/selphie.png");

    }


    public void topLevelProcess(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) return;

        UriBuilder uriBuilder = new UriBuilder(searchQuery);
        Uri userSearchUri = uriBuilder.getSearchURI();

        restCaller = new RestCaller(onRestComplete);
        restCaller.execute(userSearchUri);

    }

    public void imageClicked(View view) {
        ImageView imageView = (ImageView) view;
        Bitmap b = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(),  Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        imageView.draw(canvas);
        OutputStream fOut = null;
        new File("/sdcard/temp").mkdir();
        try {
            fOut = new FileOutputStream("/sdcard/temp/image.jpg");
            b.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, b);
        shareIntent.setType("image/jpeg");
        startActivity(shareIntent);
    }


    class DownloadImageTask extends AsyncTask<ArrayList<String>, Void, ArrayList<Bitmap>> {


        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... params) {
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            Bitmap mIcon11 = null;
            for (String urldisplay : params[0]) {
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                bitmaps.add(mIcon11);
            }
            return bitmaps;
        }


        protected void onPostExecute(ArrayList<Bitmap> result) {
            picAdapter.updateImages(result);
            picAdapter.notifyDataSetChanged();
        }
    }

    class PicAdapter extends BaseAdapter {
        private ArrayList<Bitmap> bitmapList;

        public PicAdapter(Context context, ArrayList<Bitmap> bitmapList) {
            if (bitmapList!=null)
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
            if (convertView==null) convertView = new ImageView(getApplicationContext());
            ((ImageView) convertView).setImageBitmap(bitmapList.get(position));
            return convertView;

        }

        public void updateImages(ArrayList<Bitmap> images){
            this.bitmapList = new ArrayList<>();
            this.bitmapList.addAll(images);
            notifyDataSetInvalidated();
        }
    }
}