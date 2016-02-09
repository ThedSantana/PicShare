package com.zeevd.picshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeevd.picshare.pixabayAPI.RestCaller;
import com.zeevd.picshare.pixabayAPI.UriBuilder;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the image grid and its adapter
        GridView imageGrid = (GridView) this.findViewById(R.id.gridView);
        imageGrid.setAdapter(new PicAdapter(this));

        //Set editor action listener for the query edit text
        EditText queryEditText = (EditText) this.findViewById(R.id.query_text);
        queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    topLevelProcess(v.getText().toString());
                    return true;
                }
                else return false;
            }
        });


//        //Test to create and imageview based on a downloaded image
//        ImageView im1 = (ImageView) this.findViewById(R.id.imageView);
//        new DownloadImageTask((ImageView) im1).execute("http://www.twinkieminions.com/img/selphie.png");

    }

    public void topLevelProcess(String searchQuery) {
        if (searchQuery == null || searchQuery.isEmpty()) return;

        UriBuilder uriBuilder = new UriBuilder(searchQuery);
        Uri userSearchUri = uriBuilder.getSearchURI();
        //todo: kick off next steps


    }

}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }


    protected void onPostExecute(Bitmap result) {
        //pDlg.dismiss();
        bmImage.setImageBitmap(result);
    }
}


class PicAdapter extends BaseAdapter {
    private Context context;

    public PicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;//TODO: change when images become dynamic
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

        ((ImageView) convertView).setImageResource(R.drawable.testpreview);//TODO display real images
        return convertView;
    }
}
