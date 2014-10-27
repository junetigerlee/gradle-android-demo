package cn.com.incito.wisdom.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

public class NetworkUtils {
    public static final int NETWORK_TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static final int NETWORK_TYPE_WIFI = ConnectivityManager.TYPE_WIFI;

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isServiceReachable(Context ctx, int hostAddress) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.requestRouteToHost(connectivityManager
                .getActiveNetworkInfo().getType(), hostAddress);
    }

    public static int getNetworkType(Context con){
        ConnectivityManager cm = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return NETWORK_TYPE_MOBILE;
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo != null && netinfo.isAvailable()) {
            if(netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI;
            } else {
                return NETWORK_TYPE_MOBILE;
            }
        }
        return NETWORK_TYPE_MOBILE;
    }

    public static boolean isWapNetwork() {
        return !TextUtils.isEmpty(getProxyHost());
    }

    @SuppressWarnings("deprecation")
    public static String getProxyHost() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return System.getProperty("http.proxyHost");
        } else {
            return android.net.Proxy.getDefaultHost();
        }
    }

    @SuppressWarnings("deprecation")
    public static int getProxyPort() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Integer.valueOf(System.getProperty("http.proxyPort"));
        } else {
            return Integer.valueOf(android.net.Proxy.getDefaultHost());
        }
    }
}
