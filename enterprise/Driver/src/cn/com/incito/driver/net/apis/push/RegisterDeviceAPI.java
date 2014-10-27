
package cn.com.incito.driver.net.apis.push;

import cn.com.incito.driver.Constants;
import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * register device push info api
 * 
 * @description register device push info api
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class RegisterDeviceAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/device/registerdevice";

    private final String mChannelId;

    private final String mUserId;

    public RegisterDeviceAPI(String channelId, String userId) {
        super(RELATIVE_URL);
        mChannelId = channelId;
        mUserId = userId;
    }

    protected String getPostString() {
        JSONObject jsonObject_evaluate = new JSONObject();
        try {
            jsonObject_evaluate.put("type", Constants.BAIDU_PUSH_USER_TYPE);
            jsonObject_evaluate.put("channelId", mChannelId);
            jsonObject_evaluate.put("userId", mUserId);
            jsonObject_evaluate.put("carid", GlobalModel.getInst().mLoginModel.getCarId());

            if (Constants.RELEASE_SERVER) {
                jsonObject_evaluate.put("pushEnv", Constants.API_SERVER_NAME);
            } else {
                jsonObject_evaluate.put("pushEnv", Constants.TEST_SERVER_IP);
            }

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
            jsonObject_evaluate.put("type", Constants.BAIDU_PUSH_USER_TYPE);
            jsonObject_evaluate.put("channelId", mChannelId);
            jsonObject_evaluate.put("userId", mUserId);
            jsonObject_evaluate.put("carid", GlobalModel.getInst().mLoginModel.getCarId());
            if (Constants.RELEASE_SERVER) {
                jsonObject_evaluate.put("pushEnv", Constants.API_SERVER_NAME);
            } else {
                jsonObject_evaluate.put("pushEnv", Constants.TEST_SERVER_IP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ret.put("json", jsonObject_evaluate.toString());
        return ret;
    }

    @Override
    public RegisterDeviceAPIResponse parseResponse(JSONObject json) {
        try {
            return new RegisterDeviceAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class RegisterDeviceAPIResponse extends BasicResponse {
        public final String mTag;

        public RegisterDeviceAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mTag = json.getString("tag");
        }

    }

    protected int getHttpRequestType() {
        return HTTP_REQUEST_TYPE_POST;
    }
}
