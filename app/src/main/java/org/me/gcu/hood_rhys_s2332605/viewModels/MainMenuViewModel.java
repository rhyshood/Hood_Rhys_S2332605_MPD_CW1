///////////////////////////////////////////////
// Name                 Rhys Hood            //
// Student ID           S2332605             //
// Programme of Study   BSc (Hons) Computing //
///////////////////////////////////////////////
package org.me.gcu.hood_rhys_s2332605.viewModels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.NetworkManager;

public class MainMenuViewModel extends AppCompatActivity implements View.OnClickListener {

    private Button threeDayBtn;
    private Button latestBtn;
    private Button exitBtn;
    private Intent networkManager;
    private Handler handler = new Handler();
    private boolean dialogOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_view);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        networkManager = new Intent(this, NetworkManager.class);
        findElements();
        setListeners();
    }

    private void startNetworkListener(){
        startService(networkManager);
        IntentFilter filter = new IntentFilter("network_status");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkStatusReceiver, filter);
    }



    @Override
    public void onClick(View v) {
        if (v == threeDayBtn){
            Intent threeDayView = new Intent(MainMenuViewModel.this, ThreeDayViewModel.class);
            stopService(networkManager);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStatusReceiver);
            this.finish();
            startActivity(threeDayView);
        } else if (v == latestBtn){
            Intent latestView = new Intent(MainMenuViewModel.this, LatestObservationViewModel.class);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStatusReceiver);
            stopService(networkManager);
            startActivity(latestView);
            this.finish();
        } else if (v == exitBtn) {
            stopService(networkManager);
            this.finish();
        }
    }

    private void findElements(){
        threeDayBtn = findViewById(R.id.threeDayBtn);
        latestBtn = findViewById(R.id.latestBtn);
        exitBtn = findViewById(R.id.exitBtn);
    }

    private void setListeners(){
        threeDayBtn.setOnClickListener(this);
        latestBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        startNetworkListener();
    }

    private void displayNoInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuViewModel.this);
        builder.setMessage("No Internet Connection!"); //message
        builder.setCancelable(false);
        builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startNetworkListener();
                        dialogOpen = false;
                    }
                }, 1000);
            }
        });

        builder.setPositiveButton("Exit App", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainMenuViewModel.this.finish(); //exits app
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = intent.getBooleanExtra("is_connected", false);
            if (!isConnected) {
                stopService(networkManager);
                if(!dialogOpen){
                    displayNoInternet();
                    dialogOpen = true;
                }
            }
        }
    };

}