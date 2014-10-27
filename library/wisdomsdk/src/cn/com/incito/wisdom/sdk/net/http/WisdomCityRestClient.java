package cn.com.incito.wisdom.sdk.net.http;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import cn.com.incito.wisdom.sdk.utils.NetworkUtils;

public class WisdomCityRestClient {
	protected static String RestClientLog = "NET_LOG";
	private static final String COOKIE_UID_NAME = "UID";
	private static final String COOKIE_SESSIONID_NAME = "SESSIONID";
	private static AsyncHttpClient sClient = new AsyncHttpClient();
	protected static AsyncHttpClient sSyncClient = new SimpleSyncHttpClient();

	protected static WisdomCityRestClientParameter sWisdomCityRestClientParameter = null;
	protected static WisdomCityRestClientTokenExcpriedHandler sWisdomCityRestClientTokenExcpriedHandler = null;
	protected static boolean sEnableLogging = false;

	public interface WisdomCityRestClientParameter {
		public static final int CLIENT_ERROR_NETWORK_UNAVAILABLE = 0;
		public static final int CLIENT_ERROR_NETWORK_TIMEOUT = 1;
		public static final int CLIENT_ERROR_JSON_EXCEPTION = 2;
		public static final int CLIENT_ERROR_RESPONSE_NULL = 3;
		public static final int CLIENT_ERROR_COOKIE_REQUIRED = 4;

		public String getFilterid();//

		public String getFiltertype();//

		public String getServerUrl();

		public String getUDID();

		public String getClientVersion();

		public String getErrorMessage(int errorType);

		public boolean isClientRelease();

		public int getServerAPIVersion();
	}

	public interface WisdomCityRestClientTokenExcpriedHandler {
		public void onTokenExpired();
	}

	public static WisdomCityRestClientTokenExcpriedHandler getRestClientTokenExpiredHandler() {
		return sWisdomCityRestClientTokenExcpriedHandler;
	}

	public static WisdomCityRestClientParameter getRestClientParameter() {
		return sWisdomCityRestClientParameter;
	}

	/**
	 * 初始化
	 * 
	 * @param appCtx
	 * @param param
	 * @param handler
	 * @param enableLogging
	 */
	public static void init(Context appCtx,
			WisdomCityRestClientParameter param,
			WisdomCityRestClientTokenExcpriedHandler handler,
			boolean enableLogging) {
		PersistentCookieStore myCookieStore = new PersistentCookieStore(appCtx);
		sClient.setCookieStore(myCookieStore);
		sSyncClient.setCookieStore(myCookieStore);
		if (param == null) {
			throw new IllegalArgumentException();
		}
		sWisdomCityRestClientParameter = param;
		sWisdomCityRestClientTokenExcpriedHandler = handler;
		sEnableLogging = enableLogging;
	}

