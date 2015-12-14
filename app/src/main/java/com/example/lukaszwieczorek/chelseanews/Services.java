package com.example.lukaszwieczorek.chelseanews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Services {
    public static boolean isEstablishedInternetConnectivity(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
