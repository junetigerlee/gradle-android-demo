
package cn.com.incito.driver.push.baidu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 百度推送消息接收器
 * 
 * @description 百度推送消息接收器
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class PushMessageReceiver extends BroadcastReceiver {
    public static final String TAG = PushMessageReceiver.class.getSimpleName();

    private static final String ACTION_RECEIVER_NOTIFICATION_SHOW = "com.baidu.android.pushservice.action.notification.SHOW";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_RECEIVER_NOTIFICATION_SHOW)) {
            Log.i(TAG, "intent.getExtras().size()" + intent.getExtras().size());

            Log.i(TAG,
                    "intent.getExtras().getParcelable('public_msg'):"
                            + intent.getExtras().getParcelable("public_msg").toString());
            String publicMsg = intent.getExtras().getParcelable("public_msg").toString();
            // Intent it = new Intent(context, Speech.class); // call service
            // for
            // // Speech.class
            // Bundle bundle = intent.getExtras();
            // bundle.putString("public_msg", publicMsg);
            // it.putExtras(bundle);
            // context.startService(it);

        }
    }
}
