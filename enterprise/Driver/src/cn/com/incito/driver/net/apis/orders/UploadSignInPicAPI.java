
package cn.com.incito.driver.net.apis.orders;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;
import cn.com.incito.wisdom.sdk.utils.BitmapUtils;

/**
 * 签收、拒签上传图片接口
 * 
 * @description 签收、拒签上传图片接口
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class UploadSignInPicAPI extends WisdomCityServerAPI {
    public static final String RELATIVE_URL = "/logistics/transport/orderUploadImg";

    public final Bitmap mPic;

    public UploadSignInPicAPI(Bitmap pic) {
        super(RELATIVE_URL);
        mPic = pic;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("image", BitmapUtils.BitmapToInputStream(mPic));
        return ret;
    }

    @Override
    public UploadSignInPicAPIResponse parseResponse(JSONObject json) {
        try {
            return new UploadSignInPicAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class UploadSignInPicAPIResponse extends BasicResponse {
        public final String mPhoto;

        public UploadSignInPicAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mPhoto = json.getString("photo");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append("picname = " + mPhoto);
            return sb.toString();
        }
    }

    @Override
    public int getHttpRequestType() {
        return WisdomCityServerAPI.HTTP_REQUEST_TYPE_POST_IMAGE;
    }
}
