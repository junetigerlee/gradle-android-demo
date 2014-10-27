package cn.com.incito.wisdom.sdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtils {
    public static String getVersion(Context appContext) {
        String version = "1.0.0";
        PackageManager packageManager = appContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    appContext.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    
    public static int getVersionCode(Context appContext) {
        int version = 1000;
        PackageManager packageManager = appContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    appContext.getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getPackageName(Context appContext) {
        String packageName = "com.xxx.sdk";
        PackageManager packageManager = appContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    appContext.getPackageName(), 0);
            packageName = packageInfo.packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }
}
