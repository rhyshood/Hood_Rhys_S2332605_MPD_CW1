package org.me.gcu.hood_rhys_s2332605.models;

public class ThreeDayWeather {

    // Attributes
    private Weather firstDay;
    private Weather secondDay;
    private Weather thirdDay;

    // Getters
    public Weather getFirstDay(){
        return firstDay;
    }
    public Weather getSecondDay(){
        return secondDay;
    }
    public Weather getThirdDay(){
        return thirdDay;
    }

    // Setters
    public void setFirstDay(Weather firstDayIn){
        firstDay = firstDayIn;
    }

    public void setSecondDay(Weather secondDayIn){
        secondDay = secondDayIn;
    }

    public void setThirdDay(Weather thirdDayIn){
        thirdDay = thirdDayIn;
    }

    // Constructors
    // Zero Parameter Constructor
    public ThreeDayWeather(){
        firstDay = new Weather();
        secondDay = new Weather();
        thirdDay = new Weather();
    }

    // Three Parameter Constructor
    ThreeDayWeather(Weather firstDayIn, Weather secondDayIn, Weather thirdDayIn){
        firstDay = firstDayIn;
        secondDay = secondDayIn;
        thirdDay = thirdDayIn;
    }
}
