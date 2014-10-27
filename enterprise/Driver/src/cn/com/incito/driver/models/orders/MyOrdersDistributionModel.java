package cn.com.incito.driver.models.orders;

import cn.com.incito.driver.dao.OrdersDistribution;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.ModelResponse;
import cn.com.incito.driver.net.apis.orders.MyDistributionOrdersAPI;

import de.greenrobot.dao.AbstractDaoMaster;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import java.util.List;
import java.util.Map;

/**
 * 我的全部订单页面的model
 * @description 我的全部订单页面的model
 * @author   qiujiaheng
 * @createDate 2014年10月16日
 * @version  1.0
 */
public class MyOrdersDistributionModel
        extends
        ListAbstractModel<OrdersDistribution, MyDistributionOrdersAPI, MyDistributionOrdersAPI.MyDistributionOrdersAPIResponse> {
    public static final String sDefaultUrl = "myOrdersDistribution";

    public static final String COLUMN_ORDERS_NO = "orderno";
    public static final String COLUMN_ORDERS_ORIGINALPROVINCE = "originalprovince";
    public static final String COLUMN_ORDERS_ORIGINALCITY = "originalcity";
    public static final String COLUMN_ORDERS_ORIGINALREGION = "originalregion";
    public static final String COLUMN_ORDERS_RECEIPTPROVINCE = "receiptprovince";
    public static final String COLUMN_ORDERS_RECEIPTCITY = "receiptcity";
    public static final String COLUMN_ORDERS_RECEIPTREGION = "receiptregion";

    public static final String COLUMN_ORDERS_ORDERNO = "orderno";
    public static final String COLUMN_ORDERS_GENERATETIME = "generatetime";
    public static final String COLUMN_ORDERS_STATUS = "status";
    public static final String COLUMN_ORDERS_INFOFARE = "infofare";
    public static final String COLUMN_ORDERS_DECLAREDVALUE = "declaredvalue";
    public static final String COLUMN_ORDERS_FARE = "fare";
    public static final String COLUMN_ORDERS_WEIGHT = "weight";
    public static final String COLUMN_ORDERS_GOODSTYPE = "goodstype";
    public static final String COLUMN_ORDERS_VOLUME = "volume";
    public static final String COLUMN_ORDERS_GOODSNO = "goodsno";
    public static final String COLUMN_ORDERS_GOODSNAME = "goodsname";

    public static final String COLUMN_ORDERS_COUNT = "count";
    public static final String COLUMN_ORDERS_DAY = "day";
    public static final String COLUMN_ORDERS_MEMO = "memo";
    public static final String COLUMN_ORDERS_COLLECTGS = "collectgs";
    public static final String COLUMN_ORDERS_AGENTNO = "agentno";
    public static final String COLUMN_ORDERS_COMPANY = "company";
    public static final String COLUMN_ORDERS_ADDRESS = "address";
    public static final String COLUMN_ORDERS_NAME = "name";
    public static final String COLUMN_ORDERS_TEL = "tel";
    public static final String COLUMN_ORDERS_PHOTO = "photo";
    public static final String COLUMN_ORDERS_STARIMG = "starImg";
    public static final String COLUMN_ORDERS_SID = "sid";
    public static final String COLUMN_ORDERS_SNAME = "sname";
    public static final String COLUMN_ORDERS_STEL = "stel";
    public static final String COLUMN_ORDERS_SCOMPANY = "scompany";
    public static final String COLUMN_ORDERS_SADDRESS = "saddress";
    public static final String COLUMN_ORDERS_RID = "rid";
    public static final String COLUMN_ORDERS_RNAME = "rname";
    public static final String COLUMN_ORDERS_RTEL = "rtel";
    public static final String COLUMN_ORDERS_RCOMPANY = "rcompany";
    public static final String COLUMN_ORDERS_RADDRESS = "raddress";

    public static final long sDefaultCacheExpiredTime = DateUtils.MINUTE_IN_MILLIS * 3;

    public class Response extends ModelResponse {
        public Response() {
            super();
        }
    }

    public MyOrdersDistributionModel(Context context, SQLiteDatabase db,
            AbstractDaoMaster daoMaster) {
        super(context, db, daoMaster);
    }

    @Override
    protected Class<OrdersDistribution> getDAOModelClassName() {
        return OrdersDistribution.class;
    }

    @Override
    protected MyDistributionOrdersAPI newAPIInstance(Map<String, Object> params) {
        return new MyDistributionOrdersAPI(params);
    }

    @Override
    protected List<OrdersDistribution> getAPIResponse(
            MyDistributionOrdersAPI.MyDistributionOrdersAPIResponse r) {
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
