package cn.com.incito.driver.net.apis;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 用户登录
 * 
 * @author qiujiaheng
 * 
 */
public class LoginAPI extends WisdomCityServerAPI {

	private static final String RELATIVE_URL = "/logistics/car/login";
	private final String mLoginStr;

	public LoginAPI(String loginStr) {
		super(RELATIVE_URL);
		mLoginStr = loginStr;
	}

	@Override
	protected String getPostString() {
		return mLoginStr;
	}

	@Override
	public RequestParams getRequestParams() {
		RequestParams ret = super.getRequestParams();
		ret.put("json", mLoginStr);
		return ret;
	}

	@Override
	public UserAPILoginResponse parseResponse(JSONObject json) {
		try {
			return new UserAPILoginResponse(json);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public class UserAPILoginResponse extends BasicResponse {
		public String mLoginMsg;

		public UserAPILoginResponse(JSONObject json) throws JSONException {
			super(json);
			mLoginMsg = json.toString();
		}
	}

	@Override
	public int isCookiedRequired() {
		return WisdomCityServerAPI.COOKIE_FORBIDDEN;
	}

	@Override
	public int getHttpRequestType() {
		return WisdomCityServerAPI.HTTP_REQUEST_TYPE_POST;
	}
}
