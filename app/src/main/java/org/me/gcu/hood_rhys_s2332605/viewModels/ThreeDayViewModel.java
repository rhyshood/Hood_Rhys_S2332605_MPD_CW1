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
    private TextView locationNameTxt;
    private Button locationLeftButton;
    private Button locationRightButton;
    private String result;
    private Button dayOneBtn;
    private Button dayTwoBtn;
    private Button dayThreeBtn;
    private Button returnBtn;
    private ViewFlipper flip;
    private String[] locationNames = {"Glasgow","London","New York","Oman","Mauritius","Bangladesh"};
    private String[] locationIDs = {"2648579","2643743","5128581","287286","934154","1185241"};
    private int selectedIndex = 0;
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/2648579";
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
           Intent startActivity2 = new Intent(ThreeDayViewModel.this, DetailedViewModel.class);
           startActivity2.putExtras(bundle);
           startActivity(startActivity2);
       }
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
        //new Thread(new Task("https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationIDs[selectedIndex])).start();
    }

    private Weather splitWeatherData(Weather temp, String forecast){
        String[] arr = forecast.split(",");
        temp.setMaxTemp(arr[0]);
        //temp.setMinTemp(arr[1]);
        temp.setWindDirection(arr[1]);
        temp.setWindSpeed(arr[2]);
        temp.setVisibility(arr[3]);
        temp.setAirPressure(arr[4]);
        temp.setHumidity(arr[5]);
        temp.setUVRisk(arr[6]);
        temp.setPollution(arr[7]);
        //temp.setSunrise(arr[9]);
        temp.setSunset(arr[8]);
        return temp;
    }

    private void createThreeDayWeatherClass(String dataInput) {
        Log.d("Data Parsing", "Creating Three Day Weather Class");
        int dayNumber = 0;
        Weather temp = new Weather();
        threeDayWeather = new ThreeDayWeather();
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
                        dayNumber += 1;
                        temp = new Weather();
                        Date dt = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(dt);
                        c.add(Calendar.DATE, dayNumber - 1);
                        temp.setDate(c.getTime());
                    } else if (xpp.getName().equalsIgnoreCase("Title")) {
                        Log.d("Data Parsing", "Found Weather Title");
                        temp.setForecast(xpp.nextText().split(",")[0]);
                    } else if (xpp.getName().equalsIgnoreCase("Description")) {
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

                    }
                eventType = xpp.next();
            }
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


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                int count = 1;
                while ((inputLine = in.readLine()) != null) {
                    if (count >= 18){
                    result = result + inputLine;}
                    Log.e("MyTag", inputLine);
                    count++;
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