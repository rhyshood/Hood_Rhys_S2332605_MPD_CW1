package org.me.gcu.hood_rhys_s2332605.models;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RSSManager {

    private void fetchForecastImage(Weather temp, String forecast){
        forecast = forecast.split(": ")[1];
        if (forecast.equalsIgnoreCase("Light Cloud")){
            temp.setForecastImage("day_partial_cloud");
        } else if (forecast.contains("Cloudy")){
            temp.setForecastImage("cloudy");
        } else if (forecast.contains("Rain")){
            temp.setForecastImage("day_rain");
        } else if (forecast.contains("Snow")){
            temp.setForecastImage("day_snow");
        } else {
            temp.setForecastImage("cloudy");
        }
    }

    // NEXT TWO METHODS INCLUDE ERROR PREVENTION DUE TO HOW INCONSISTENT THE BBC RSS FEED IS
    // WHERE THERE IS NO GUARANTEE ALL DATA WILL BE TRANSMITTED

    // VERIFIES DATA HAS BEEN RECEIVED
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
        } else if (dataIndex == 12){
            return forecast.contains("Temperature");
        } else {
            return false;
        }
    }
    private void splitWeatherData(Weather temp, String forecast, boolean isLatest){
        String[] arr = forecast.split(",");
        int index = 0;
        if (isLatest){
            if (verifyDataExistence(12, forecast)){
                temp.setTemp(arr[index]);
                index++;
            } else {
                temp.setTemp("Temperature: Error");
            }

            if (verifyDataExistence(3, forecast)){
                temp.setWindDirection(arr[index]);
                index++;
            } else {
                temp.setWindDirection("Wind Direction: Error");
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
                temp.setAirPressure(arr[index] + "," + arr[index + 1]);
                index = index + 2;
            } else {
                temp.setAirPressure("Pressure: Error");
            }

            if (verifyDataExistence(7, forecast)){
                temp.setHumidity(arr[index]);
            } else {
                temp.setHumidity("Humidity: Error");
            }
        }else{
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
                temp.setWindDirection("Wind Direction: Error");
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
        }
    }

    public ThreeDayWeather createThreeDayWeatherClass(String locationID) {
        String dataInput = getDataString(false, locationID);
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
                    } else if (xpp.getName().equalsIgnoreCase("Title")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Title");
                            String forecast = xpp.nextText().split(",")[0];
                            temp.setForecast(forecast);
                            fetchForecastImage(temp, forecast);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("Description")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Details");
                            splitWeatherData(temp, xpp.nextText(), false);
                            }

                    } else if (xpp.getName().equalsIgnoreCase("Date")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Date");

                            Date dt = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").parse(xpp.nextText());
                            Calendar c = Calendar.getInstance();
                            c.setTime(dt);
                            c.add(Calendar.DATE, dayNumber - 1);
                            temp.setDate(c.getTime());
                        }

                    } else if (xpp.getName().equalsIgnoreCase("Point")) {
                        if (readingItem) {
                            Log.d("Data Parsing", "Found Location Coordinates");
                            String[] coordinates = xpp.nextText().split(" ");
                            temp.setLatitude(Double.parseDouble(coordinates[0]));
                            temp.setLongitude(Double.parseDouble(coordinates[1]));

                            if (dayNumber == 1) {
                                threeDayWeather.setFirstDay(temp);
                            } else if (dayNumber == 2) {
                                threeDayWeather.setSecondDay(temp);
                            } else if (dayNumber == 3) {
                                threeDayWeather.setThirdDay(temp);
                            }
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
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return threeDayWeather;
    }
    // CREATES WEATHER CLASS USING LATEST OBSERVATION DATA
    public Weather createWeatherClass(String locationID) {
        String dataInput = getDataString(true, locationID);
        Log.d("Data Parsing", "Creating Single Weather Class");
        Weather temp = new Weather("0");
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
                        temp.setDate(new Date());
                    } else if (xpp.getName().equalsIgnoreCase("Title")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Title");
                            String forecast = xpp.nextText().split(",")[0];
                            temp.setForecast(forecast);
                            fetchForecastImage(temp, forecast);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("Description")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Weather Details");
                            splitWeatherData(temp, xpp.nextText(), true);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("Point")) {
                        if(readingItem) {
                            Log.d("Data Parsing", "Found Location Coordinates");
                            String[] coordinates = xpp.nextText().split(" ");
                            temp.setLatitude(Double.parseDouble(coordinates[0]));
                            temp.setLongitude(Double.parseDouble(coordinates[1]));
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
        return temp;
    }

    public String getDataString(boolean isLatest, String locationID){

        String url = "";
        String result = "";
        if (isLatest){
            url = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/" + locationID;
        } else {
            url = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + locationID;
        }

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
        return result;
    }

}