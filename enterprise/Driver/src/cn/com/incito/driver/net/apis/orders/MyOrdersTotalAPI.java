
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.models.orders.MyOrdersTotal;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的订单数量
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyOrdersTotalAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/orderTotal";

    private String mAgentId = null;

    private String mCarId = null;

    private final int mDateStatus;

    private final int type;

    public MyOrdersTotalAPI(String id, int dateStatus, int type) {
        super(RELATIVE_URL);
        if (type == 0) {
            mCarId = id;
        } else {
            mAgentId = id;
        }
        mDateStatus = dateStatus;
        this.type = type;

    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new MyOrdersTotalAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        if (type == 0) {
            ret.put("id", mCarId);
        } else {
            ret.put("id", mAgentId);
        }
        ret.put("type", String.valueOf(type));
        ret.put("datestatus", String.valueOf(mDateStatus));
        return ret;
    }

    public class MyOrdersTotalAPIResponse extends BasicResponse {
        public final MyOrdersTotal mMyOrdersTotal;

        public MyOrdersTotalAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mMyOrdersTotal = new MyOrdersTotal();
            JSONArray dataArray = json.getJSONArray("items");
            JSONObject jsonObject = dataArray.getJSONObject(0);
            mMyOrdersTotal.setAllorder(jsonObject.getString("allorder"));
            mMyOrdersTotal.setPayorder(jsonObject.getString("payorder"));// 待付款
            mMyOrdersTotal.setPickingorder(jsonObject.getString("pickingorder"));// 待配货
            mMyOrdersTotal.setSignorder(jsonObject.getString("signorder"));// 待签收
            mMyOrdersTotal.setEvelorder(jsonObject.getString("evelorder"));// 待评价
            mMyOrdersTotal.setCancelorder(jsonObject.getString("cancelorder"));// 取消
            mMyOrdersTotal.setReceiveorder(jsonObject.getString("appointorder"));//待接单
        }
    }

}
