
package cn.com.incito.driver.net.apis;

import cn.com.incito.driver.models.StarLevelModel;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

/**
 * 获取货代/司机等级API
 * 
 * @description 获取货代/司机等级API
 * @author zhangyushuang
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class CreditLevelAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/comment/getCreditLevel";

    private final String mId;

    public CreditLevelAPI(Context context, String mId) {
        super(RELATIVE_URL);
        this.mId = mId;
        mContext = context;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("agentid", mId);
        return ret;
    }

    @Override
    public CreditLevelAPIResponse parseResponse(JSONObject json) {
        try {
            return new CreditLevelAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class CreditLevelAPIResponse extends BasicResponse {
        public final StarLevelModel starlevel;

        public CreditLevelAPIResponse(JSONObject json) throws JSONException {
            super(json);
            JSONArray jsonarray = json.getJSONArray("items");
            starlevel = new StarLevelModel();
            if (jsonarray.length() > 0) {
                starlevel.setDesc(jsonarray.getJSONObject(0).getString("level"));
                starlevel.setQuality(jsonarray.getJSONObject(1).getString("level"));
            } else {
                starlevel.setDesc("0");
                starlevel.setQuality("0");
            }
        }
    }

}
