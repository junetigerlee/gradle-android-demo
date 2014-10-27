
package cn.com.incito.driver.net.apis;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 确认揽活的api
 * 
 * @description 确认揽活的api
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class CanvassingOrderAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/canvassingOrder";

    private final String mOrderId;

    private final String mLeavelocation;

    private final String localcity;

    public CanvassingOrderAPI(String orderId, String leavelocation, String localcity) {
        super(RELATIVE_URL);
        mOrderId = orderId;
        mLeavelocation = leavelocation;
        this.localcity = localcity;
    }

    @Override
    protected String getPostString() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mOrderId);
            json.put("leavelocation", mLeavelocation);
            json.put("localcity", localcity);
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
            json.put("leavelocation", mLeavelocation);
            json.put("localcity", localcity);
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
