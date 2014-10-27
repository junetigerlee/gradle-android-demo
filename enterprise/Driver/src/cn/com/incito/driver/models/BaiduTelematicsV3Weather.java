
package cn.com.incito.driver.models;

import java.util.ArrayList;
import java.util.List;

/**
 * BaiduTelematicsV3Weather
 * 
 * @description BaiduTelematicsV3Weather
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class BaiduTelematicsV3Weather {

    private String error;

    private String status;

    private String date;

    private List<BaiduTelematicsV3WeatherCity> results = new ArrayList<BaiduTelematicsV3WeatherCity>();

    public BaiduTelematicsV3Weather(String error, String status, String date, List<BaiduTelematicsV3WeatherCity> results) {
        super();
        this.error = error;
        this.status = status;
        this.date = date;
        this.results = results;
    }

    public BaiduTelematicsV3Weather() {
        super();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BaiduTelematicsV3WeatherCity> getResults() {
        return results;
    }

    public void setResults(List<BaiduTelematicsV3WeatherCity> results) {
        this.results = results;
    }

}
