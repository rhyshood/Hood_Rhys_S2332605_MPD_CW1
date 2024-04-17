package org.me.gcu.hood_rhys_s2332605.models;

import android.util.Log;

import org.me.gcu.hood_rhys_s2332605.viewModels.ThreeDayViewModel;
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

public class RSSManager {
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

    public ThreeDayWeather createThreeDayWeatherClass(String dataInput) {
        Log.d("Data Parsing", "Creating Three Day Weather Class");
        int dayNumber = 0;
        Weather temp = new Weather();
        ThreeDayWeather threeDayWeather= new ThreeDayWeather();
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
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException(e);
        }
        return threeDayWeather;
    }


}
