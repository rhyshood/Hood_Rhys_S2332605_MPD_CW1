///////////////////////////////////////////////
// Name                 Rhys Hood            //
// Student ID           S2332605             //
// Programme of Study   BSc (Hons) Computing //
///////////////////////////////////////////////
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

    // SETS FORECAST IMAGE FOR WEATHER CLASS
    private void fetchForecastImage(Weather temp, String forecast){
        // Forecast variable relates to title tag from RSS Feed (Example Data: Today: Light Cloud, Minimum Temperature: 4°C (39°F) Maximum Temperature: 8°C (46°F))
        // This sets forecast to first part of this input ("Today: Light Cloud")
        forecast = forecast.split(": ")[1];
        // Checks if forecast contains Light Cloud
        if (forecast.equalsIgnoreCase("Light Cloud")){
            // If so, set forecast image to partial cloud image
            temp.setForecastImage("day_partial_cloud");
        // Checks if forecast contains Cloudy
        } else if (forecast.contains("Cloudy")){
            // If so, set forecast image to cloudy
            temp.setForecastImage("cloudy");
        // Checks if forecast contains Rain
        } else if (forecast.contains("Rain")){
            // If so, set forecast image to rain
            temp.setForecastImage("day_rain");
        // Checks if forecast contains Snow
        } else if (forecast.contains("Snow")){
            // If so, set forecast image to snow
            temp.setForecastImage("snow");
        // If forecast is missing data
        } else {
            // Set forecast image to default cloudy
            temp.setForecastImage("cloudy");
        }
    }

    // NEXT TWO METHODS INCLUDE ERROR HANDLING DUE TO HOW INCONSISTENT THE BBC RSS FEED IS
    // WHERE THERE IS NO GUARANTEE ALL DATA WILL BE TRANSMITTED. THIS WILL DETECT WHAT
    // DATA IS MISSING AND HANDLE THEM APPROPRIATELY.

    // VERIFIES DATA HAS BEEN RECEIVED
    private boolean verifyDataExistence(int dataIndex, String forecast){
        // All forecast data across both 3 Day and Latest is assigned a number from 1 to 12
        // The developer can use this method with that number and the forecast string from the description tag
        // To verify that the data has actually been received

        // Maximum Temperature = 1
        if (dataIndex == 1){
            // Returns true if Maximum Temperature has been transmitted
            return forecast.contains("Maximum Temperature");
        // Minimum Temperature = 2
        } else if (dataIndex == 2){
            // Returns true if Minimum Temperature has been transmitted
            return forecast.contains("Minimum Temperature");
        // Wind Direction = 3
        } else if (dataIndex == 3){
            // Returns true if Wind Direction has been transmitted
            return forecast.contains("Wind Direction");
        // Wind Speed = 4
        } else if (dataIndex == 4){
            // Returns true if Wind Speed has been transmitted
            return forecast.contains("Wind Speed");
        // Visibility = 5
        } else if (dataIndex == 5){
            // Returns true if Visibility has been transmitted
            return forecast.contains("Visibility");
        // Pressure = 6
        } else if (dataIndex == 6){
            // Returns true if Pressure has been transmitted
            return forecast.contains("Pressure");
        // Humidity = 7
        } else if (dataIndex == 7){
            // Returns true if Humidity has been transmitted
            return forecast.contains("Humidity");
        // UV Risk = 8
        } else if (dataIndex == 8){
            // Returns true if UV Risk has been transmitted
            return forecast.contains("UV Risk");
        // Pollution = 9
        } else if (dataIndex == 9){
            // Returns true if Pollution has been transmitted
            return forecast.contains("Pollution");
        // Sunrise = 10
        } else if (dataIndex == 10){
            // Returns true if Sunrise has been transmitted
            return forecast.contains("Sunrise");
        // Sunset = 11
        } else if (dataIndex == 11){
            // Returns true if Sunset has been transmitted
            return forecast.contains("Sunset");
        // Temperature = 12
        } else if (dataIndex == 12){
            // Returns true if Temperature has been transmitted
            return forecast.contains("Temperature");
        // Anything else returns false
        } else {
            return false;
        }
    }

    // SPLITS WEATHER DATA FROM FULL DESCRIPTION STRING
    private void splitWeatherData(Weather temp, String forecast, boolean isLatest){
        // Splits full direction string into an array using a comma delimiter (Example Data: Maximum Temperature: 8°C (46°F), Minimum Temperature: 4°C (39°F), Wind Direction: North Easterly, Wind Speed: 9mph, Visibility: Good, Pressure: 1040mb, Humidity: 77%, UV Risk: 1, Pollution: Low, Sunrise: 08:21 GMT, Sunset: 16:14 GMT)
        String[] arr = forecast.split(",");
        // Sets index to 0
        int index = 0;
        // If Data is from Latest Observation
        if (isLatest){
            // Checks if Temperature has been transmitted
            if (verifyDataExistence(12, forecast)){
                // If so, sets the temp class's temperature and increases index
                temp.setTemp(arr[index]);
                index++;
            } else {
                // If not, sets Temperature as error.
                temp.setTemp("Temperature: Error");
            }
            // Checks if Wind Direction has been transmitted
            if (verifyDataExistence(3, forecast)){
                // If so, sets the temp class's Wind Direction and increases index
                temp.setWindDirection(arr[index]);
                index++;
            } else {
                // If not, sets Wind Direction as error.
                temp.setWindDirection("Wind Direction: Error");
            }
            // Checks if Wind Speed has been transmitted
            if (verifyDataExistence(4, forecast)){
                // If so, sets the Wind Speed and increases index
                temp.setWindSpeed(arr[index]);
                index++;
            } else {
                // If not, sets Wind Speed as error.
                temp.setWindSpeed("Wind Speed: Error");
            }
            // Checks if Visibility has been transmitted
            if (verifyDataExistence(5, forecast)){
                // If so, sets the Visibility and increases index
                temp.setVisibility(arr[index]);
                index++;
            } else {
                // If not, sets Visibility as error.
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