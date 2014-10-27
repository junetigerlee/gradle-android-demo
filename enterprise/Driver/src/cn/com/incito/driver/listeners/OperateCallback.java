
package cn.com.incito.driver.listeners;

import java.util.Map;

/**
 * 回调接口
 * 
 * @description 回调接口
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public interface OperateCallback {
    /**
	 */
    void onOperate(int resId, Map<String, Object> params);

    /**
	 */
    void onReservFinished();

    /**
	 */
    void onOperateFinished();
}
