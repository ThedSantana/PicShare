package com.zeevd.picshare.pixabayAPI;

import junit.framework.TestCase;

/**
 * Created by zz on 2/7/16.
 */
public class UriBuilderTest extends TestCase {
    UriBuilder uri;
    String testQuery="good morning";


    public void setUp() throws Exception {
        super.setUp();
        uri = new UriBuilder(testQuery);
    }


    public void testAppendQuery() throws Exception {
        UriBuilder uri2 = new UriBuilder(testQuery);

        uri.appendQuery(uri.PAGE_PARAM,"2");
        assertEquals(uri.getSearchURI().toString() , uri2.getSearchURI().toString()+"&"+uri2.PAGE_PARAM+"=2");
    }

    public void testAppendQuery1() throws Exception {

    }

    public void testGetSearchURI() throws Exception {
        assertEquals(uri.getSearchURI().toString(), uri.BASE_URL +
                uri.API_KEY_PARAM + "=" + uri.API_KEY + "&" +
                uri.IMAGE_QUERY_PARAM + "=" + testQuery.replaceAll(" ","%20"));
    }

}