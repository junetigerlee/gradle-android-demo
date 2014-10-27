
package cn.com.incito.driver.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * ParseUtil
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class ParseUtil {

    /**
     * 科学计数法转换如：4E+07--->4000000000
     * 
     * @param string
     * @return
     */
    public static String parseDoubleToString(Double d)// 科学计数法转换
    {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(d);
    }

    public static String parseIntToString(int d)// 科学计数法转换
    {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(d);
    }

    /**
     * 日期转换成yyyy-MM-dd HH:mm显示
     * 
     * @param string
     * @return
     */
    public static String praseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = "";
        try {

            date = dateFormat.format(dateFormat.parse(dateStr));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return dateStr;// 如果出现异常则其有可能是没有填写，揽货的具体小时和分钟，造成解析异常
        }
        return date;
    }
}
