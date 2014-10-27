package cn.com.incito.wisdom.sdk.net.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * �����Ӧͷ����
 * 
 * @author qiujiaheng
 * 
 */
public class BasicResponse {
	public static final int NETWORK_EXCEPTION = -3;
	public static final int JSON_EXCEPTION = -2;
	public static final int TIME_OUT = -1;
	public static final int SUCCESS = 0;
	public static final int TOKEN_EXPIRED = 17;

	public final int status;
	public final String msg;

	public BasicResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public BasicResponse(JSONObject json) throws JSONException {
		status = json.getInt("status");
		msg = json.getString("msg");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("status = " + status).append(" ");
		sb.append("msg = " + msg).append(" ");
		return sb.toString();
	}

	public interface APIFinishCallback {
		public void OnRemoteApiFinish(BasicResponse response);
	}
}
