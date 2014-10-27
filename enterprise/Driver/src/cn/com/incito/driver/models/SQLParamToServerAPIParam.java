
package cn.com.incito.driver.models;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

/**
 * 查询语句参数 转换成 Map键值对集合
 * 
 * @description 查询语句参数 转换成 Map键值对集合
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class SQLParamToServerAPIParam {
    /**
     * 将查询语句中的条件语句与查询参数数组转换成键值对集合
     * 
     * @param selection 数据库查询条件语句
     * @param selectionArgs 数据库查询条件参数 数组
     * @return
     */
    public static Map<String, Object> parseParams(String selection, String[] selectionArgs) {
        final String equalTag = "=?";

        HashMap<String, Object> keyValues = new HashMap<String, Object>();
        if (selection != null && selectionArgs != null && !TextUtils.isEmpty(selection)) {
            //			selection = selection.toLowerCase();
            String[] keys = selection.split("AND");
            for (int index = 0; index < keys.length; ++index) {
                String key = keys[index].trim();
                int tagIndex = key.indexOf(equalTag);
                if (tagIndex <= 0) {
                    throw new IllegalArgumentException();
                }
                key = key.substring(0, tagIndex);
                key = key.trim();
                if (TextUtils.isEmpty(key)) {
                    throw new IllegalArgumentException();
                }
                keyValues.put(key, (index < selectionArgs.length) ? selectionArgs[index] : "");
            }
        }
        return keyValues;
    }
}
