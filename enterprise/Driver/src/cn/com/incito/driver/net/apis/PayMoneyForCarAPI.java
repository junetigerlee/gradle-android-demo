
package cn.com.incito.driver.net.apis;

import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 司机付款api
 * 
 * @description 司机付款api
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class PayMoneyForCarAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/paymoenyForCar";

    private final String mOrderId;

    public PayMoneyForCarAPI(String orderId) {
        super(RELATIVE_URL);
        mOrderId = orderId;
    }

    @Override
    protected String getPostString() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mOrderId);
            json.put("totalload", GlobalModel.getInst().mLoginModel.getTotalLoad());
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
            json.put("totalload", GlobalModel.getInst().mLoginModel.getTotalLoad());
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
