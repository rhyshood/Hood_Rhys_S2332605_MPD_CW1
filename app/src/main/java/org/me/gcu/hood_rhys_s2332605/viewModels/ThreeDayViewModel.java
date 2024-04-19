///////////////////////////////////////////////
// Name                 Rhys Hood            //
// Student ID           S2332605             //
// Programme of Study   BSc (Hons) Computing //
///////////////////////////////////////////////

package org.me.gcu.hood_rhys_s2332605.viewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.NetworkManager;
import org.me.gcu.hood_rhys_s2332605.models.RSSManager;
import org.me.gcu.hood_rhys_s2332605.models.ThreeDayWeather;
import org.me.gcu.hood_rhys_s2332605.models.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

public class ThreeDayViewModel extends AppCompatActivity implements OnClickListener, OnMapReadyCallback
{
    private RSSManager rssManager = new RSSManager();
    // Initialise Text Views
    private TextView locationNameTxt;
    private TextView minTempTxtOne;
    private TextView maxTempTxtOne;
    private TextView dateOne;
    private TextView maxTempTxtTwo;
    private TextView minTempTxtTwo;
    private TextView dateTwo;
    private TextView maxTempTxtThree;
    private TextView minTempTxtThree;
    private TextView dateThree;

    // Initialise Buttons
    private Button locationLeftButton;
    private Button locationRightButton;
    private Button dayOneBtn;
    private Button dayTwoBtn;
    private Button dayThreeBtn;
    private Button returnBtn;

    // Initialise Images
    private ImageView dayOneImg;
    private ImageView dayTwoImg;
    private ImageView dayThreeImg;

    // Initialise Variables
    private String[] locationNames = {"Glasgow","London","New York","Oman","Mauritius","Bangladesh"};
    private String[] locationIDs = {"2648579","2643743","5128581","287286","934154","1185241"};
    private int selectedIndex = 0;
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
    private GoogleMap mMap;
    private Intent networkManager;
    private Handler handler = new Handler();
    private boolean dialogOpen = false;

