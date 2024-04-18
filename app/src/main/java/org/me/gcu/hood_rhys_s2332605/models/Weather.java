package org.me.gcu.hood_rhys_s2332605.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather implements Parcelable {
    // Attributes
    private String minTemp;
    private String maxTemp;
    private String temp;
    private String forecast;
    private String forecastImage;
    private String windDirection;
    private String windSpeed;
    private String visibility;
    private String airPressure;
    private String humidity;
    private String uvRisk;
    private String pollution;
    private String sunrise;
    private String sunset;
    private Date date;
    private String coordinates;

    protected Weather(Parcel in) {
        minTemp = in.readString();
        maxTemp = in.readString();
        temp = in.readString();
        forecast = in.readString();
        forecastImage = in.readString();
        windDirection = in.readString();
        windSpeed = in.readString();
        visibility = in.readString();
        airPressure = in.readString();
        humidity = in.readString();
        uvRisk = in.readString();
        pollution = in.readString();
        sunrise = in.readString();
        sunset = in.readString();
        date = (java.util.Date) in.readSerializable();
        coordinates = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    // Getters
    public String getMinTemp(){
        return minTemp;
    }
    public String getMaxTemp(){
        return maxTemp;
    }
    public String getTemp(){
        return temp;
    }
    public String getForecast(){
        return forecast;
    }

    public String getForecastImage(){
        return forecastImage;
    }
    public String getWindDirection(){
        return windDirection;
    }
    public String getWindSpeed(){
        return windSpeed;
    }
    public String getVisibility(){
        return visibility;
    }
    public String getAirPressure(){
        return airPressure;
    }
    public String getHumidity(){
        return humidity;
    }
    public String getUVRisk(){
        return uvRisk;
    }
    public String getPollution(){
        return pollution;
    }
    public String getSunset() { return sunset; }
    public String getSunrise() { return sunrise; }
    public Date getDate() { return date; }
    public String getCoordinates(){ return coordinates; }

    // Setters
    public void setMinTemp(String tempIn){
        minTemp = tempIn;
    }
    public void setMaxTemp(String tempIn){
        maxTemp = tempIn;
    }
    public void setTemp(String tempIn){
        temp = tempIn;
    }
    public void setForecast(String forecastIn){
        forecast = forecastIn;
    }
    public void setForecastImage(String forecastImageIn){
        forecastImage = forecastImageIn;
    }
    public void setWindDirection(String windDirectionIn){
        windDirection = windDirectionIn;
    }
    public void setWindSpeed(String windSpeedIn){
        windSpeed = windSpeedIn;
    }
    public void setVisibility(String visibilityIn){
        visibility = visibilityIn;
    }
    public void setAirPressure(String airPressureIn){
        airPressure = airPressureIn;
    }
    public void setHumidity(String humidityIn){
        humidity = humidityIn;
    }
    public void setUVRisk(String uvRiskIn){
        uvRisk = uvRiskIn;
    }
    public void setPollution(String pollutionIn){
        pollution = pollutionIn;
    }
    public void setSunrise(String sunriseIn){
        sunrise = sunriseIn;
    }
    public void setSunset(String sunsetIn){
        sunset = sunsetIn;
    }
    public void setDate(Date dateIn) { date = dateIn; }
    public void setCoordinates(String coordinatesIn){ coordinates = coordinatesIn;}

    // Constructor

    // Zero Parameter Constructor
    public Weather(){
        minTemp = "";
        maxTemp = "";
        temp = "";
        forecast = "";
        forecastImage = "cloudy";
        windDirection = "";
        windSpeed = "";
        visibility = "";
        airPressure = "";
        humidity = "";
        uvRisk = "";
        pollution = "";
        sunset = "";
        sunrise = "";
        date = new Date();
        coordinates = "";
    }

    // Constructor for Latest Observations
    public Weather(String tempIn){
        temp = "";
        forecast = "";
        forecastImage = "cloudy";
        windDirection = "";
        windSpeed = "";
        visibility = "";
        airPressure = "";
        humidity = "";
        date = new Date();
        coordinates = "";
    }
    // 13 Parameter Constructor
    Weather(String minTempIn, String maxTempIn, String tempIn, String forecastIn, String forecastImageIn, String windDirectionIn, String windSpeedIn, String visibilityIn, String airPressureIn, String humidityIn, String uvRiskIn, String pollutionIn, String sunsetIn, String sunriseIn, Date dateIn, String coordinatesIn){
        minTemp = minTempIn;
        maxTemp = maxTempIn;
        temp = tempIn;
        forecast = forecastIn;
        forecastImage = forecastImageIn;
        windDirection = windDirectionIn;
        windSpeed = windSpeedIn;
        visibility = visibilityIn;
        airPressure = airPressureIn;
        humidity = humidityIn;
        uvRisk = uvRiskIn;
        pollution = pollutionIn;
        sunset = sunsetIn;
        sunrise = sunriseIn;
        date = dateIn;
        coordinates = coordinatesIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(minTemp);
        dest.writeString(maxTemp);
        dest.writeString(temp);
        dest.writeString(forecast);
        dest.writeString(forecastImage);
        dest.writeString(windDirection);
        dest.writeString(windSpeed);
        dest.writeString(visibility);
        dest.writeString(airPressure);
        dest.writeString(humidity);
        dest.writeString(uvRisk);
        dest.writeString(pollution);
        dest.writeString(sunrise);
        dest.writeString(sunset);
        dest.writeSerializable(date);
        dest.writeString(coordinates);
    }

    public String getFormattedDate(){
        return new SimpleDateFormat("E dd MMM").format(date);
    }
}
