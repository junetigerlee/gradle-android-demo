package cn.com.incito.wisdom.sdk.net.http;

import android.os.Message;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;

public class WisdomCitySyncHttpResponseHandler extends WisdomCityHttpResponseHandler {

	public WisdomCitySyncHttpResponseHandler(WisdomCityServerAPI api, APIFinishCallback callback) {
		super(api, callback);
	}

	@Override
	protected void sendMessage(Message msg) {
		/*
		 * Dont use the handler and send it directly to the analysis (because
		 * its all the same thread)
		 */
		handleMessage(msg);
	}
}
