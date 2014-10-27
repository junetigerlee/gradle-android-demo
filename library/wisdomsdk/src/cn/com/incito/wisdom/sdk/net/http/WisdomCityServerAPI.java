package cn.com.incito.wisdom.sdk.net.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient.WisdomCityRestClientParameter;

/**
 * 所有api类的父类
 * 
 * @author qiujiaheng
 * 
 */
public class WisdomCityServerAPI {
    public static final int API_VERSION = 1;
    public static final int COOKIE_REQUIRED = 2;
    public static final int COOKIE_PREFERED = 1;
    public static final int COOKIE_FORBIDDEN = 0;
    public static final int HTTP_REQUEST_TYPE_GET = 1;
    public static final int HTTP_REQUEST_TYPE_POST = 2;
    public static final int HTTP_REQUEST_TYPE_PUT = 3;
    public static final int HTTP_REQUEST_TYPE_DELETE = 4;
    public static final int HTTP_REQUEST_TYPE_POST_IMAGE = 5;

    protected final String mRelativeURL;
    protected JsonHttpResponseHandler mResponseHandler;
    protected final Map<String, Object> mRequestParams;
    protected final String[] mRequiredKeys;
    protected Context mContext;

    public WisdomCityServerAPI(String url) {
        this(url, null, null);
    }

    public WisdomCityServerAPI(String url, Map<String, Object> params,
            String[] requiredKeys) {
        mRelativeURL = url;
        mRequestParams = params;
        mRequiredKeys = requiredKeys;
    }

    /**
     * 设置结果处理类
     * 
     * @param responseHandler
     */
    public void setResponseHandler(JsonHttpResponseHandler responseHandler) {
        mResponseHandler = responseHandler;
    }

    public JsonHttpResponseHandler getResponseHandler() {
        return mResponseHandler;
    }

    /**
     * 获取api的url地址
     * 
     * @return
     */
    public String getUrl() {
        return mRelativeURL;
    }

    public int isCookiedRequired() {
        return COOKIE_PREFERED;
    }

    protected int getHttpRequestType() {
        return HTTP_REQUEST_TYPE_GET;
    }

    protected Context getContext() {
        return mContext;
    }

    protected String getPostString() {
        String str = null;
        return str;
    }

    /**
     * 获取请求参数
     * 
     * @return
     */
    protected RequestParams getRequestParams() {
        RequestParams ret = new RequestParams();
        if (mRequestParams != null && mRequiredKeys != null
                && mRequiredKeys.length > 0) {
            for (String key : mRequiredKeys) {
                if (!mRequestParams.containsKey(key)) {
                    throw new IllegalArgumentException();
                }
            }

            Set<String> keys = mRequestParams.keySet();
            for (String key : keys) {
                Object value = mRequestParams.get(key);
                if (value != null) {
                    if (value instanceof String) {
                        ret.put(key, (String) value);
                    } else if (value instanceof File) {
                        try {
                            ret.put(key, (File) value);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (value instanceof InputStream) {
                        ret.put(key, (InputStream) value);
                    } else if (value instanceof ArrayList<?>) {
                        ret.put(key, (ArrayList<String>) value);
                    } else {
                        ret.put(key, String.valueOf(value));
                    }
                }
            }
        }
        return ret;
    }

    /**
     * 
     * @param json
     * @return
     */
    public BasicResponse parseResponseBase(JSONObject json) {
        try {
            BasicResponse resp = new BasicResponse(json);
            if (resp.status == BasicResponse.SUCCESS) {
                return this.parseResponse(json);
            } else {
                return resp;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param json
     * @return
     * @throws JSONException
     */
    protected BasicResponse parseResponse(JSONObject json) throws JSONException {
        return new BasicResponse(json);
    }

    /**
     * 
     * @param e
     * @param errorResponse
     * @return
     */
    protected String requestFailed(Throwable e, String errorResponse) {
        String errMsg = WisdomCityRestClient
                .getRestClientParameter()
                .getErrorMessage(
                        WisdomCityRestClientParameter.CLIENT_ERROR_NETWORK_TIMEOUT);
        if (e == null) {
            if (!WisdomCityRestClient.getRestClientParameter()
                    .isClientRelease()) {
                errMsg += "(" + errorResponse + ")";
            }
            return errMsg;
        }
        if (!WisdomCityRestClient.getRestClientParameter()
                .isClientRelease()) {
            if (errorResponse != null) {
                errMsg += "(" + e.getMessage() + ", Response = "
                        + errorResponse + ")";
            } else {
                errMsg += "(" + e.getMessage() + ")";
            }
        }
        return errMsg;
    }

    /**
     * 
     * @param e
     * @param errorResponse
     * @return
     */
    protected String requestFailed(Throwable e, JSONObject errorResponse) {
        String errMsg = WisdomCityRestClient
                .getRestClientParameter()
                .getErrorMessage(
                        WisdomCityRestClientParameter.CLIENT_ERROR_NETWORK_TIMEOUT);
        if (e == null) {
            if (!WisdomCityRestClient.getRestClientParameter()
                    .isClientRelease()) {
                errMsg += "(" + errorResponse + ")";
            }
            return errMsg;
        }
        if (!WisdomCityRestClient.getRestClientParameter()
                .isClientRelease()) {
            if (errorResponse != null) {
                errMsg += "(" + e.getMessage() + ", Response = "
                        + errorResponse.toString() + ")";
            } else {
                errMsg += "(" + e.getMessage() + ")";
            }
        }
        return errMsg;
    }

}
