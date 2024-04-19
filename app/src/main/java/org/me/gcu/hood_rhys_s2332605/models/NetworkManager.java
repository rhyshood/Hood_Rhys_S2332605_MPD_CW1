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
    // Initializes Variables
    private static final String TAG = "NetworkService";
    private boolean isRunning = false;
    private final Handler handler = new Handler();
    private String previousStatus = "";

    // RUNS WHEN INSTANCE CREATED
    @Override
    public void onCreate() {
        super.onCreate();
        // Sets Running to True
        isRunning = true;
        // Begins Network Check
        runnable.run();
    }

    // RUNS WHEN STOP SERVICE IS CALLED
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Sets Running to false
        isRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // NETWORK CHECKER RUNNABLE
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Checks if network is available
            if (isNetworkAvailable()) {
                // If so, checks if network status has changed
                // This check prevents logs being filled with network updates
                if (!Objects.equals(previousStatus, "Available")) {
                    // If so, logs the network change and updates previous state
                    previousStatus = "Available";
                    Log.d(TAG, "Internet connection available");
                }
                // Creates new intent called network_status
                Intent intent = new Intent("network_status");
                // Adds boolean to intent called is_connected that is set to true
                intent.putExtra("is_connected", true);
                // Broadcasts the intent
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            } else {
                // If so, checks if network status has changed
                // This check prevents logs being filled with network updates
                if(!Objects.equals(previousStatus, "Not Available")){
                    // If so, logs the network change and updates previous state
                    previousStatus = "Not Available";
                    Log.d(TAG, "No internet connection");
                }
                // Adds boolean to intent called is_connected that is set to true
                Intent intent = new Intent("network_status");
                // Adds boolean to intent called is_connected that is set to true
                intent.putExtra("is_connected", false);
                // Broadcasts the intent
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
            // Checks if Network Checker should still be running
            if(isRunning) {
                // If so, runs runnable again after 3 seconds
                handler.postDelayed(this, 3000);
            }
        }
    };

    // CHECKS THAT NETWORK IS AVAILABLE
    boolean isNetworkAvailable() {
        // Fetches network connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Fetches current network information
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        // Returns true if there is any information available and activeNetworkInfo.isConnected returns true
        // False for either of these means there is not a valid connection
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
