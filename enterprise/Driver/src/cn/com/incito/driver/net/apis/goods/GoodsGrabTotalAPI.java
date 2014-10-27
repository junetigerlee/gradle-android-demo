
package cn.com.incito.driver.net.apis.goods;

import cn.com.incito.driver.models.goods.GoodsGrabTotal;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 抢货源数量
 * 
 * @description 抢货源数量
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsGrabTotalAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/grabgoods/grabTotal";

    private String mCarId = null;

    public GoodsGrabTotalAPI(String carId) {
        super(RELATIVE_URL);
        mCarId = carId;

    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new GoodsGrabTotalAPIResponse(json);
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

    public class GoodsGrabTotalAPIResponse extends BasicResponse {
        public final GoodsGrabTotal mGoodsGrabTotal;

        public GoodsGrabTotalAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mGoodsGrabTotal = new GoodsGrabTotal();
            mGoodsGrabTotal.setAvailableGoods(json.getString("canGrabNum"));// 可抢货源数量
            mGoodsGrabTotal.setHasAvailableGoods(json.getString("hasGrabNum"));// 已抢货源数量
            mGoodsGrabTotal.setSuccGrabNum(json.getString("succGrabNum"));// 已抢货源成功数量
            mGoodsGrabTotal.setFailGrabNum(json.getString("failGrabNum"));// 已货源失败的数量
        }
    }

}
