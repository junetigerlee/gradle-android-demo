
package cn.com.incito.driver.net.apis;

import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 位置上传
 * 
 * @description 位置上传
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class LocationUploadAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/location/updateCarlocation";

    private final String mLocationStr;

    public LocationUploadAPI(String locationStr) {
        super(RELATIVE_URL);
        mLocationStr = locationStr;
    }

    @Override
    protected String getPostString() {
        return mLocationStr;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("json", mLocationStr);
        return ret;
    }

    @Override
    public int getHttpRequestType() {
        return WisdomCityServerAPI.HTTP_REQUEST_TYPE_POST;
    }
}
