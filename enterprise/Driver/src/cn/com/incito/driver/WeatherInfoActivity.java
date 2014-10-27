
package cn.com.incito.driver;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.incito.driver.models.BaiduTelematicsV3WeatherCity;
import cn.com.incito.driver.models.BaiduTelematicsV3WeatherData;
import cn.com.incito.driver.util.WeatherIconUtil;

/**
 * 天气信息
 * 
 * @description 天气信息
 * @author lizhan
 * @createDate 2014年10月13日
 * @version 1.0
 */
public class WeatherInfoActivity extends FragmentActivity {

    private GridView gridView;

    private LinearLayout exitTV;

    private GridAdapter gridAdapter;

    private WeatherData data;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_weather);
        gridView = (GridView) findViewById(R.id.grid_view);
        exitTV = (LinearLayout) findViewById(R.id.weather_exit);
        exitTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WeatherInfoActivity.this.finish();
            }
        });
        gridAdapter = new GridAdapter(this);
        /*
         * List<BaiduTelematicsV3WeatherCity> baiduTelematicsV3WeatherCities =
         * new ArrayList<BaiduTelematicsV3WeatherCity>();
         * BaiduTelematicsV3WeatherCity baiduTelematicsV3WeatherCity0 = new
         * BaiduTelematicsV3WeatherCity();
         * baiduTelematicsV3WeatherCity0.setCurrentCity("襄阳");
         * List<BaiduTelematicsV3WeatherData> weather_data0 = new
         * ArrayList<BaiduTelematicsV3WeatherData>();
         * BaiduTelematicsV3WeatherData weather_data0_0 = new
         * BaiduTelematicsV3WeatherData();
         * weather_data0_0.setDate("周五 06月06日 (实时：28℃)"); weather_data0_0
         * .setDayPictureUrl
         * ("http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data0_0 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data0_0.setWeather("多云转晴"); weather_data0_0.setWind("微风");
         * weather_data0_0.setTemperature("31 ~ 22℃");
         * weather_data0.add(weather_data0_0); BaiduTelematicsV3WeatherData
         * weather_data0_1 = new BaiduTelematicsV3WeatherData();
         * weather_data0_1.setDate("周六"); weather_data0_1 .setDayPictureUrl(
         * "http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data0_1 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data0_1.setWeather("多云转晴"); weather_data0_1.setWind("微风");
         * weather_data0_1.setTemperature("31 ~ 22℃");
         * weather_data0.add(weather_data0_1); BaiduTelematicsV3WeatherData
         * weather_data0_2 = new BaiduTelematicsV3WeatherData();
         * weather_data0_2.setDate("周日"); weather_data0_2 .setDayPictureUrl(
         * "http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data0_2 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data0_2.setWeather("多云转晴"); weather_data0_2.setWind("微风");
         * weather_data0_2.setTemperature("31 ~ 22℃");
         * weather_data0.add(weather_data0_2); BaiduTelematicsV3WeatherData
         * weather_data0_3 = new BaiduTelematicsV3WeatherData();
         * weather_data0_3.setDate("周一"); weather_data0_3 .setDayPictureUrl(
         * "http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data0_3 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data0_3.setWeather("多云转晴"); weather_data0_3.setWind("微风");
         * weather_data0_3.setTemperature("31 ~ 22℃");
         * weather_data0.add(weather_data0_3);
         * 
         * baiduTelematicsV3WeatherCity0.setWeather_data(weather_data0);
         * 
         * baiduTelematicsV3WeatherCities.add(baiduTelematicsV3WeatherCity0);
         * 
         * BaiduTelematicsV3WeatherCity baiduTelematicsV3WeatherCity1 = new
         * BaiduTelematicsV3WeatherCity();
         * baiduTelematicsV3WeatherCity1.setCurrentCity("北京");
         * List<BaiduTelematicsV3WeatherData> weather_data1 = new
         * ArrayList<BaiduTelematicsV3WeatherData>();
         * BaiduTelematicsV3WeatherData weather_data1_0 = new
         * BaiduTelematicsV3WeatherData();
         * weather_data1_0.setDate("周五 06月06日 (实时：28℃)"); weather_data1_0
         * .setDayPictureUrl
         * ("http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data1_0 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data1_0.setWeather("多云转晴"); weather_data1_0.setWind("微风");
         * weather_data1_0.setTemperature("31 ~ 22℃");
         * weather_data1.add(weather_data1_0); BaiduTelematicsV3WeatherData
         * weather_data1_1 = new BaiduTelematicsV3WeatherData();
         * weather_data1_1.setDate("周六"); weather_data1_1 .setDayPictureUrl(
         * "http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data1_1 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data1_1.setWeather("多云转晴"); weather_data1_1.setWind("微风");
         * weather_data1_1.setTemperature("31 ~ 22℃");
         * weather_data1.add(weather_data1_1); BaiduTelematicsV3WeatherData
         * weather_data1_2 = new BaiduTelematicsV3WeatherData();
         * weather_data1_2.setDate("周日"); weather_data1_2 .setDayPictureUrl(
         * "http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data1_2 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data1_2.setWeather("多云转晴"); weather_data1_2.setWind("微风");
         * weather_data1_2.setTemperature("31 ~ 22℃");
         * weather_data1.add(weather_data1_2); BaiduTelematicsV3WeatherData
         * weather_data1_3 = new BaiduTelematicsV3WeatherData();
         * weather_data1_3.setDate("周一"); weather_data1_3 .setDayPictureUrl(
         * "http://api.map.baidu.com/images/weather/day/duoyun.png");
         * weather_data1_3 .setNightPictureUrl(
         * "http://api.map.baidu.com/images/weather/night/qing.png");
         * weather_data1_3.setWeather("多云转晴"); weather_data1_3.setWind("微风");
         * weather_data1_3.setTemperature("31 ~ 22℃");
         * weather_data1.add(weather_data1_3);
         * 
         * baiduTelematicsV3WeatherCity1.setWeather_data(weather_data1);
         * 
         * baiduTelematicsV3WeatherCities.add(baiduTelematicsV3WeatherCity1);
         * baiduTelematicsV3WeatherCities.add(baiduTelematicsV3WeatherCity0);
         * baiduTelematicsV3WeatherCities.add(baiduTelematicsV3WeatherCity1);
         * gridAdapter.setList(baiduTelematicsV3WeatherCities);
         */

        data = (WeatherData) getIntent().getSerializableExtra("data");
        if (data != null) {
            gridAdapter.setList(data.getWeathFormatData());
            gridView.setAdapter(gridAdapter);
        }

    }

    public class GridAdapter extends BaseAdapter {

        private Context context;

        private List<BaiduTelematicsV3WeatherCity> list;

        private LayoutInflater mInflater;

        public GridAdapter(Context c) {
            super();
            this.context = c;
        }

        public void setList(List<BaiduTelematicsV3WeatherCity> list) {
            this.list = list;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int index) {

            return list.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            Viewholder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_weather_gridview_item, null);
                holder = new Viewholder();
                holder.mCurrentCity = (TextView) convertView.findViewById(R.id.cityname);
                // 今天
                holder.mDate0 = (TextView) convertView.findViewById(R.id.date_0);
                holder.mWeatherPictureUrl0 = (ImageView) convertView.findViewById(R.id.weather_pic_0);
                // holder.mWind0 = (TextView) convertView
                // .findViewById(R.id.wind_0);
                holder.mWeather0 = (TextView) convertView.findViewById(R.id.weather_0);
                holder.mTemperature0 = (TextView) convertView.findViewById(R.id.temperature_0);
                // 明天
                holder.mDate1 = (TextView) convertView.findViewById(R.id.date_1);
                holder.mWeatherPictureUrl1 = (ImageView) convertView.findViewById(R.id.weather_pic_1);
                // holder.mWind1 = (TextView) convertView
                // .findViewById(R.id.wind_1);
                holder.mWeather1 = (TextView) convertView.findViewById(R.id.weather_1);
                holder.mTemperature1 = (TextView) convertView.findViewById(R.id.temperature_1);
                // 后天
                holder.mDate2 = (TextView) convertView.findViewById(R.id.date_2);
                holder.mWeatherPictureUrl2 = (ImageView) convertView.findViewById(R.id.weather_pic_2);
                // holder.mWind2 = (TextView) convertView
                // .findViewById(R.id.wind_2);
                holder.mWeather2 = (TextView) convertView.findViewById(R.id.weather_2);
                holder.mTemperature2 = (TextView) convertView.findViewById(R.id.temperature_2);
                // 大后天
                holder.mDate3 = (TextView) convertView.findViewById(R.id.date_3);
                holder.mWeatherPictureUrl3 = (ImageView) convertView.findViewById(R.id.weather_pic_3);
                // holder.mWind3 = (TextView) convertView
                // .findViewById(R.id.wind_3);
                holder.mWeather3 = (TextView) convertView.findViewById(R.id.weather_3);
                holder.mTemperature3 = (TextView) convertView.findViewById(R.id.temperature_3);

                convertView.setTag(holder);

            } else {
                holder = (Viewholder) convertView.getTag();

            }
            BaiduTelematicsV3WeatherCity info = list.get(index);
            if (info != null) {
                holder.mCurrentCity.setText(info.getCurrentCity());

                BaiduTelematicsV3WeatherData weather_data = info.getWeather_data().get(0);
                // 今天
                // holder.mDate0.setText(weather_data.getDate());
                holder.mDate0.setText("今天");
                holder.mWeatherPictureUrl0.setBackgroundResource(new WeatherIconUtil().transferImg(weather_data
                        .getWeather()));
                // ImageLoader.getInstance().displayImage(
                // weather_data.getDayPictureUrl(),
                // holder.mWeatherPictureUrl0);
                // holder.mWind0.setText(weather_data.getWind());
                holder.mWeather0.setText(weather_data.getWeather());
                holder.mTemperature0.setText(weather_data.getTemperature());
                // 明天
                weather_data = info.getWeather_data().get(1);
                holder.mDate1.setText(weather_data.getDate());
                // ImageLoader.getInstance().displayImage(
                // weather_data.getDayPictureUrl(),
                // holder.mWeatherPictureUrl1);
                holder.mWeatherPictureUrl1.setBackgroundResource(new WeatherIconUtil().transferImg(weather_data
                        .getWeather()));
                // holder.mWind1.setText(weather_data.getWind());
                holder.mWeather1.setText(weather_data.getWeather());
                holder.mTemperature1.setText(weather_data.getTemperature());
                // 后台
                weather_data = info.getWeather_data().get(2);
                holder.mDate2.setText(weather_data.getDate());
                // ImageLoader.getInstance().displayImage(
                // weather_data.getDayPictureUrl(),
                // holder.mWeatherPictureUrl2);
                holder.mWeatherPictureUrl2.setBackgroundResource(new WeatherIconUtil().transferImg(weather_data
                        .getWeather()));
                // holder.mWind2.setText(weather_data.getWind());
                holder.mWeather2.setText(weather_data.getWeather());
                holder.mTemperature2.setText(weather_data.getTemperature());
                // 大后天
                weather_data = info.getWeather_data().get(3);
                holder.mDate3.setText(weather_data.getDate());
                // ImageLoader.getInstance().displayImage(
                // weather_data.getDayPictureUrl(),
                // holder.mWeatherPictureUrl3);
                holder.mWeatherPictureUrl3.setBackgroundResource(new WeatherIconUtil().transferImg(weather_data
                        .getWeather()));
                // holder.mWind3.setText(weather_data.getWind());
                holder.mWeather3.setText(weather_data.getWeather());
                holder.mTemperature3.setText(weather_data.getTemperature());
            }
            return convertView;
        }

    }

    class Viewholder {
        TextView mCurrentCity;

        // 今天
        TextView mDate0;

        // ImageView mDayPictureUrl0;
        // ImageView mNightPictureUrl0;
        ImageView mWeatherPictureUrl0;

        TextView mWeather0;

        // TextView mWind0;
        TextView mTemperature0;

        // 明天
        TextView mDate1;

        // ImageView mDayPictureUrl1;
        // ImageView mNightPictureUrl1;
        ImageView mWeatherPictureUrl1;

        TextView mWeather1;

        // TextView mWind1;
        TextView mTemperature1;

        // 后天
        TextView mDate2;

        // ImageView mDayPictureUrl1;
        // ImageView mNightPictureUrl1;
        ImageView mWeatherPictureUrl2;

        TextView mWeather2;

        // TextView mWind2;
        TextView mTemperature2;

        // 大后天
        TextView mDate3;

        // ImageView mDayPictureUrl1;
        // ImageView mNightPictureUrl1;
        ImageView mWeatherPictureUrl3;

        TextView mWeather3;

        // TextView mWind3;
        TextView mTemperature3;

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    public static class WeatherData implements Serializable {
        private static final long serialVersionUID = 1L;

        private List<BaiduTelematicsV3WeatherCity> weathFormatData;

        public List<BaiduTelematicsV3WeatherCity> getWeathFormatData() {
            return weathFormatData;
        }

        public void setWeathFormatData(List<BaiduTelematicsV3WeatherCity> weathFormatData) {
            this.weathFormatData = weathFormatData;
        }

    }
}
