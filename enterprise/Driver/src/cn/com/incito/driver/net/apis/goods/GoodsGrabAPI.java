
package cn.com.incito.driver.net.apis.goods;

import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 抢货源
 * 
 * @description 抢货源
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsGrabAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/grabgoods/grab";

    private String mId = "";

    private String mGoodsId = "";

    private String mGeneratetime = "";

    private String mGoodsTime = "";

    public GoodsGrabAPI(String id, String goodsId, String generateTime, String goodsTime) {
        super(RELATIVE_URL);
        mId = id;
        mGoodsId = goodsId;
        mGeneratetime = generateTime;
        mGoodsTime = goodsTime;

    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("id", mId);
        ret.put("goodsid", mGoodsId);
        ret.put("generatetime", mGeneratetime);
        ret.put("goodstime", mGoodsTime);
        return ret;
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) throws JSONException {
        return new GoodsGrabAPIResponse(json);
    }

    public class GoodsGrabAPIResponse extends BasicResponse {

        public GoodsGrabAPIResponse(JSONObject json) throws JSONException {
            super(json);
        }

    }

}
