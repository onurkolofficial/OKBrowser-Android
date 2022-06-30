package com.onurkol.app.browser.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionController {
    private static boolean networkConnected=false;

    public static void startSession(Context context){
        ConnectivityManager connectManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectManager.getActiveNetworkInfo();
        if(activeNetwork!=null) {
            /*
             *
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                // Connected MOBILE DATA
            }
            else if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                // Connected WIFI
            }
            */
            networkConnected = true;
        }
        else
            networkConnected=false;
    }

    public static boolean isConnected(){
        return networkConnected;
    }
}
