
package cn.com.incito.driver.models;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import cn.com.incito.driver.R;
import cn.com.incito.driver.util.WeatherCityUtil;
import cn.com.incito.wisdom.sdk.image.display.BitmapDisplayerImpl;
import cn.com.incito.wisdom.sdk.image.display.DisplayAnim;
import cn.com.incito.wisdom.sdk.image.display.DisplayShape;
import cn.com.incito.wisdom.sdk.image.loader.DisplayImageOptions;

/**
 * GlobalModel
 * 
 * @description GlobalModel
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class GlobalModel {
    private static GlobalModel sInstance;

    private final Context mContext;

    public final SharedPreferences mPrefs;

    public final DisplayImageOptions mDisplayOptions;

    public final LoginModel mLoginModel;

    /**
     * {"城市代码":[{"省":"",市:[{"市名":"","编码":""}]}]}
     */
    public JSONObject weatherCityCode = null;

    private static final int CHECK_NEW_MESSAGE_ALARM_START_TIME_HOUR = 10;

    private static final int CHECK_NEW_MESSAGE_ALARM_END_TIME_HOUR = 24;

    private static final long CHECK_NEW_MESSAGE_ALARM_INTERVAL_MILLIS = DateUtils.HOUR_IN_MILLIS * 2;

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new GlobalModel(context);
        }
    }

    public final static GlobalModel getInst() {
        return sInstance;
    }

    private GlobalModel(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());

        mLoginModel = new LoginModel(context, mPrefs);

        mDisplayOptions = new DisplayImageOptions.Builder()
                .displayer(new BitmapDisplayerImpl(DisplayShape.DEFAULT, DisplayAnim.FADE_IN, 400))
                .showStubImage(R.drawable.default_photo).showImageForEmptyUri(R.drawable.default_photo).cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        setupCheckNewMessageAlarm();
        //读取气象局发布的城市信息
        WeatherCityUtil weatherCityUtil = new WeatherCityUtil(mContext);
        try {
            weatherCityCode = new JSONObject(weatherCityUtil.getCityJson());
            //			Log.v("weatherCityCode", weatherCityCode.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 城市编码
     * 
     * @param cityName
     * @return
     */
    public String getCityCode(String cityName) {

        String cityCode = "";

        try {
            JSONArray provinceArray = weatherCityCode.getJSONArray("城市代码");
            for (int i = 0; i < provinceArray.length(); i++) {
                JSONObject provinceObject = provinceArray.getJSONObject(i);
                //				Log.v("省", provinceObject.getString("省"));
                JSONArray cityArray = provinceObject.getJSONArray("市");
                for (int j = 0; j < cityArray.length(); j++) {
                    JSONObject cityJson = cityArray.getJSONObject(j);
                    if (cityJson.getString("市名").equals(cityName)) {
                        Log.v("市名", cityJson.getString("市名"));
                        Log.v("编码", cityJson.getString("编码"));
                        cityCode = cityJson.getString("编码");
                        break;
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cityCode;

    }

    public void checkNewMessageAlarmTriggled() {
        long triggleAtMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= CHECK_NEW_MESSAGE_ALARM_START_TIME_HOUR
                && hour <= (CHECK_NEW_MESSAGE_ALARM_END_TIME_HOUR - CHECK_NEW_MESSAGE_ALARM_INTERVAL_MILLIS
                        / DateUtils.HOUR_IN_MILLIS)) {
            triggleAtMillis = calendar.getTimeInMillis() + CHECK_NEW_MESSAGE_ALARM_INTERVAL_MILLIS;
        } else if (hour < CHECK_NEW_MESSAGE_ALARM_START_TIME_HOUR) {
            calendar.set(Calendar.HOUR_OF_DAY, CHECK_NEW_MESSAGE_ALARM_START_TIME_HOUR);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            triggleAtMillis = calendar.getTimeInMillis();
        } else if (hour > (CHECK_NEW_MESSAGE_ALARM_END_TIME_HOUR - CHECK_NEW_MESSAGE_ALARM_INTERVAL_MILLIS
                / DateUtils.HOUR_IN_MILLIS)) {
            calendar.set(Calendar.HOUR_OF_DAY, CHECK_NEW_MESSAGE_ALARM_START_TIME_HOUR);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            triggleAtMillis = calendar.getTimeInMillis() + DateUtils.DAY_IN_MILLIS;
        }
        scheduleCheckNewMessageAlarm(true, triggleAtMillis);
    }

    private void setupCheckNewMessageAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, CHECK_NEW_MESSAGE_ALARM_START_TIME_HOUR);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        scheduleCheckNewMessageAlarm(false, calendar.getTimeInMillis());
    }

    private void scheduleCheckNewMessageAlarm(boolean cancelPrevious, long triggleAtMillis) {

    }

}