    private String result;
    private ThreeDayWeather threeDayWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.three_day_view);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            selectedIndex = 0;
        else
            selectedIndex = bundle.getInt("selectedIndex");
        // Set up the raw links to the graphical components
        updateLocation(0);
        networkManager = new Intent(this, NetworkManager.class);
        findElements();
        setListeners();
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void onClick(View v) {
        if (v == locationLeftButton) {
            updateLocation(-1);
        } else if (v == locationRightButton) {
            updateLocation(1);
        } else if (v == returnBtn){
            Intent startMainMenu = new Intent(ThreeDayViewModel.this, MainMenuViewModel.class);
            LocalBroadcastManager.getInstance(this).unregisterReceiver(networkStatusReceiver);
            stopService(networkManager);
            startActivity(startMainMenu);
            this.finish();
        } else if (v == dayOneBtn || v == dayTwoBtn || v == dayThreeBtn) {
           String id = getResources().getResourceName(v.getId());
           id = id.substring(id.lastIndexOf("/") + 1);

           Bundle bundle = new Bundle();
           bundle.putInt("selectedIndex",selectedIndex);
           switch(id){
               case "dayOneBtn":
                   bundle.putInt("selectedDay",1);
                   break;
               case "dayTwoBtn":
                   bundle.putInt("selectedDay",2);
                   break;
               case "dayThreeBtn":
                   bundle.putInt("selectedDay",3);
                   break;
           }
           Intent startDetailedView = new Intent(ThreeDayViewModel.this, DetailedViewModel.class);
           startDetailedView.putExtras(bundle);

           startDetailedView.putExtra("threeDayWeather",threeDayWeather);
           startDetailedView.putExtra("dayOne",threeDayWeather.getFirstDay());
           startDetailedView.putExtra("dayTwo",threeDayWeather.getSecondDay());
           startDetailedView.putExtra("dayThree",threeDayWeather.getThirdDay());
           startActivity(startDetailedView);
           this.finish();
       }
    }

    private void startNetworkListener(){
        startService(networkManager);
        IntentFilter filter = new IntentFilter("network_status_changed");
        LocalBroadcastManager.getInstance(this).registerReceiver(networkStatusReceiver, filter);
    }

    private void findElements(){
        // Text Views
        maxTempTxtOne = findViewById(R.id.dayOneMaxTempTxt);
        maxTempTxtTwo = findViewById(R.id.dayTwoMaxTempTxt);
        maxTempTxtThree = findViewById(R.id.dayThreeMaxTempTxt);
        minTempTxtOne = findViewById(R.id.dayOneMinTempTxt);
        minTempTxtTwo = findViewById(R.id.dayTwoMinTempTxt);
        minTempTxtThree = findViewById(R.id.dayThreeMinTempTxt);
        dateOne = findViewById(R.id.dayOneDateTxt);
        dateTwo = findViewById(R.id.dayTwoDateTxt);
        dateThree = findViewById(R.id.dayThreeDateTxt);

        // Buttons
        dayOneBtn = findViewById(R.id.dayOneBtn);
        dayTwoBtn = findViewById(R.id.dayTwoBtn);
        dayThreeBtn = findViewById(R.id.dayThreeBtn);
        locationLeftButton = findViewById(R.id.locationLeftBtn);
        locationRightButton = findViewById(R.id.locationRightBtn);
        returnBtn = findViewById(R.id.returnBtn);

        // Images
        dayOneImg = findViewById(R.id.dayOneImg);
        dayTwoImg = findViewById(R.id.dayTwoImg);
        dayThreeImg = findViewById(R.id.dayThreeImg);
    }

    private void setListeners(){
        locationLeftButton.setOnClickListener(this);
        locationRightButton.setOnClickListener(this);
        dayOneBtn.setOnClickListener(this);
        dayTwoBtn.setOnClickListener(this);
        dayThreeBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
        startNetworkListener();
    }

    private void updateLocation(int change){
        selectedIndex = selectedIndex + change;
        if (selectedIndex == -1){
            selectedIndex = 5;
        } else if (selectedIndex == 6){
            selectedIndex = 0;
        }
        locationNameTxt = (TextView)findViewById(R.id.locationNameTxt);
        locationNameTxt.setText(locationNames[selectedIndex]);

        // Run network access on a separate thread;
        new Thread(new Task()).start();
    }

    private int getResID(String iconName){
        return getResources().getIdentifier(iconName , "drawable", getPackageName());
    }
    private void displayThreeDayWeather(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Display Max Temperatures
                maxTempTxtOne.setText(threeDayWeather.getFirstDay().getMaxTemp());
                maxTempTxtTwo.setText(threeDayWeather.getSecondDay().getMaxTemp());
                maxTempTxtThree.setText(threeDayWeather.getThirdDay().getMaxTemp());

                // Display Min Temperatures
                minTempTxtOne.setText(threeDayWeather.getFirstDay().getMinTemp());
                minTempTxtTwo.setText(threeDayWeather.getSecondDay().getMinTemp());
                minTempTxtThree.setText(threeDayWeather.getThirdDay().getMinTemp());

                // Display Dates
                dateOne.setText(threeDayWeather.getFirstDay().getFormattedDate());
                dateTwo.setText(threeDayWeather.getSecondDay().getFormattedDate());
                dateThree.setText(threeDayWeather.getThirdDay().getFormattedDate());

                // Display Images
                dayOneImg.setImageResource(getResID(threeDayWeather.getFirstDay().getForecastImage()));
                dayTwoImg.setImageResource(getResID(threeDayWeather.getSecondDay().getForecastImage()));
                dayThreeImg.setImageResource(getResID(threeDayWeather.getThirdDay().getForecastImage()));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 10;
        // Add a marker in Sydney and move the camera
        LatLng selectedLocation = new LatLng(threeDayWeather.getFirstDay().getLatitude(), threeDayWeather.getFirstDay().getLongitude());
        mMap.addMarker(new MarkerOptions().position(selectedLocation).title("Selected Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation,zoom));
    }

    private class Task implements Runnable {

        public Task() {
        }

        @Override
        public void run() {
            threeDayWeather = rssManager.createThreeDayWeatherClass(locationIDs[selectedIndex]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayThreeDayWeather();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(ThreeDayViewModel.this);
                }
            });
        }

    }

    private void displayNoInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ThreeDayViewModel.this);
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
                ThreeDayViewModel.this.finish(); //exits app
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