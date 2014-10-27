
package cn.com.incito.driver.util;

import cn.com.incito.driver.R;

import java.util.HashMap;

/**
 * WeatherIconUtil
 * 
 * @description WeatherIconUtil
 * @author zhangyushuang
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class WeatherIconUtil {
    private HashMap<String, String> weatherMap;

    public WeatherIconUtil() {
        weatherMap = new HashMap<String, String>();// 气象局接口数据
        weatherMap.put("晴", "00");
        weatherMap.put("多云", "01");
        weatherMap.put("小雨转多云", "01");
        weatherMap.put("阴", "02");
        weatherMap.put("阴转中雨", "02");
        weatherMap.put("阵雨", "03");
        weatherMap.put("雷阵雨", "04");
        weatherMap.put("雷阵雨伴有冰雹", "05");
        weatherMap.put("雨夹雪", "06");
        weatherMap.put("小雨", "07");
        weatherMap.put("多云转小雨", "07");
        weatherMap.put("中雨", "08");
        weatherMap.put("大雨", "09");
        weatherMap.put("暴雨", "10");
        weatherMap.put("大暴雨", "11");
        weatherMap.put("特大暴雨", "12");
        weatherMap.put("阵雪", "13");
        weatherMap.put("小雪", "14");
        weatherMap.put("中雪", "15");
        weatherMap.put("大雪", "16");
        weatherMap.put("暴雪", "17");
        weatherMap.put("雾", "18");
        weatherMap.put("冻雨", "19");
        weatherMap.put("沙尘暴", "20");
        weatherMap.put("小到中雨", "21");
        weatherMap.put("中到大雨", "22");
        weatherMap.put("大到暴雨", "23");
        weatherMap.put("暴雨到大暴雨", "24");
        weatherMap.put("大暴雨到特大暴雨", "25");
        weatherMap.put("小到中雪", "26");
        weatherMap.put("中到大雪", "27");
        weatherMap.put("大到暴雪", "28");
        weatherMap.put("浮尘", "29");
        weatherMap.put("扬沙", "30");
        weatherMap.put("强沙尘暴", "31");
        weatherMap.put("霾", "53");
        weatherMap.put("无", "99");
    }

    // 工具方法，该方法负责把返回的天气图标字符串，转换为程序的图片资源ID。
    public int transferImg(String weather) {
        System.out.println(weather);
        if (weatherMap.get(weather) == null)
            return R.drawable.undefined;
        if (weather.indexOf("转") == -1)
            return transferIcon(weatherMap.get(weather));
        System.out.println(weather.split("转")[1]);
        return transferIcon(weatherMap.get(weather.split("转")[1]));
    }

    // 工具方法，该方法负责把返回的天气图标字符串，转换为程序的图片资源ID。
    public int transferIcon(String strIcon) {
        if (strIcon == null)
            return R.drawable.undefined;
        if ("00".equals(strIcon))
            return R.drawable.a_0;
        if ("01".equals(strIcon))
            return R.drawable.a_1;
        if ("02".equals(strIcon))
            return R.drawable.a_2;
        if ("03".equals(strIcon))
            return R.drawable.a_3;
        if ("04".equals(strIcon))
            return R.drawable.a_4;
        if ("05".equals(strIcon))
            return R.drawable.a_5;
        if ("06".equals(strIcon))
            return R.drawable.a_6;
        if ("07".equals(strIcon))
            return R.drawable.a_7;
        if ("08".equals(strIcon))
            return R.drawable.a_8;
        if ("09".equals(strIcon))
            return R.drawable.a_9;
        if ("10".equals(strIcon))
            return R.drawable.a_10;
        if ("11".equals(strIcon))
            return R.drawable.a_11;
        if ("12".equals(strIcon))
            return R.drawable.a_12;
        if ("13".equals(strIcon))
            return R.drawable.a_13;
        if ("14".equals(strIcon))
            return R.drawable.a_14;
        if ("15".equals(strIcon))
            return R.drawable.a_15;
        if ("16".equals(strIcon))
            return R.drawable.a_16;
        if ("17".equals(strIcon))
            return R.drawable.a_17;
        if ("18".equals(strIcon))
            return R.drawable.a_18;
        if ("19".equals(strIcon))
            return R.drawable.a_19;
        if ("20".equals(strIcon))
            return R.drawable.a_20;
        if ("21".equals(strIcon))
            return R.drawable.a_21;
        if ("22".equals(strIcon))
            return R.drawable.a_22;
        if ("23".equals(strIcon))
            return R.drawable.a_23;
        if ("24".equals(strIcon))
            return R.drawable.a_24;
        if ("25".equals(strIcon))
            return R.drawable.a_25;
        if ("26".equals(strIcon))
            return R.drawable.a_26;
        if ("27".equals(strIcon))
            return R.drawable.a_27;
        if ("28".equals(strIcon))
            return R.drawable.a_28;
        if ("29".equals(strIcon))
            return R.drawable.a_29;
        if ("30".equals(strIcon))
            return R.drawable.a_30;
        if ("31".equals(strIcon))
            return R.drawable.a_31;
        if ("53".equals(strIcon))
            return R.drawable.a_53;
        return R.drawable.undefined;
    }
}
