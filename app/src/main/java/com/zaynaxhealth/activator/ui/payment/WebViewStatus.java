package com.zaynaxhealth.activator.ui.payment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WebViewStatus {

    private static WebViewStatus instance = new WebViewStatus();
    static Context context;
    private ConnectivityManager connectivityManager;
    private boolean connected = false;

    public static WebViewStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
//            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
