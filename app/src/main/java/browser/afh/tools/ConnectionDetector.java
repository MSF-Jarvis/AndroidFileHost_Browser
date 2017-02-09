package browser.afh.tools;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import hugo.weaving.DebugLog;

public class ConnectionDetector {
    private final Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    @DebugLog
    public boolean isConnectingToInternet() {
        if (networkConnectivity()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(
                        "https://www.androidfilehost.com").openConnection());
                urlc.setRequestProperty("User-Agent", "AFHBrowser");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(30000);
                urlc.setReadTimeout(10000);
                urlc.connect();
                return urlc.getResponseCode() == 200;
            } catch (IOException e) {
                Log.d(Constants.TAG, "isConnectingToInternet: " + e.toString());
                return false;
            }
        } else
            return false;
    }

    private boolean networkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}