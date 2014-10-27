
package cn.com.incito.driver.net.apis;

import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询热点城市
 * 
 * @description 查询热点城市
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class SearchHotCityAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/seach/hostSeachCity";

    private final String mCarid;

    public SearchHotCityAPI(String mCarid) {
        super(RELATIVE_URL);
        this.mCarid = mCarid;

    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new SearchHotCityAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("carid", mCarid);
        return ret;
    }

    public class SearchHotCityAPIResponse extends BasicResponse {
        public final JSONArray dataArray;

        public SearchHotCityAPIResponse(JSONObject json) throws JSONException {
            super(json);
            dataArray = json.getJSONArray("items");

        }
    }

}
