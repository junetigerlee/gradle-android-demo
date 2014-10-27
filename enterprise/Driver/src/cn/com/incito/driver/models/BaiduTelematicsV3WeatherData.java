
package cn.com.incito.driver.models;

import java.io.Serializable;

/**
 * BaiduTelematicsV3WeatherData
 * 
 * @description BaiduTelematicsV3WeatherData
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class BaiduTelematicsV3WeatherData implements Serializable {

    private String date;

    private String dayPictureUrl;

    private String nightPictureUrl;

    private String weather;

    private String wind;

    private String temperature;

    public BaiduTelematicsV3WeatherData() {
        super();
    }

    public BaiduTelematicsV3WeatherData(String date, String dayPictureUrl, String nightPictureUrl, String weather,
            String wind, String temperature) {
        super();
        this.date = date;
        this.dayPictureUrl = dayPictureUrl;
        this.nightPictureUrl = nightPictureUrl;
        this.weather = weather;
        this.wind = wind;
        this.temperature = temperature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayPictureUrl() {
        return dayPictureUrl;
    }

    public void setDayPictureUrl(String dayPictureUrl) {
        this.dayPictureUrl = dayPictureUrl;
    }

    public String getNightPictureUrl() {
        return nightPictureUrl;
    }

    public void setNightPictureUrl(String nightPictureUrl) {
        this.nightPictureUrl = nightPictureUrl;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}
