
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.dao.Comment;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 司机货代查看订单评价信息
 * 
 * @description 司机货代查看订单评价信息
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class OrderCommentDetailAPI extends WisdomCityServerAPI {
    private static final String RELATIVE_URL = "/logistics/order/orderComentDetail";

    /**
     * 标记是司机段查看评价详情
     */
    public static final String FLAG_DRIVER_LOOK = "1";

    private String mOrderid, mCarid, mAgentid, mType;

    public OrderCommentDetailAPI(String orderid, String carid, String agentid, String type) {
        super(RELATIVE_URL);
        mOrderid = orderid;
        mCarid = carid;
        mAgentid = agentid;
        mType = type;

    }

    @Override
    protected String getPostString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", mOrderid);
            jsonObject.put("carid", mCarid);
            jsonObject.put("agentid", mAgentid);
            jsonObject.put("type", mType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("orderid", mOrderid);
        ret.put("carid", mCarid);
        ret.put("agentid", mAgentid);
        ret.put("type", mType);
        return ret;
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new OrderCommentDetailAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class OrderCommentDetailAPIResponse extends BasicResponse {

        public final HashMap<String, Comment> mCommentMap;

        public OrderCommentDetailAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mCommentMap = new HashMap<String, Comment>();
            Gson gson = new Gson();
            String currComment = null;
            Comment carComment = null;
            Comment agentComment = null;
            if (!json.isNull("carcoment")) {
                currComment = json.getString("carcoment");
                carComment = gson.fromJson(currComment, Comment.class);
            }
            if (!json.isNull("agentcoment")) {
                currComment = json.getString("agentcoment");
                agentComment = gson.fromJson(currComment, Comment.class);
            }
            mCommentMap.put("carcomment", carComment);
            mCommentMap.put("agentcomment", agentComment);
        }
    }

}
