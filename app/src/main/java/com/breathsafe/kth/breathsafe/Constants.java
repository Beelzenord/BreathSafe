package com.breathsafe.kth.breathsafe;

public class Constants {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final String SEARCH_EXTRA = "SEARCH_EXTRA";
    public static final int SEARCH_EXTRA_1 = 1;
    public static final int SEARCH_EXTRA_2 = 2;
    public static final int SEARCH_ACTIVITY_LOCATION = 0;
    public static final String SEARCH_ACTIVITY_CALLBACK_ACTIVITY = "SEARCH_ACTIVITY_CALLBACK_ACTIVITY";
    public static final int SEARCH_ACTIVITY_CALLBACK_MAINACTIVITY = 0;
    public static final int SEARCH_ACTIVITY_CALLBACK_MAPACTIVITY = 1;

    public static long start;

    public static void setStart() {
        start = System.currentTimeMillis();
    }

    public static long getStart() {
        return start;
    }

}
