
package cn.com.incito.driver.net.apis.goods;

import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 是否有可抢货源，显示小红点
 * 
 * @description 是否有可抢货源，显示小红点
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsGrabCheckReddotAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/grabgoods/checkReddot";

    private String mCarId = null;

    public GoodsGrabCheckReddotAPI(String carId) {
        super(RELATIVE_URL);
        mCarId = carId;

    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new GoodsGrabCheckReddotAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("carid", mCarId);
        return ret;
    }

    public class GoodsGrabCheckReddotAPIResponse extends BasicResponse {
        public final String mFlag;

        public GoodsGrabCheckReddotAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mFlag = json.isNull("flag") ? "0" : json.getString("flag");// 0不显示，1显示
        }
    }

}
