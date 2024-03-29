package org.me.gcu.hood_rhys_s2332605;

public class Weather {
    // Attributes
    private int minTemp;
    private int maxTemp;
    private String forecast;
    private String windDirection;
    private int windSpeed;
    private String visibility;
    private int airPressure;
    private int humidity;
    private int uvRisk;
    private String pollution;

    // Getters
    public int getMinTemp(){
        return minTemp;
    }
    public int getMaxTemp(){
        return maxTemp;
    }
    public String getForecast(){
        return forecast;
    }
    public String getWindDirection(){
        return windDirection;
    }
    public int getWindSpeed(){
        return windSpeed;
    }
    public String getVisibility(){
        return visibility;
    }
    public int getAirPressure(){
        return airPressure;
    }
    public int humidity(){
        return humidity;
    }
    public int getUVRisk(){
        return uvRisk;
    }
    public String getPollution(){
        return pollution;
    }

    // Setters
    public void setMinTemp(int tempIn){
        minTemp = tempIn;
    }
    public void setMaxTemp(int tempIn){
        maxTemp = tempIn;
    }
    public void getForecast(String forecastIn){
        forecast = forecastIn;
    }
    public void getWindDirection(String windDirectionIn){
        windDirection = windDirectionIn;
    }
    public void getWindSpeed(int windSpeedIn){
        windSpeed = windSpeedIn;
    }
    public void getVisibility(String visibilityIn){
        visibility = visibilityIn;
    }
    public void getAirPressure(int airPressureIn){
        airPressure = airPressureIn;
    }
    public void humidity(int humidityIn){
        humidity = humidityIn;
    }
    public void getUVRisk(int uvRiskIn){
        uvRisk = uvRiskIn;
    }
    public void getPollution(String pollutionIn){
        pollution = pollutionIn;
    }

    // Constructor
    Weather(){
        minTemp = 0;
        maxTemp = 0;
        forecast = "";
        windDirection = "";
        windSpeed = 0;
        visibility = "";
        airPressure = 0;
        humidity = 0;
        uvRisk = 0;
        pollution = "";
    }

    Weather(int minTempIn, int maxTempIn, String forecastIn, String windDirectionIn, int windSpeedIn, String visibilityIn, int airPressureIn, int humidityIn, int uvRiskIn, String pollutionIn){
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
    }
}
