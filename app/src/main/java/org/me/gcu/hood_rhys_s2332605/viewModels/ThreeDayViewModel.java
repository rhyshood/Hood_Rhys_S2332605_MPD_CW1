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
import org.me.gcu.hood_rhys_s2332605.models.ThreeDayWeather;
import org.me.gcu.hood_rhys_s2332605.models.Weather;
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

public class ThreeDayViewModel extends AppCompatActivity implements OnClickListener
{
    // Initialise Text Views
    private TextView locationNameTxt;
    private TextView minTempTxtOne;
    private TextView maxTempTxtOne;
    private TextView maxTempTxtTwo;
    private TextView maxTempTxtThree;

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
        locationLeftButton = (Button)findViewById(R.id.locationLeftBtn);
        locationLeftButton.setOnClickListener(this);
        locationRightButton = (Button)findViewById(R.id.locationRightBtn);
        locationRightButton.setOnClickListener(this);
        dayOneBtn = (Button)findViewById(R.id.dayOneBtn);
        dayOneBtn.setOnClickListener(this);
        dayTwoBtn = (Button)findViewById(R.id.dayTwoBtn);
        dayTwoBtn.setOnClickListener(this);
        dayThreeBtn = (Button)findViewById(R.id.dayThreeBtn);
        dayThreeBtn.setOnClickListener(this);
        maxTempTxtOne = (TextView) findViewById(R.id.dayOneMaxTempTxt);
        maxTempTxtTwo = (TextView) findViewById(R.id.dayTwoMaxTempTxt);
        maxTempTxtThree = (TextView) findViewById(R.id.dayThreeMaxTempTxt);
        Toolbar myToolbar=(Toolbar)findViewById(R.id.toolbar);
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
           Bundle bundle = new Bundle();
           bundle.putInt("selectedIndex",selectedIndex);
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

    private boolean verifyDataExistence(int dataIndex, String forecast){
        if (dataIndex == 1){
            return forecast.contains("Maximum Temperature");
        } else if (dataIndex == 2){
            return forecast.contains("Minimum Temperature");
        } else if (dataIndex == 3){
            return forecast.contains("Wind Direction");
        } else if (dataIndex == 4){
            return forecast.contains("Wind Speed");
        } else if (dataIndex == 5){
            return forecast.contains("Visibility");
        } else if (dataIndex == 6){
            return forecast.contains("Pressure");
        } else if (dataIndex == 7){
            return forecast.contains("Humidity");
        } else if (dataIndex == 8){
            return forecast.contains("UV Risk");
        } else if (dataIndex == 9){
            return forecast.contains("Pollution");
        } else if (dataIndex == 10){
            return forecast.contains("Sunrise");
        } else if (dataIndex == 11){
            return forecast.contains("Sunset");
        } else {
            return false;
        }
    }
    private Weather splitWeatherData(Weather temp, String forecast){
        String[] arr = forecast.split(",");
        int index = 0;
        if (verifyDataExistence(1, forecast)){
            temp.setMaxTemp(arr[index]);
            index++;
        } else {
            temp.setMaxTemp("Maximum Temperature: Error");
        }

        if (verifyDataExistence(2, forecast)){
            temp.setMinTemp(arr[index]);
            index++;
        } else {
            temp.setMinTemp("Minimum Temperature: Error");
        }

        if (verifyDataExistence(3, forecast)){
            temp.setWindDirection(arr[index]);
            index++;
        } else {
            temp.setWindDirection("WindDirection: Error");
        }

        if (verifyDataExistence(4, forecast)){
            temp.setWindSpeed(arr[index]);
            index++;
        } else {
            temp.setWindSpeed("Wind Speed: Error");
        }

        if (verifyDataExistence(5, forecast)){
            temp.setVisibility(arr[index]);
            index++;
        } else {
            temp.setVisibility("Visibility: Error");
        }

        if (verifyDataExistence(6, forecast)){
            temp.setAirPressure(arr[index]);
            index++;
        } else {
            temp.setAirPressure("Pressure: Error");
        }

        if (verifyDataExistence(7, forecast)){
            temp.setHumidity(arr[index]);
            index++;
        } else {
            temp.setHumidity("Humidity: Error");
        }

        if (verifyDataExistence(8, forecast)){
            temp.setUVRisk(arr[index]);
            index++;
        } else {
            temp.setUVRisk("UV Risk: Error");
        }

        if (verifyDataExistence(9, forecast)){
            temp.setPollution(arr[index]);
            index++;
        } else {
            temp.setPollution("Pollution: Error");
        }

        if (verifyDataExistence(10, forecast)){
            temp.setSunrise(arr[index]);
            index++;
        } else {
            temp.setSunrise("Sunrise: Error");
        }

        if (verifyDataExistence(11, forecast)){
            temp.setSunset(arr[index]);
        } else {
            temp.setSunset("Sunset: Error");
        }
        return temp;
    }

    private void displayThreeDayWeather(){
        maxTempTxtOne.setText(threeDayWeather.getFirstDay().getMaxTemp());
        maxTempTxtTwo.setText(threeDayWeather.getSecondDay().getMaxTemp());
        maxTempTxtThree.setText(threeDayWeather.getThirdDay().getMaxTemp());
    }
    private void createThreeDayWeatherClass(String dataInput) {
        Log.d("Data Parsing", "Creating Three Day Weather Class");
        int dayNumber = 0;
        Weather temp = new Weather();
        threeDayWeather = new ThreeDayWeather();
        boolean readingItem = false;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataInput.replace("&","&amp;")));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("Item")) {
                        Log.d("Data Parsing", "Item found");
                        readingItem = true;
                        dayNumber += 1;
                        temp = new Weather();
                        Date dt = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(dt);
                        c.add(Calendar.DATE, dayNumber - 1);
                        temp.setDate(c.getTime());
                    } else if (xpp.getName().equalsIgnoreCase("Title")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Title");
                            temp.setForecast(xpp.nextText().split(",")[0]);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("Description")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Details");
                            splitWeatherData(temp, xpp.nextText());
                            if (dayNumber == 1) {
                                threeDayWeather.setFirstDay(temp);
                            } else if (dayNumber == 2) {
                                threeDayWeather.setSecondDay(temp);
                            } else if (dayNumber == 3) {
                                threeDayWeather.setThirdDay(temp);
                            }
                        }
                    } else if (xpp.getName().equalsIgnoreCase("Point")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Location Coordinates");
                            threeDayWeather.setCoordinates(xpp.nextText());
                        }
                    }

                } else if(eventType == XmlPullParser.END_TAG){
                    if (xpp.getName().equalsIgnoreCase("Item")) {
                        Log.d("Data Parsing", "Finished Item");
                        readingItem = false;
                    }
                }
                eventType = xpp.next();
            }
            displayThreeDayWeather();
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }
        // Need separate thread to access the internet resource over network
        // Other neater solutions should be adopted in later iterations.
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
            createThreeDayWeatherClass(result);


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