///////////////////////////////////////////////
// Name                 Rhys Hood            //
// Student ID           S2332605             //
// Programme of Study   BSc (Hons) Computing //
///////////////////////////////////////////////
package org.me.gcu.hood_rhys_s2332605.models;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;

import java.util.Objects;
import java.util.logging.LogRecord;

public class NetworkManager extends Service {

    private static final String TAG = "NetworkService";
    private boolean isRunning = false;
    private final Handler handler = new Handler();
    private String previousStatus = "";
    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        startRepeatingTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkAvailable()) {
                if (!Objects.equals(previousStatus, "Available")) {
                    previousStatus = "Available";
                    Log.d(TAG, "Internet connection available");
                }
                // Notify activities about internet connection
                Intent intent = new Intent("network_status_changed");
                intent.putExtra("is_connected", true);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            } else {
                if(!Objects.equals(previousStatus, "Not Available")){
                    previousStatus = "Not Available";
                    Log.d(TAG, "No internet connection");
                }
                // Notify activities about no internet connection
                Intent intent = new Intent("network_status_changed");
                intent.putExtra("is_connected", false);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
            if(isRunning) {
                handler.postDelayed(this, 3000);
            }
        }
    };

    void startRepeatingTask() {
        runnable.run();
    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
