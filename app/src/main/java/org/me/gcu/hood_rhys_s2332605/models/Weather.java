package org.me.gcu.hood_rhys_s2332605.models;

import java.util.Date;

public class Weather {
    // Attributes
    private String minTemp;
    private String maxTemp;
    private String forecast;
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

    // Getters
    public String getMinTemp(){
        return minTemp;
    }
    public String getMaxTemp(){
        return maxTemp;
    }
    public String getForecast(){
        return forecast;
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
    public String humidity(){
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

    // Setters
    public void setMinTemp(String tempIn){
        minTemp = tempIn;
    }
    public void setMaxTemp(String tempIn){
        maxTemp = tempIn;
    }
    public void setForecast(String forecastIn){
        forecast = forecastIn;
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

    // Constructor

    // Zero Parameter Constructor
    public Weather(){
        minTemp = "";
        maxTemp = "";
        forecast = "";
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
    }

    // 13 Parameter Constructor
    Weather(String minTempIn, String maxTempIn, String forecastIn, String windDirectionIn, String windSpeedIn, String visibilityIn, String airPressureIn, String humidityIn, String uvRiskIn, String pollutionIn, String sunsetIn, String sunriseIn, Date dateIn){
        minTemp = minTempIn;
        maxTemp = maxTempIn;
        forecast = forecastIn;
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
    }
}
