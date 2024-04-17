/*  Starter project for Mobile Platform Development in main diet 2023/2024
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Rhys Hood
// Student ID           S2332605
// Programme of Study   BSc (Hons) Computing
//

package org.me.gcu.hood_rhys_s2332605.viewModels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;

import org.me.gcu.hood_rhys_s2332605.R;
import org.me.gcu.hood_rhys_s2332605.models.RSSManager;
import org.me.gcu.hood_rhys_s2332605.models.ThreeDayWeather;
import org.me.gcu.hood_rhys_s2332605.models.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

public class ThreeDayViewModel extends AppCompatActivity implements OnClickListener
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

    // Initialise Variables
    private String[] locationNames = {"Glasgow","London","New York","Oman","Mauritius","Bangladesh"};
    private String[] locationIDs = {"2648579","2643743","5128581","287286","934154","1185241"};
    private int selectedIndex = 0;
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";

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

    public void onClick(View v)
    {
       if (v == locationLeftButton){
            updateLocation(-1);
       } else if (v == locationRightButton){
            updateLocation(1);
       } else if (v == dayOneBtn || v == dayTwoBtn || v == dayThreeBtn) {
           String id = getResources().getResourceName(v.getId());
           id = id.substring(id.lastIndexOf("/") + 1);

           Bundle bundle = new Bundle();
           bundle.putInt("selectedIndex",selectedIndex);
           switch(id){
               case "dayOneBtn":
                   bundle.putInt("selectedDay",1);
               case "dayTwoBtn":
                   bundle.putInt("selectedDay",2);
               case "dayThreeBtn":
                   bundle.putInt("selectedDay",3);
           }
           bundle.putInt("selectedDay",1);
           Intent startDetailedView = new Intent(ThreeDayViewModel.this, DetailedViewModel.class);
           startDetailedView.putExtras(bundle);
           startDetailedView.putExtra("threeDayWeather",threeDayWeather);
           startDetailedView.putExtra("dayOne",threeDayWeather.getFirstDay());
           startDetailedView.putExtra("dayTwo",threeDayWeather.getSecondDay());
           startDetailedView.putExtra("dayThree",threeDayWeather.getThirdDay());
           startActivity(startDetailedView);
       }
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
    }

    private void setListeners(){
        locationLeftButton.setOnClickListener(this);
        locationRightButton.setOnClickListener(this);
        dayOneBtn.setOnClickListener(this);
        dayTwoBtn.setOnClickListener(this);
        dayThreeBtn.setOnClickListener(this);
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
        new Thread(new Task("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationIDs[selectedIndex])).start();
    }

    private void displayThreeDayWeather(){
        maxTempTxtOne.setText(threeDayWeather.getFirstDay().getMaxTemp());
        maxTempTxtTwo.setText(threeDayWeather.getSecondDay().getMaxTemp());
        maxTempTxtThree.setText(threeDayWeather.getThirdDay().getMaxTemp());
        minTempTxtOne.setText(threeDayWeather.getFirstDay().getMinTemp());
        minTempTxtTwo.setText(threeDayWeather.getSecondDay().getMinTemp());
        minTempTxtThree.setText(threeDayWeather.getThirdDay().getMinTemp());
        dateOne.setText(threeDayWeather.getFirstDay().getFormattedDate());
        dateTwo.setText(threeDayWeather.getSecondDay().getFormattedDate());
        dateThree.setText(threeDayWeather.getThirdDay().getFormattedDate());
    }

    private class Task implements Runnable {
        private String url;

        public Task(String aurl) {
            url = aurl;
        }

        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";
            result = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);
                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result.indexOf(">");
            result = result.substring(i + 1);
            Log.e("MyTag - cleaned", result);
            threeDayWeather = rssManager.createThreeDayWeatherClass(result);
            displayThreeDayWeather();


            //
            // Now that you have the xml data you can parse it
            //


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            ThreeDayViewModel.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    //rawDataDisplay.setText(threeDayWeather.getFirstDay().getMaxTemp());
                }
            });
        }

    }
}