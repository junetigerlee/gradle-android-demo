
package cn.com.incito.driver.util;

import android.content.Context;
import android.content.res.Resources;

/**
 * 获取资源id工具类
 * 
 * @description 获取资源id工具类
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GetResourcesUtil {

    /**
     * get drawable Identifier
     * 
     * @param mContext
     * @param type
     * @return
     */
    public static int getDrawableIdentifier(Context mContext, String type) {

        Resources res = mContext.getResources();

        return res.getIdentifier(type, "drawable", mContext.getPackageName());
    }
}
