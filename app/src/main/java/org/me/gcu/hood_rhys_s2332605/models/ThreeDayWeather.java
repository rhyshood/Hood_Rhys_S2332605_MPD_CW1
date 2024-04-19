///////////////////////////////////////////////
// Name                 Rhys Hood            //
// Student ID           S2332605             //
// Programme of Study   BSc (Hons) Computing //
///////////////////////////////////////////////
package org.me.gcu.hood_rhys_s2332605.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ThreeDayWeather implements Parcelable {

    // Attributes
    private Weather firstDay;
    private Weather secondDay;
    private Weather thirdDay;

    protected ThreeDayWeather(Parcel in) {
    }

    public static final Creator<ThreeDayWeather> CREATOR = new Creator<ThreeDayWeather>() {
        @Override
        public ThreeDayWeather createFromParcel(Parcel in) {
            return new ThreeDayWeather(in);
        }

        @Override
        public ThreeDayWeather[] newArray(int size) {
            return new ThreeDayWeather[size];
        }
    };

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
    ThreeDayWeather(Weather firstDayIn, Weather secondDayIn, Weather thirdDayIn, String coordinatesIn){
        firstDay = firstDayIn;
        secondDay = secondDayIn;
        thirdDay = thirdDayIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
