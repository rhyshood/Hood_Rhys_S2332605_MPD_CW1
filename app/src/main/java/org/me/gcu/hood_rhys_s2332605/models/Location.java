package org.me.gcu.hood_rhys_s2332605.models;

public class Location {
    // Attributes
    private String name;
    private String coordinates;
    private ThreeDayWeather threeDayWeather;

    // Getters
    private String getName (){
        return name;
    }
    private String getCoordinates (){
        return coordinates;
    }
    private ThreeDayWeather getThreeDayForecast(){
        return threeDayWeather;
    }

    // Setters
    private void setName(String nameIn){
        name = nameIn;
    }
    private void setCoordinates(String coordinatesIn){
        coordinates = coordinatesIn;
    }

    private void setThreeDayWeather (ThreeDayWeather threeDayWeatherIn){
        threeDayWeather = threeDayWeatherIn;
    }

    // Constructors
    // Zero Parameter Constructor
    Location(){
        name = "";
        coordinates = "";
        threeDayWeather = new ThreeDayWeather();
    }

    // Three Parameter Constructor
    Location(String nameIn, String coordinatesIn, ThreeDayWeather threeDayWeatherIn){
        name = nameIn;
        coordinates = coordinatesIn;
        threeDayWeather = threeDayWeatherIn;
    }
}
