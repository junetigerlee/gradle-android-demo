
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 订单付款
 * 
 * @description 订单付款
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class PaymentOrdersAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/confirmCarPayment";

    private final String mOrdersId;

    public PaymentOrdersAPI(String ordersId) {
        super(RELATIVE_URL);
        this.mOrdersId = ordersId;
    }

    @Override
    protected RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("orderId", mOrdersId);
        return ret;
    }

    @Override
    protected int getHttpRequestType() {
        return WisdomCityServerAPI.HTTP_REQUEST_TYPE_GET;
    }

}
