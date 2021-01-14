package com.example.titche.moneyconverter;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    Context context;

    public ConnectionDetector(Context context){
        this.context=context;
    }
    /*
    * Method who detect if the network is ok
     */
    public boolean isConnected(){
        ConnectivityManager connectivity =(ConnectivityManager)context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity != null){
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo != null){
                if(networkInfo.getState()==NetworkInfo.State.CONNECTED){
                    return  true;
                }
            }
        }
        return false;
    }
}
