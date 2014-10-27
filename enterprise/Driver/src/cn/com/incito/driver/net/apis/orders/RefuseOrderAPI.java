
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.Constants;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 拒单
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class RefuseOrderAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/refuseOrder";

    private final String mOrderId;

    private final String mRefusalreason;

    private final String mRefusalmemo;

    public RefuseOrderAPI(String mOrderId, String mRefusalreason, String mRefusalmemo) {
        super(RELATIVE_URL);
        this.mOrderId = mOrderId;
        this.mRefusalreason = mRefusalreason;
        this.mRefusalmemo = mRefusalmemo;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mOrderId);
            jsonObject.put(Constants.REFUSE_REASON_REFUSALREASON, mRefusalreason);
            jsonObject.put(Constants.REFUSE_REASON_REFUSALMEMO, mRefusalmemo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret.put("json", jsonObject.toString());
        return ret;
    }

    @Override
    protected String getPostString() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mOrderId);
            json.put(Constants.REFUSE_REASON_REFUSALREASON, mRefusalreason);
            json.put(Constants.REFUSE_REASON_REFUSALMEMO, mRefusalmemo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    protected int getHttpRequestType() {
        return HTTP_REQUEST_TYPE_POST;
    }
}
