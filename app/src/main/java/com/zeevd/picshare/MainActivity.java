package com.zeevd.picshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeevd.picshare.pixabayAPI.JsonParser;
import com.zeevd.picshare.pixabayAPI.UrlBuilder;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = MainActivity.class.getSimpleName();

    public GridView imageGrid;
    PicAdapter picAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picAdapter = new PicAdapter(getApplicationContext(), new ArrayList<Bitmap>());

        //create the image grid and its adapter
        imageGrid = (GridView) this.findViewById(R.id.gridView);
        imageGrid.setAdapter(picAdapter);

        //Set editor action listener for the query edit text
        EditText queryEditText = (EditText) this.findViewById(R.id.query_text);
        queryEditText.setOnEditorActionListener(getTextEditorListener());

    }

    private TextView.OnEditorActionListener getTextEditorListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v != null)
                        buildUrlMakeRestCall(v.getText().toString());
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    private void buildUrlMakeRestCall(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) return;

        UrlBuilder urlBuilder = new UrlBuilder(searchQuery);
        URL url = null;

        try {
            url = urlBuilder.getSearchURL();
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Failed to get search URL: " + e.getMessage());
        }

        new RestCaller().execute(url);

    }

    private void updateImageGrid(ArrayList<Bitmap> images){
        picAdapter.updateImages(images);
        picAdapter.notifyDataSetChanged();
    }


    public void imageClicked(View view) {
        ImageView imageView = (ImageView) view;
        Bitmap b = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
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

    private void getImageUrlsAndDownload(String json){
        try {
            ArrayList<String> previewImageUrls = JsonParser.getImageURLList(json, true);
            ArrayList<String> fullSizeImageUrls = JsonParser.getImageURLList(json, false);
            new DownloadImageTask().execute(previewImageUrls);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Could not get image list based on json string");
            e.printStackTrace();
        }

    }
    class RestCaller extends AsyncTask<URL, Void, String> {
        final String LOG_TAG = RestCaller.class.getSimpleName();

        @Override
        protected String doInBackground(URL... urls) {
            if (urls == null || urls.length == 0) return null;

            HttpURLConnection urlConnection = null;
            StringBuffer jsonBuffer = new StringBuffer();

            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                int temp;
                while ((temp = inputStream.read()) != -1) jsonBuffer.append((char) temp);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to get JSON response for url " + urls[0].toString());
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
                return jsonBuffer == null ? null : jsonBuffer.toString();
            }
        }

        @Override
        protected void onPostExecute(String json) {
            getImageUrlsAndDownload(json);
        }


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


        protected void onPostExecute(ArrayList<Bitmap> images) {
            updateImageGrid(images);
        }
    }


}