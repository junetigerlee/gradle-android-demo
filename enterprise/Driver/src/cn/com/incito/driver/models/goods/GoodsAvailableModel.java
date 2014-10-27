
package cn.com.incito.driver.models.goods;

import cn.com.incito.driver.dao.GoodsAvailable;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.ModelResponse;
import cn.com.incito.driver.net.apis.goods.GoodsAvailableAPI;
import cn.com.incito.driver.net.apis.goods.GoodsAvailableAPI.GoodsAvailableAPIResponse;

import de.greenrobot.dao.AbstractDaoMaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import java.util.List;
import java.util.Map;

/**
 * 可抢货源
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class GoodsAvailableModel extends
        ListAbstractModel<GoodsAvailable, GoodsAvailableAPI, GoodsAvailableAPI.GoodsAvailableAPIResponse> {
    public static final String sDefaultUrl = "goodsAvailable";

    public static final String COLUMN_GOODS_ORIGINALPROVINCE = "originalprovince";// 发货省

    public static final String COLUMN_GOODS_ORIGINALCITY = "originalcity";// 发货地市

    public static final String COLUMN_GOODS_ORIGINALREGION = "originalregion";// 发货地区县

    public static final String COLUMN_GOODS_RECEIPTPROVINCE = "receiptprovince"; // 收货省

    public static final String COLUMN_GOODS_RECEIPTCITY = "receiptcity";// 收货地城市

    public static final String COLUMN_GOODS_RECEIPTREGION = "receiptregion";// 收货地区县

    public static final String COLUMN_GOODS_NAME = "goodsname";// 货物名称

    public static final String COLUMN_GOODS_TYPE = "goodstype";// 货物类型

    public static final String COLUMN_GOODS_FARE = "fare";// 运价

    public static final String COLUMN_GOODS_INFOFARE = "infofare";// 信息费

    public static final String COLUMN_GOODS_WEIGHT = "weight";// 吨位

    public static final String COLUMN_GOODS_VOLUME = "volume";// 货物体积

    public static final String COLUMN_GOODS_COUNT = "count";// 货物件数

    public static final String COLUMN_GOODS_CARLENGTH = "carlength";// 车长

    public static final String COLUMN_GOODS_GENERATETIME = "generatetime";// 发布时间

    public static final String COLUMN_GOODS_GOODSTIME = "goodstime";// 

    public static final String COLUMN_GOODS_COMPLETETIME = "completetime";// 抢单时间

    public static final String COLUMN_GOODS_GOODSID = "goodsid";// 货源ID

    public static final String COLUMN_GOODS_CARID = "carid";// 车辆ID

    public static final String COLUMN_GOODS_GRABFLAG = "grabflag";// 司机是否已抢单，默认0是未抢单，1是已抢单

    public static final String COLUMN_GOODS_GRABRESULT = "grabresult";// 1审批中
                                                                      // 2抢单成功
                                                                      // 3抢单失败

    public static final String COLUMN_GOODS_ORDERID = "orderid";// 抢单成功后对应的订单ID

    public static final String COLUMN_GOODS_FAILUREREASON = "failurereason";// 失败原因

    public static final String SEARCH_VCOLUMN_TYPE = "type";// 0 是可抢货源，1是已抢货源

    public static final long sDefaultCacheExpiredTime = DateUtils.MINUTE_IN_MILLIS * 3;

    public class Response extends ModelResponse {
        public Response() {
            super();
        }
    }

    public GoodsAvailableModel(Context context, SQLiteDatabase db, AbstractDaoMaster daoMaster) {
        super(context, db, daoMaster);
    }

    @Override
    protected Class<GoodsAvailable> getDAOModelClassName() {
        return GoodsAvailable.class;
    }

    @Override
    protected GoodsAvailableAPI newAPIInstance(Map<String, Object> params) {
        return new GoodsAvailableAPI(params);
    }

    @Override
    protected List<GoodsAvailable> getAPIResponse(GoodsAvailableAPIResponse r) {
        return r.mList;
    }

    @Override
    protected ModelResponse getProviderResponse() {
        return new Response();
    }

    @Override
    protected long getCacheExpiredTime() {
        return sDefaultCacheExpiredTime;
    }

}
