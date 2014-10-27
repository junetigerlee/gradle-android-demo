
package cn.com.incito.driver.net.apis.orders;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incito.driver.models.ModelSignInfo;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import com.google.gson.Gson;

/**
 * 获取订单签收信息接口
 * 
 * @description 获取订单签收信息接口
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GetSignInfoAPI extends WisdomCityServerAPI {
    private static final String RELATIVE_URL = "/logistics/order/signDetail";

    private String mOrderId;

    public GetSignInfoAPI(String orderId) {
        super(RELATIVE_URL);
        mOrderId = orderId;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("id", mOrderId);
        return ret;
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new GetSignInfoAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class GetSignInfoAPIResponse extends BasicResponse {
        public final ModelSignInfo mSignInfo;

        public GetSignInfoAPIResponse(JSONObject json) throws JSONException {
            super(json);
            String signInfoJsonStr = json.getString("order");
            Gson gson = new Gson();
            mSignInfo = gson.fromJson(signInfoJsonStr, ModelSignInfo.class);
        }
    }
}
