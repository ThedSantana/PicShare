package com.zeevd.picshare.pixabayAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public static ArrayList<String> getImageURLList(String json, boolean previewStyleImage) throws JSONException {
        ArrayList<String> imageUrls = new ArrayList<String>();
        JSONObject jsonObj = new JSONObject(json);

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
