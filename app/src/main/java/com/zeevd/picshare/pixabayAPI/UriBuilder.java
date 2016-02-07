package com.zeevd.picshare.pixabayAPI;

import android.net.Uri;

import java.util.Map;


public class UriBuilder {
    //Basic URI information
    public final String API_KEY="2019053-608207730a6137da96dd87b74";
    public final String BASE_URL="https://pixabay.com/api/?";

    //Required params
    public final String API_KEY_PARAM="key";
    public final String IMAGE_QUERY_PARAM="q";

    //Optional params
    public final String PAGE_PARAM="page";
    public final String RESULTS_PER_PAGE_PARAM="per_page";

    private Uri searchURI;

    public UriBuilder(String searchQuery){
        searchURI = Uri.parse(BASE_URL); //set up the uri using the base url
        searchURI = searchURI.buildUpon().appendQueryParameter(API_KEY_PARAM,API_KEY) //append the api key param
        .appendQueryParameter(IMAGE_QUERY_PARAM,searchQuery).build(); //append image search query
    }
    public void appendQuery(String paramKey, String paramVal){
        searchURI = searchURI.buildUpon().appendQueryParameter(paramKey,paramVal).build();
    }
    public void appendQuery(Map<String, String> paramKeyVal){
        Uri.Builder temp = searchURI.buildUpon();
        for (String key : paramKeyVal.keySet()){
            temp = temp.appendQueryParameter(key, paramKeyVal.get(key));
        }
        searchURI = temp.build();
    }

    public Uri getSearchURI() {
        return searchURI;
    }
}
