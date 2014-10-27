
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 签收/拒签API
 * 
 * @description 签收/拒签API
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class SignAndRefuseOrderAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/order/signOrder";

    private final String mOrderId;

    private final String mType;

    private final String mPhotoUrl;

    private final String mMemo;

    private final String mRefusal;

    private final String mPhotoLocation;

    private final String mPhotoAddress;

    public SignAndRefuseOrderAPI(String orderId, String type, String photoUrl, String photoLocation,
            String photoAddress, String memo, String refusal) {
        super(RELATIVE_URL);
        mOrderId = orderId;
        mType = type;
        mPhotoUrl = photoUrl;
        mMemo = memo;
        mRefusal = refusal;
        mPhotoLocation = photoLocation;
        mPhotoAddress = photoAddress;
    }

    @Override
    protected String getPostString() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", mOrderId);
            json.put("type", mType);
            json.put("photo", mPhotoUrl);
            json.put("photolocation", mPhotoLocation);
            json.put("photoaddress", mPhotoAddress);
            json.put("memo", mMemo);
            json.put("refusal", mRefusal);
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
            json.put("type", mType);
            json.put("photo", mPhotoUrl);
            json.put("photolocation", mPhotoLocation);
            json.put("photoaddress", mPhotoAddress);
            json.put("memo", mMemo);
            json.put("refusal", mRefusal);
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
