package com.eme22.kumaanime.AppUtils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

public class Network {

    /**
     * @author Dar
     */

    public enum ConnectionType{NO_CONNECTION,WIFI,CELLULAR}
    private volatile static ConnectionType type=ConnectionType.CELLULAR;

    public static ConnectionType getType() {
        return type;
    }
    public static void setType(ConnectionType x){
        Log.d("new Status: " , String.valueOf(x));
        type = x;
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @SuppressWarnings("deprecation")
    private static ConnectionType getConnectivityPreLollipop(ConnectivityManager cm){
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork==null)return ConnectionType.WIFI;
        if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            return ConnectionType.CELLULAR;

        return ConnectionType.WIFI;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static ConnectionType getConnectivityPostLollipop(ConnectivityManager cm, android.net.Network network){
        NetworkCapabilities capabilities=cm.getNetworkCapabilities(network);
        if(capabilities==null){return ConnectionType.WIFI;}
        if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            return ConnectionType.CELLULAR;
        return ConnectionType.WIFI;
    }

    @SuppressWarnings("deprecation")
    public static void initConnectivity(@NonNull Context context){
        context=context.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET);
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

            ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull android.net.Network network) {
                    super.onAvailable(network);
                    setType(getConnectivityPostLollipop(cm,network));
                }
            };
            cm.registerNetworkCallback(builder.build(), callback);
            android.net.Network[]networks=cm.getAllNetworks();
            if(networks.length>0)
                setType(getConnectivityPostLollipop(cm,networks[0]));
        } else {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    setType(getConnectivityPreLollipop(cm));
                }
            };
            context.registerReceiver(receiver, filter);
            setType(getConnectivityPreLollipop(cm));
        }
    }

}
