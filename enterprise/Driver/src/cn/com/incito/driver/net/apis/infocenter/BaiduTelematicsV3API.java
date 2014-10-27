
package cn.com.incito.driver.net.apis.infocenter;

import cn.com.incito.wisdom.sdk.net.http.AsyncHttpClient;
import cn.com.incito.wisdom.sdk.net.http.AsyncHttpResponseHandler;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;

import android.util.Log;

/**
 * 百度车联网 API>天气查询
 * 
 * @description 百度车联网 API>天气查询
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class BaiduTelematicsV3API {

    private static final String BASE_URL = "http://api.map.baidu.com/telematics/v3/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("BaiduTelematicsV3API  get ", getAbsoluteUrl(url) + params.toString());
        Log.d("NET_LOG", getAbsoluteUrl(url) + params.toString());
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Log.d("BaiduTelematicsV3API  post ", getAbsoluteUrl(url) + params.toString());
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
