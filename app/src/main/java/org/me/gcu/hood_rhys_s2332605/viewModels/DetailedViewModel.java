package org.me.gcu.hood_rhys_s2332605.viewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.ThreeDayWeather;
import org.me.gcu.hood_rhys_s2332605.models.Weather;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

public class DetailedViewModel extends AppCompatActivity implements OnClickListener, OnMapReadyCallback {
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
    // Buttons
    private Button returnBtn;
    private Button dateRightBtn;
    private Button dateLeftBtn;

    // Misc
    private ImageView forecastImg;
    private String[] locationNames = {"Glasgow", "London", "New York", "Oman", "Mauritius", "Bangladesh"};
    private String[] locationIDs = {"2648579", "2643743", "5128581", "287286", "934154", "1185241"};
    private int selectedIndex = 0;
    private int selectedDay;
    private ThreeDayWeather threeDayWeather;
    private Weather currentWeather;
    private GoogleMap mMap;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            selectedIndex = 0;
            currentWeather = new Weather();
        }else {
            selectedIndex = bundle.getInt("selectedIndex");
            selectedDay = getIntent().getExtras().getInt("selectedDay");
            threeDayWeather = getIntent().getExtras().getParcelable("threeDayWeather");
            threeDayWeather.setFirstDay(getIntent().getExtras().getParcelable("dayOne"));
            threeDayWeather.setSecondDay(getIntent().getExtras().getParcelable("dayTwo"));
            threeDayWeather.setThirdDay(getIntent().getExtras().getParcelable("dayThree"));
        }
        TextView locationName = findViewById(R.id.locationNameTxt);
        locationName.setText(locationNames[selectedIndex]);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        assignElements();
        assignListeners();
        updateCurrentWeather(selectedDay);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void changeDay(int change){
        selectedDay = selectedDay + change;

        if (selectedDay == 0){
            selectedDay = 3;
        } else if (selectedDay == 4){
            selectedDay = 1;
        }
        updateCurrentWeather(selectedDay);
    }

    private void updateCurrentWeather(int currentDay){
        if (currentDay == 1){
            currentWeather = threeDayWeather.getFirstDay();
            displayData();
        } else if (currentDay == 2){
            currentWeather = threeDayWeather.getSecondDay();
            displayData();
        } else {
            currentWeather = threeDayWeather.getThirdDay();
            displayData();
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
        dateLeftBtn = findViewById(R.id.dateLeftBtn);
        dateRightBtn = findViewById(R.id.dateRightBtn);
        returnBtn = findViewById(R.id.returnBtn);

        // Misc
        forecastImg = findViewById(R.id.forecastImg);
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailedViewModel.this);
    }

    public void assignListeners (){
        returnBtn.setOnClickListener(this);
        dateRightBtn.setOnClickListener(this);
        dateLeftBtn.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        if (v == returnBtn) {
            Bundle bundle = new Bundle();
            bundle.putInt("selectedIndex",selectedIndex);
            Intent startThreeDayView = new Intent(DetailedViewModel.this, ThreeDayViewModel.class);
            startThreeDayView.putExtras(bundle);
            startActivity(startThreeDayView);
        } else if (v == dateRightBtn){
            changeDay(1);
        } else if (v == dateLeftBtn){
            changeDay(-1);
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 10;
        // Add a marker in Sydney and move the camera
        LatLng selectedLocation = new LatLng(currentWeather.getLatitude(), currentWeather.getLongitude());
        mMap.addMarker(new MarkerOptions().position(selectedLocation).title("Selected Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation,zoom));
    }
}