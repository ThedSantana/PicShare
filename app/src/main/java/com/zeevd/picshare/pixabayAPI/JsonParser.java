package com.zeevd.picshare.pixabayAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zz on 2/7/16.
 */
public class JsonParser {

    JSONObject jsonObj;

    JsonParser(String json) throws JSONException {
        jsonObj = new JSONObject(json);
    }

    public ArrayList<String> getImageURL(boolean previewStyleImage) throws JSONException {
        ArrayList<String> imageUrls = new ArrayList<String>();

        String targetKey;
        if (previewStyleImage) targetKey = "previewURL";
        else targetKey = "webformatURL";

        JSONArray resultsArr = jsonObj.getJSONArray("hits");

        for (int i=0; i<resultsArr.length(); i++){
            JSONObject resultEntry = resultsArr.getJSONObject(i);
            String imageUrl = resultEntry.getString(targetKey);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }



}
