package cn.com.incito.wisdom.sdk.log;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

public class DeveloperMode {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static final void enableStrictMode() {
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().penaltyLog().penaltyDeath().build());
        }
    }
}
