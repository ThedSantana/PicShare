package com.zeevd.picshare.pixabayAPI;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.BuildConfig;
import android.util.Log;

import com.zeevd.picshare.OnRestComplete;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zz on 2/7/16.
 */
public class RestCaller extends AsyncTask<Uri, Void, String> {
    OnRestComplete onRestComplete;

    public RestCaller(OnRestComplete onRestComplete){
        this.onRestComplete = onRestComplete;
    }

    @Override
    protected String doInBackground(Uri... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String responseJson = null;

        try {

            URL url = new URL(params[0].toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            responseJson = buffer.toString();
        } catch (IOException e) {
//            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
//                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
            return responseJson;
        }
    }

    @Override
    protected void onPostExecute(String json) {
        onRestComplete.onRestComplete(json);
    }
}