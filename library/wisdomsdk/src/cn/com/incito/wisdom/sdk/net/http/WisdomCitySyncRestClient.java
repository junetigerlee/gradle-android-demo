package cn.com.incito.wisdom.sdk.net.http;

import cn.com.incito.wisdom.sdk.net.http.BasicResponse.APIFinishCallback;

public class WisdomCitySyncRestClient extends WisdomCityRestClient {
	/*
	 * as this is a synchronous request this is just a helping mechanism to pass
	 * the result back to this method. Therefore the result object has to be a
	 * field to be accessible
	 */
	protected BasicResponse result;
	protected APIFinishCallback responseHandler = new APIFinishCallback() {
		@Override
		public void OnRemoteApiFinish(BasicResponse response) {
			result = response;
		}
	};

	public BasicResponse executeSync(WisdomCityServerAPI api) {
		api.setResponseHandler(new WisdomCitySyncHttpResponseHandler(api, responseHandler));
		executeImpl(api, sSyncClient);
		return result;
	}
}
