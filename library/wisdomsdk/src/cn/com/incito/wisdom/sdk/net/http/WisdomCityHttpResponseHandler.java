package cn.com.incito.wisdom.sdk.net.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityRestClient.WisdomCityRestClientParameter;

public class WisdomCityHttpResponseHandler extends JsonHttpResponseHandler {
	protected WisdomCityServerAPI mApi;
	protected APIFinishCallback mCallback;

	public WisdomCityHttpResponseHandler(WisdomCityServerAPI api, APIFinishCallback callback) {
		mApi = api;
		mCallback = callback;
		mApi.setResponseHandler(this);
	}

	@Override
	public void onFailure(Throwable e, String content) {
		super.onFailure(e, content);
		if (WisdomCityRestClient.sEnableLogging) {
			Log.e(WisdomCityRestClient.RestClientLog, " onFailure(Throwable e, String content)    content = " + content);
		}
		if (mApi == null) {
			return;
		}
		BasicResponse resp = null;
		if (e instanceof JSONException) {
			resp = new BasicResponse(BasicResponse.JSON_EXCEPTION, WisdomCityRestClient.getRestClientParameter()
					.getErrorMessage(WisdomCityRestClientParameter.CLIENT_ERROR_JSON_EXCEPTION));
			if (mCallback != null) {
				mCallback.OnRemoteApiFinish(resp);
			}
		} else {
			resp = new BasicResponse(BasicResponse.NETWORK_EXCEPTION, mApi.requestFailed(e, content));
			if (mCallback != null) {
				mCallback.OnRemoteApiFinish(resp);
			}
		}
		if (WisdomCityRestClient.sEnableLogging) {
			Log.e(WisdomCityRestClient.RestClientLog, "Fail: status = " + resp.status + " message = " + resp.msg);
		}
		mApi = null;
	}

	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		super.onFailure(e, errorResponse);
		if (WisdomCityRestClient.sEnableLogging) {
			Log.e(WisdomCityRestClient.RestClientLog,
					" onFailure(Throwable e, JSONObject errorResponse)  errorResponse == " + errorResponse.toString());
		}
		if (mApi == null) {
			return;
		}
		BasicResponse resp = null;
		if (e instanceof JSONException) {
			resp = new BasicResponse(BasicResponse.JSON_EXCEPTION, WisdomCityRestClient.getRestClientParameter()
					.getErrorMessage(WisdomCityRestClientParameter.CLIENT_ERROR_JSON_EXCEPTION));
			if (mCallback != null) {
				mCallback.OnRemoteApiFinish(resp);
			}
		} else {
			resp = new BasicResponse(BasicResponse.NETWORK_EXCEPTION, mApi.requestFailed(e, errorResponse));
			if (mCallback != null) {
				mCallback.OnRemoteApiFinish(resp);
			}
		}
		if (WisdomCityRestClient.sEnableLogging) {
			Log.e(WisdomCityRestClient.RestClientLog, "Fail: status = " + resp.status + " message = " + resp.msg);
		}
		mApi = null;
	}

	@Override
	public void onSuccess(int statusCode, JSONObject response) {
		super.onSuccess(statusCode, response);
		if (WisdomCityRestClient.sEnableLogging) {
			Log.e(WisdomCityRestClient.RestClientLog, "onSuccess(int statusCode, JSONObject response)  response == "
					+ response.toString());
		}
		BasicResponse resp = null;
		if (response == null) {
			resp = new BasicResponse(BasicResponse.TIME_OUT, WisdomCityRestClient.getRestClientParameter()
					.getErrorMessage(WisdomCityRestClientParameter.CLIENT_ERROR_NETWORK_TIMEOUT));
			if (mCallback != null) {
				mCallback.OnRemoteApiFinish(resp);
			}
			if (WisdomCityRestClient.sEnableLogging) {
				Log.e(WisdomCityRestClient.RestClientLog, "Fail: status = " + resp.status + " message = " + resp.msg);
			}
		} else {
			if (mApi == null) {
				return;
			}
			resp = mApi.parseResponseBase(response);
			if (resp == null) {
				resp = new BasicResponse(BasicResponse.JSON_EXCEPTION, WisdomCityRestClient
						.getRestClientParameter().getErrorMessage(
								WisdomCityRestClientParameter.CLIENT_ERROR_JSON_EXCEPTION));
				if (WisdomCityRestClient.sEnableLogging) {
					Log.e(WisdomCityRestClient.RestClientLog, "Fail: status = " + resp.status + " message = "
							+ resp.msg);
				}
			} else {
				if (resp.status != BasicResponse.SUCCESS) {
					if (resp.status == BasicResponse.TOKEN_EXPIRED) {
						if (WisdomCityRestClient.getRestClientTokenExpiredHandler() != null) {
							WisdomCityRestClient.getRestClientTokenExpiredHandler().onTokenExpired();
						}
					}
				}
				if (WisdomCityRestClient.sEnableLogging) {
					Log.i(WisdomCityRestClient.RestClientLog, "Success: status = " + resp.status + " message = "
							+ resp.msg);
				}
			}
			if (mCallback != null) {
				mCallback.OnRemoteApiFinish(resp);
			}
		}
		mApi = null;
	}
}
