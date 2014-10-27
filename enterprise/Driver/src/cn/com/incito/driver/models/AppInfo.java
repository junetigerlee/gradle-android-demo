
package cn.com.incito.driver.models;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * AppInfo
 * 
 * @description AppInfo
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class AppInfo {
    public String appName = "";

    public String packageName = "";

    public String versionName = "";

    public int versionCode = 0;

    public Drawable appIcon = null;

    public void print() {
        Log.v("app", "Name:" + appName + " Package:" + packageName);
        Log.v("app", "Name:" + appName + " versionName:" + versionName);
        Log.v("app", "Name:" + appName + " versionCode:" + versionCode);
    }

}
