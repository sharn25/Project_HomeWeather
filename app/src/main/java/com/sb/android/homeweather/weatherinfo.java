package com.sb.android.homeweather;

/**
 * Created by singhsh on 9/15/2019.
 */

public class weatherinfo {
    private int id;
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String description;
    private String icon;
    private int temp;
    private int humidity;
    private double windspeed;
    private int winddeg;
    private int clounds;

    public int getClounds() {
        return clounds;
    }

    public void setClounds(int clounds) {
        this.clounds = clounds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public int getWinddeg() {
        return winddeg;
    }

    public void setWinddeg(int winddeg) {
        this.winddeg = winddeg;
    }
}
