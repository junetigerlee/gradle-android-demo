
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.models.GlobalModel;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 货物拒签API
 * 
 * @description 货物拒签API
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class RefuseSignGoodsAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/transport/refuseSignGoods";

    private final String mGoodsId;

    private final String mTransportId;

    private final String mPhotoUrl;

    public RefuseSignGoodsAPI(String goodsId, String transportId, String photourl) {
        super(RELATIVE_URL);
        mGoodsId = goodsId;
        mTransportId = transportId;
        mPhotoUrl = photourl;
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("carId", GlobalModel.getInst().mLoginModel.getCarId());
        ret.put("goodsId", mGoodsId);
        ret.put("transportId", mTransportId);
        ret.put("photo", mPhotoUrl);
        return ret;
    }

}
