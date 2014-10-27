
package cn.com.incito.driver.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BaiduTelematicsV3WeatherCity
 * 
 * @description BaiduTelematicsV3WeatherCity
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class BaiduTelematicsV3WeatherCity implements Serializable {

    private String currentCity;

    private List<BaiduTelematicsV3WeatherData> weather_data = new ArrayList<BaiduTelematicsV3WeatherData>();

    public BaiduTelematicsV3WeatherCity() {
        super();
    }

    public BaiduTelematicsV3WeatherCity(String currentCity, List<BaiduTelematicsV3WeatherData> weather_data) {
        super();
        this.currentCity = currentCity;
        this.weather_data = weather_data;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public List<BaiduTelematicsV3WeatherData> getWeather_data() {
        return weather_data;
    }

    public void setWeather_data(List<BaiduTelematicsV3WeatherData> weather_data) {
        this.weather_data = weather_data;
    }

}
