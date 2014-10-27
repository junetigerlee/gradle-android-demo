
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.dao.Comment;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单评价货代 api
 * 
 * @description 订单评价货代 api
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class EvaluateAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/orderEvaluate";

    private final String mOrdersId;

    private final String mAgentId;

    private final Comment mComment;

    public EvaluateAPI(String ordersId, String agentId, Comment comment) {
        super(RELATIVE_URL);
        mOrdersId = ordersId;
        mAgentId = agentId;
        mComment = comment;
    }

    protected String getPostString() {
        JSONObject jsonObject_evaluate = new JSONObject();
        try {
            jsonObject_evaluate.put("orderid", mOrdersId);
            jsonObject_evaluate.put("agentid", mAgentId);
            jsonObject_evaluate.put("level", mComment.getLevel());
            jsonObject_evaluate.put("content", mComment.getContent());
            jsonObject_evaluate.put("type", mComment.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject_evaluate.toString();
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        JSONObject jsonObject_evaluate = new JSONObject();
        try {
            jsonObject_evaluate.put("orderid", mOrdersId);
            jsonObject_evaluate.put("agentid", mAgentId);
            jsonObject_evaluate.put("level", mComment.getLevel());
            jsonObject_evaluate.put("content", mComment.getContent());
            jsonObject_evaluate.put("type", mComment.getStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret.put("json", jsonObject_evaluate.toString());
        return ret;
    }

    protected int getHttpRequestType() {
        return HTTP_REQUEST_TYPE_POST;
    }
}
