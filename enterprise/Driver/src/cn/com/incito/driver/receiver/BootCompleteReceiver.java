
package cn.com.incito.driver.receiver;

import cn.com.incito.driver.DriverMainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 自启广播接收器
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent startIntent = new Intent();
            startIntent.setClass(context, DriverMainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }

}
