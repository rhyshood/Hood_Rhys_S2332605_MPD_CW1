package org.me.gcu.hood_rhys_s2332605.viewModels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.RSSManager;
import org.me.gcu.hood_rhys_s2332605.models.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LatestObservationViewModel extends AppCompatActivity implements View.OnClickListener {
    // Text Views
    private TextView maxTempTxt;
    private TextView minTempTxt;
    private TextView windDirectionTxt;
    private TextView windSpeedTxt;
    private TextView visibilityTxt;
    private TextView pressureTxt;
    private TextView humidityTxt;
    private TextView uvTxt;
    private TextView pollutionTxt;
    private TextView sunriseTxt;
    private TextView sunsetTxt;
    private TextView dateTxt;
    private TextView locationTxt;

    // Buttons
    private Button returnBtn;
    private Button locationRightBtn;
    private Button locationLeftBtn;

    // Misc
    private ImageView forecastImg;
    private Weather currentWeather;
    private RSSManager rssManager = new RSSManager();
    private String[] locationNames = {"Glasgow","London","New York","Oman","Mauritius","Bangladesh"};
    private String[] locationIDs = {"2648579","2643743","5128581","287286","934154","1185241"};
    private int selectedIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_observation_view);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        assignElements();
        assignListeners();
        updateLocation(0);
    }

    @Override
    public void onClick(View v) {
        if (v == locationLeftBtn) {
            updateLocation(-1);
        } else if (v == locationRightBtn) {
            updateLocation(1);
        } else if (v == returnBtn){
            Intent mainMenu = new Intent(LatestObservationViewModel.this, MainMenuViewModel.class);
            startActivity(mainMenu);
        }
    }


    private void assignElements(){
        // Text Views
        maxTempTxt = findViewById(R.id.maxTempTxt);
        minTempTxt = findViewById(R.id.minTempTxt);
        windDirectionTxt = findViewById(R.id.windDirectionTxt);
        windSpeedTxt = findViewById(R.id.windSpeedTxt);
        visibilityTxt = findViewById(R.id.visibilityTxt);
        pressureTxt = findViewById(R.id.pressureTxt);
        humidityTxt = findViewById(R.id.humidityTxt);
        uvTxt = findViewById(R.id.uvTxt);
        pollutionTxt = findViewById(R.id.pollutionTxt);
        sunriseTxt = findViewById(R.id.sunriseTxt);
        sunsetTxt = findViewById(R.id.sunsetTxt);
        dateTxt = findViewById(R.id.dateTxt);

        // Buttons
        locationLeftBtn = findViewById(R.id.locationLeftBtn);
        locationRightBtn = findViewById(R.id.locationRightBtn);
        returnBtn = findViewById(R.id.returnBtn);

        // Misc
        forecastImg = findViewById(R.id.forecastImg);
    }
        private void updateLocation(int change){
            selectedIndex = selectedIndex + change;
            if (selectedIndex == -1){
                selectedIndex = 5;
            } else if (selectedIndex == 6){
                selectedIndex = 0;
            }
            locationTxt = (TextView)findViewById(R.id.locationNameTxt);
            locationTxt.setText(locationNames[selectedIndex]);

            // Run network access on a separate thread;
            new Thread(new Task()).start();
        }
    private int getResID(String iconName){
        return getResources().getIdentifier(iconName , "drawable", getPackageName());
    }

    private void displayData(){
        maxTempTxt.setText(currentWeather.getMaxTemp());
        minTempTxt.setText(currentWeather.getMinTemp());
        windDirectionTxt.setText(currentWeather.getWindDirection());
        windSpeedTxt.setText(currentWeather.getWindSpeed());
        visibilityTxt.setText(currentWeather.getVisibility());
        pressureTxt.setText(currentWeather.getAirPressure());
        humidityTxt.setText(currentWeather.getHumidity());
        uvTxt.setText(currentWeather.getUVRisk());
        pollutionTxt.setText(currentWeather.getPollution());
        sunriseTxt.setText(currentWeather.getSunrise());
        sunsetTxt.setText(currentWeather.getSunset());
        dateTxt.setText(currentWeather.getFormattedDate());
        forecastImg.setImageResource(getResID(currentWeather.getForecastImage()));

    }

    public void assignListeners (){
        returnBtn.setOnClickListener(this);
        locationRightBtn.setOnClickListener(this);
        locationLeftBtn.setOnClickListener(this);
    }
        class Task implements Runnable {
        public Task() {}

        @Override
        public void run() {
            currentWeather = rssManager.createWeatherClass(locationIDs[selectedIndex]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayData();
                }
            });
        }

    }
}