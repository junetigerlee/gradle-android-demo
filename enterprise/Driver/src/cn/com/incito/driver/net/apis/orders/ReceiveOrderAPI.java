
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 接受订单API
 * 
 * @description 接受订单API
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class ReceiveOrderAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/acceptOrder";

    private final String mOrderId;

    public ReceiveOrderAPI(String orderId) {
        super(RELATIVE_URL);
        mOrderId = orderId;
    }

    @Override
    protected String getPostString() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mOrderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        JSONObject json = new JSONObject();
        try {
            json.put("id", mOrderId);
            ret.put("json", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    protected int getHttpRequestType() {
        return HTTP_REQUEST_TYPE_POST;
    }
}
