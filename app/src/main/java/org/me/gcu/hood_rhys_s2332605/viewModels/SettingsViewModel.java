package org.me.gcu.hood_rhys_s2332605.viewModels;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.NetworkManager;

public class SettingsViewModel extends AppCompatActivity implements View.OnClickListener {

    private Button returnBtn;
    private Intent networkManager;
    private Handler handler = new Handler();
    private boolean dialogOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_view);
        networkManager = new Intent(this, NetworkManager.class);
        findElements();
        assignListeners();
    }

    @Override
    public void onClick(View v) {
        if (v == returnBtn){
            Intent mainMenu = new Intent(SettingsViewModel.this, MainMenuViewModel.class);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStatusReceiver);
            stopService(networkManager);
            startActivity(mainMenu);
        }
    }

    private void findElements(){
        returnBtn = findViewById(R.id.returnBtn);
    }

    private void assignListeners(){
        returnBtn.setOnClickListener(this);
        startNetworkListener();
    }

    private void startNetworkListener(){
        startService(networkManager);
        IntentFilter filter = new IntentFilter("network_status_changed");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkStatusReceiver, filter);
    }
    private void displayNoInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsViewModel.this);
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
                }, 2000);
            }
        });

        builder.setPositiveButton("Exit App", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SettingsViewModel.this.finish(); //exits app
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