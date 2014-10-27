package cn.com.incito.wisdom.sdk.utils;

import java.util.List;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;
import cn.com.incito.wisdom.sdk.log.WLog;

public class IntentUtils {

    public static boolean startIntentSafe(final Activity activity, Intent it,
            int failStr) {
        if (it != null && activity != null) {
            try {
                activity.startActivity(it);
                return true;
            } catch (ActivityNotFoundException e) {
                WLog.logStackTrace(e);
            } catch (SecurityException e) {
                WLog.logStackTrace(e);
            }
        } else {
            if (failStr != 0) {
                Toast.makeText(activity, failStr, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public static boolean startIntentForResultSafe(final Activity activity,
            Intent it, int requestCode, int failStr) {
        if (it != null) {
            try {
                activity.startActivityForResult(it, requestCode);
                return true;
            } catch (ActivityNotFoundException e) {
                WLog.logStackTrace(e);
            } catch (SecurityException e) {
                WLog.logStackTrace(e);
            }
        } else {
            if (failStr != 0) {
                Toast.makeText(activity, failStr, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    public static boolean hasActivityResolver(PackageManager pm, Intent intent) {
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !activities.isEmpty();
    }

    public static boolean hasServiceResolver(PackageManager pm, Intent intent) {
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> services = pm.queryIntentServices(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return !services.isEmpty();
    }

    @TargetApi(4)
    public static Intent getSMSIntent(List<String> toList, String content) {
        String spc = ",";
        if (Build.MANUFACTURER.contains("HTC")) {
            spc = ";";
        }
        Intent it = new Intent();
        it.setAction(Intent.ACTION_SENDTO);
        StringBuilder sb = new StringBuilder();
        for (String address : toList) {
            if (sb.length() > 0)
                sb.append(spc);
            sb.append(address);
        }
        it.putExtra("address", sb.toString());
        if (!TextUtils.isEmpty(content)) {
            it.putExtra("sms_body", content);
        }
        it.setData(Uri.fromParts("sms", sb.toString(), null));
        return it;
    }

    public static Intent getSMSIntent(String content) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.putExtra("sms_body", content);
        it.setType("vnd.android-dir/mms-sms");
        return it;
    }

    public static Intent getBrowseWebIntent(String urlStr) {
        Intent it = new Intent();
        Uri uri = null;
        if (urlStr == null)
            urlStr = "";
        it.setAction(Intent.ACTION_VIEW);
        // simple checking for http protocol head, should be regular in a
        // tool class
        if (!Pattern.matches("^[\\w-]+://.*$", urlStr)) {
            urlStr = "http://" + urlStr;
        }
        uri = Uri.parse(urlStr);
        it.setData(uri);
        return it;
    }

    public static Intent getCallItent(String number) {
        Intent it = new Intent(Intent.ACTION_CALL);
        it.setData(Uri.fromParts("tel", number, null));
        return it;
    }

    public static Intent getDialItent(String number) {
        Intent it = new Intent(Intent.ACTION_DIAL);
        it.setData(Uri.fromParts("tel", number, null));
        return it;
    }
}
