
package cn.com.incito.driver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import cn.com.incito.driver.R;

/**
 * WeatherCityUtil
 * 
 * @description WeatherCityUtil
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class WeatherCityUtil {
    private Context mContext;

    public WeatherCityUtil(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /**
     * 读取本地文件中JSON字符串
     * 
     * @param fileName
     * @return
     */
    public String getCityJson() {

        InputStream im = mContext.getResources().openRawResource(R.raw.city_weather_code);
        BufferedReader read = new BufferedReader(new InputStreamReader(im));
        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = read.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                    read = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (im != null) {
                try {
                    im.close();
                    im = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //			Log.v("result = ", "result = " + sb.toString().trim());
        return sb.toString().trim();
    }
}
