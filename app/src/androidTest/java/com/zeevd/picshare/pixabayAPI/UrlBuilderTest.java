package com.zeevd.picshare.pixabayAPI;

import junit.framework.TestCase;

/**
 * Created by zz on 2/7/16.
 */
public class UrlBuilderTest extends TestCase {
    UrlBuilder uri;
    String testQuery="good morning";


    public void setUp() throws Exception {
        super.setUp();
        uri = new UrlBuilder(testQuery);
    }


    public void testAppendQuery() throws Exception {
        UrlBuilder uri2 = new UrlBuilder(testQuery);

        uri.appendQuery(uri.PAGE_PARAM,"2");
        assertEquals(uri.getSearchURL().toString() , uri2.getSearchURL().toString()+"&"+uri2.PAGE_PARAM+"=2");
    }

    public void testAppendQuery1() throws Exception {

    }

    public void testGetSearchURI() throws Exception {
        assertEquals(uri.getSearchURL().toString(), uri.BASE_URL +
                uri.API_KEY_PARAM + "=" + uri.API_KEY + "&" +
                uri.IMAGE_QUERY_PARAM + "=" + testQuery.replaceAll(" ","%20"));
    }

}