	/**
	 * 
	 * @param ctx
	 * @param userName
	 * @param password
	 */
	public static void setProxy(Context ctx, String userName, String password) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable()
				&& ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = NetworkUtils.getProxyHost();
			int port = NetworkUtils.getProxyPort();
			if (proxyHost != null) {
				AuthScope authScope = new AuthScope(proxyHost, port);
				sClient.setBasicAuth(userName, password, authScope);
				sSyncClient.setBasicAuth(userName, password, authScope);
			}
		}
	}

	/**
	 * 清除cookie
	 */
	public static void clearCookie() {
		PersistentCookieStore myCookieStore = (PersistentCookieStore) sClient
				.getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
		List<Cookie> cookieList = myCookieStore.getCookies();
		myCookieStore.clear();
		for (int i = 0; i < cookieList.size(); i++) {
			Cookie cookie = cookieList.get(i);
			if (COOKIE_UID_NAME.equals(cookie.getName())) {
				continue;
			} else {
				myCookieStore.addCookie(cookie);
			}
		}
		sSyncClient.setCookieStore(myCookieStore);
	}

	/**
	 * 获取cookie
	 * 
	 * @param name
	 * @return
	 */
	private static Cookie getCookie(String name) {
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException();
		}
		PersistentCookieStore myCookieStore = (PersistentCookieStore) sClient
				.getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
		List<Cookie> cookieList = myCookieStore.getCookies();
		for (int i = 0; i < cookieList.size(); i++) {
			Cookie c = cookieList.get(i);
			if (name.equals(c.getName())) {
				return c;
			}
		}
		return null;
	}

	public static final int getUid() {
		Cookie c = getCookie(COOKIE_UID_NAME);
		if (c != null) {
			return Integer.valueOf(c.getValue());
		} else {
			return 0;
		}
	}

	public static final Cookie getSessionCookie() {
		return getCookie(COOKIE_SESSIONID_NAME);
	}

	public static final Cookie getUidCookie() {
		return getCookie(COOKIE_UID_NAME);
	}

	public static final String getSessionId() {
		Cookie c = getCookie(COOKIE_SESSIONID_NAME);
		if (c != null) {
			return c.getValue();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param api
	 */
	public static void execute(WisdomCityServerAPI api) {
		executeImpl(api, sClient);
	}

	/**
	 * httprequest execute
	 * 
	 * @param api
	 * @param client
	 */
	protected static void executeImpl(WisdomCityServerAPI api,
			AsyncHttpClient client) {
		if (api.isCookiedRequired() == WisdomCityServerAPI.COOKIE_FORBIDDEN) {
			clearCookie();
		} else if (api.isCookiedRequired() == WisdomCityServerAPI.COOKIE_REQUIRED) {
			if (getUid() == 0 || TextUtils.isEmpty(getSessionId())) {
				api.mResponseHandler
						.onFailure(
								null,
								WisdomCityRestClient
										.getRestClientParameter()
										.getErrorMessage(
												WisdomCityRestClientParameter.CLIENT_ERROR_COOKIE_REQUIRED));
				return;
			}
		}
		RequestParams params = api.getRequestParams();
		switch (api.getHttpRequestType()) {
		case WisdomCityServerAPI.HTTP_REQUEST_TYPE_GET:// get
			params.put("filterid", sWisdomCityRestClientParameter.getFilterid());
			params.put("filtertype",
					sWisdomCityRestClientParameter.getFiltertype());
			params.put("imei", sWisdomCityRestClientParameter.getUDID());
			params.put("clientversion",
					sWisdomCityRestClientParameter.getClientVersion());
			params.put("version", String.valueOf(sWisdomCityRestClientParameter
					.getServerAPIVersion()));
			client.get(
					sWisdomCityRestClientParameter.getServerUrl()
							+ api.getUrl(), params, api.getResponseHandler());
			if (sEnableLogging) {
				Log.d(RestClientLog, String.format("%s%s?%s",
						sWisdomCityRestClientParameter.getServerUrl(),
						api.getUrl(), params.toString()));
			}
			break;
		case WisdomCityServerAPI.HTTP_REQUEST_TYPE_POST:// POST
			params.put("version", String.valueOf(sWisdomCityRestClientParameter
					.getServerAPIVersion()));
			params.put("filterid", sWisdomCityRestClientParameter.getFilterid());
			params.put("filtertype",
					sWisdomCityRestClientParameter.getFiltertype());
			HttpEntity entity;
			try {
				entity = new StringEntity(api.getPostString(), HTTP.UTF_8);
				client.post(
						api.getContext(),
						sWisdomCityRestClientParameter.getServerUrl()
								+ api.getUrl()
								+ "?imei="
								+ sWisdomCityRestClientParameter.getUDID()
								+ "&filterid="
								+ sWisdomCityRestClientParameter.getFilterid()
								+ "&filtertype="
								+ sWisdomCityRestClientParameter
										.getFiltertype()
								+ "&clientversion="
								+ sWisdomCityRestClientParameter
										.getClientVersion(), entity,
						"application/json", api.getResponseHandler());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (sEnableLogging) {
				Log.d(RestClientLog, String.format(
						"%s%s%s%s&%s",
						sWisdomCityRestClientParameter.getServerUrl(),
						api.getUrl(),
						"?imei=" + sWisdomCityRestClientParameter.getUDID(),
						"&clientversion="
								+ sWisdomCityRestClientParameter
										.getClientVersion(), params.toString()));
			}
			break;
		case WisdomCityServerAPI.HTTP_REQUEST_TYPE_POST_IMAGE:// POST image
			params.put("version", String.valueOf(sWisdomCityRestClientParameter
					.getServerAPIVersion()));
			client.post(
					sWisdomCityRestClientParameter.getServerUrl()
							+ api.getUrl() + "?imei="
							+ sWisdomCityRestClientParameter.getUDID()
							+ "&clientversion="
							+ sWisdomCityRestClientParameter.getClientVersion(),
					params, api.getResponseHandler());
			if (sEnableLogging) {
				Log.d(RestClientLog, String.format(
						"%s%s%s%s&%s",
						sWisdomCityRestClientParameter.getServerUrl(),
						api.getUrl(),
						"?imei=" + sWisdomCityRestClientParameter.getUDID(),
						"&clientversion="
								+ sWisdomCityRestClientParameter
										.getClientVersion(), params.toString()));
			}
			break;
		case WisdomCityServerAPI.HTTP_REQUEST_TYPE_PUT:
			client.put(
					sWisdomCityRestClientParameter.getServerUrl()
							+ api.getUrl(), params, api.getResponseHandler());
			if (sEnableLogging) {
				Log.d(RestClientLog, String.format("%s%s?%s",
						sWisdomCityRestClientParameter.getServerUrl(),
						api.getUrl(), params.toString()));
			}
			break;
		case WisdomCityServerAPI.HTTP_REQUEST_TYPE_DELETE:
			client.delete(
					sWisdomCityRestClientParameter.getServerUrl()
							+ api.getUrl(), api.getResponseHandler());
			if (sEnableLogging) {
				Log.d(RestClientLog, String.format("%s%s",
						sWisdomCityRestClientParameter.getServerUrl(),
						api.getUrl()));
			}
			break;
		}
	}
}
