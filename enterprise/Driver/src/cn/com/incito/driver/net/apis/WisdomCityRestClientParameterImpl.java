package cn.com.incito.driver.net.apis;

import android.content.res.Resources;
import cn.com.incito.driver.Constants;
import cn.com.incito.driver.R;
import cn.com.incito.driver.DriverApplication;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient.WisdomCityRestClientParameter;
import cn.com.incito.wisdom.sdk.openudid.OpenUDIDManager;
import cn.com.incito.wisdom.sdk.utils.PackageUtils;

public class WisdomCityRestClientParameterImpl implements
		WisdomCityRestClientParameter {
	/***
	 * 正式域名
	 */
	// private static final String BASE_URL = "http://"
	// + Constants.API_SERVER_NAME + ":10080";
	private static final String BASE_URL = "http://"
			+ Constants.API_SERVER_NAME + ":30001";

	// private static final String BASE_URL = "http://"
	// + Constants.API_SERVER_NAME + ":8080";
	private static final String TEST_URL = "http://" + Constants.TEST_SERVER_IP
			+ ":8080";
	// private static final String TEST_URL = "http://" +
	// Constants.TEST_SERVER_IP
	// + ":8081";

	// private static final String TEST_URL = "http://" +
	// Constants.TEST_SERVER_IP
	// + ":8082";// enterprise
	private Resources mRes;
	private String mClientVersion;
	private String mUDID;
	private String mFilterid;
	private String mFiltertype;

	public WisdomCityRestClientParameterImpl(Resources r) {
		mRes = r;
		mClientVersion = String.valueOf(PackageUtils
				.getVersionCode(DriverApplication.getInstance()
						.getApplicationContext()));
	}

	@Override
	public String getFilterid() {
		// TODO Auto-generated method stub
		if (mFilterid == null) {
			return mFilterid = DriverApplication.getInstance().getAuthUserId();
		} else if (mFilterid != null) {
			return mFilterid;
		} else {
			return DriverApplication.getInstance().getAuthUserId();
		}
	}

	@Override
	public String getFiltertype() {
		if (mFiltertype == null) {
			return mFiltertype = DriverApplication.getInstance()
					.getAuthUserType();
		} else if (mFiltertype != null) {
			return mFiltertype;
		} else {
			return DriverApplication.getInstance().getAuthUserType();
		}
	}

	@Override
	public String getServerUrl() {
		if (!Constants.RELEASE_SERVER) {
			return TEST_URL;
		} else {
			return BASE_URL;
		}
	}

	public static String getUrl() {
		if (!Constants.RELEASE_SERVER) {
			return TEST_URL;
		} else {
			return BASE_URL;
		}
	}

	@Override
	public String getUDID() {
		if (mUDID == null && OpenUDIDManager.isInitialized()) {
			return mUDID = DriverApplication.getInstance().getUDID();
		} else if (mUDID != null) {
			return mUDID;
		} else {
			return DriverApplication.getInstance().getUDID();
		}
	}

	@Override
	public String getClientVersion() {
		return mClientVersion;
	}

	@Override
	public String getErrorMessage(int errorType) {
		switch (errorType) {
		case WisdomCityRestClientParameter.CLIENT_ERROR_NETWORK_UNAVAILABLE:
			return mRes.getString(R.string.errcode_network_unavailable);
		case WisdomCityRestClientParameterImpl.CLIENT_ERROR_NETWORK_TIMEOUT:
			return mRes.getString(R.string.errcode_network_response_timeout);
		case WisdomCityRestClientParameterImpl.CLIENT_ERROR_JSON_EXCEPTION:
			return mRes.getString(R.string.errcode_network_json_exception);
		case WisdomCityRestClientParameterImpl.CLIENT_ERROR_RESPONSE_NULL:
			return mRes.getString(R.string.errcode_network_response_no_data);
		case WisdomCityRestClientParameterImpl.CLIENT_ERROR_COOKIE_REQUIRED:
			return mRes.getString(R.string.errcode_network_cookie_required);
		default:
			return Constants.EMPTY_STR;
		}
	}

	@Override
	public boolean isClientRelease() {
		return Constants.RELEASE_CLIENT;
	}

	@Override
	public int getServerAPIVersion() {
		return Constants.API_VERSION;
	}

}
