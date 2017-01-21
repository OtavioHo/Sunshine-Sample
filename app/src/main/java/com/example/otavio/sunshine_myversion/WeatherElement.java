package com.example.otavio.sunshine_myversion;

import android.media.Image;

/**
 * Created by otavio on 24/08/16.
 */
public class WeatherElement {
    private String date;
    private String min, max;

    public WeatherElement(String date, String min, String max) {
        this.date = date;
        this.min = min;
        this.max = max;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
