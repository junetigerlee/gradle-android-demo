
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 删除订单
 * 
 * @description 删除订单
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class DeleteOrderAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/deleteOrder";

    private final String mOrderId;

    public DeleteOrderAPI(String mOrderId) {
        super(RELATIVE_URL);
        this.mOrderId = mOrderId;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("id", mOrderId);
        ret.put("type", "2");
        return ret;
    }

}
