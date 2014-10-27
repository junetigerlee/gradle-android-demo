
package cn.com.incito.driver.net.apis.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incito.driver.dao.GoodsAvailable;
import cn.com.incito.driver.dao.OrdersAll;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.ModelOrderListItem;
import cn.com.incito.driver.net.response.MyOrdersResponseParserNew;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

/**
 * 可抢货源列表API
 * 
 * @description 可抢货源列表API
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsAvailableAPI extends WisdomCityServerAPI {
    private static final String RELATIVE_URL = "/logistics/grabgoods/carGrabgoods";

    public GoodsAvailableAPI(Map<String, Object> params) {
        super(RELATIVE_URL, params, new String[] {
                ListAbstractModel.VCOLUMN_CAR_ID, ListAbstractModel.VCOLUMN_START, ListAbstractModel.VCOLUMN_PAGE_SIZE
        });
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new GoodsAvailableAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class GoodsAvailableAPIResponse extends BasicResponse {

        public final List<GoodsAvailable> mList;

        public GoodsAvailableAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mList = new ArrayList<GoodsAvailable>();

            GoodsAvailable goodsAvailable = null;
            String str = null;
            JSONArray dataArray = json.getJSONArray("items");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObj = dataArray.getJSONObject(i);
                goodsAvailable = new GoodsAvailable();

                str = dataObj.isNull("id") ? "" : dataObj.getString("id");// 推送ID
                goodsAvailable.setMId(str);
                str = dataObj.isNull("originalprovince") ? "" : dataObj.getString("originalprovince");// 发货地省
                goodsAvailable.setOriginalprovince(str);
                str = dataObj.isNull("originalcity") ? "" : dataObj.getString("originalcity");// 发货地城市
                goodsAvailable.setOriginalcity(str);
                str = dataObj.isNull("originalregion") ? "" : dataObj.getString("originalregion");// 发货地区/县
                goodsAvailable.setOriginalregion(str);
                str = dataObj.isNull("receiptprovince") ? "" : dataObj.getString("receiptprovince");// 收货地省
                goodsAvailable.setReceiptprovince(str);
                str = dataObj.isNull("receiptcity") ? "" : dataObj.getString("receiptcity");// 收货地城市
                goodsAvailable.setReceiptcity(str);
                str = dataObj.isNull("receiptregion") ? "" : dataObj.getString("receiptregion");// 收货地区/县
                goodsAvailable.setReceiptregion(str);
                str = dataObj.isNull("goodsname") ? "" : dataObj.getString("goodsname");// 货物名称
                goodsAvailable.setGoodsname(str);
                str = dataObj.isNull("goodstype") ? "" : dataObj.getString("goodstype");// 货物类型
                goodsAvailable.setGoodstype(str);
                str = dataObj.isNull("fare") ? "" : dataObj.getString("fare");// 运费
                goodsAvailable.setFare(str);
                str = dataObj.isNull("infofare") ? "" : dataObj.getString("infofare");// 信息费
                goodsAvailable.setInfofare(str);
                str = dataObj.isNull("weight") ? "" : dataObj.getString("weight");// 重量
                goodsAvailable.setWeight(str);
                str = dataObj.isNull("volume") ? "" : dataObj.getString("volume");// 体积
                goodsAvailable.setVolume(str);
                str = dataObj.isNull("count") ? "" : dataObj.getString("count");// 货源建设
                goodsAvailable.setCount(str);
                str = dataObj.isNull("carlength") ? "" : dataObj.getString("carlength");// 车长
                goodsAvailable.setCarlength(str);
                str = dataObj.isNull("generatetime") ? "" : dataObj.getString("generatetime");// 发布时间
                goodsAvailable.setGeneratetime(str);
                str = dataObj.isNull("goodstime") ? "" : dataObj.getString("goodstime");//
                goodsAvailable.setGoodstime(str);
                str = dataObj.isNull("completetime") ? "" : dataObj.getString("completetime");// 抢单时间
                goodsAvailable.setCompletetime(str);
                str = dataObj.isNull("goodsid") ? "" : dataObj.getString("goodsid");// 货源ID
                goodsAvailable.setGoodsid(str);
                str = dataObj.isNull("carid") ? "" : dataObj.getString("carid");// 车辆ID
                goodsAvailable.setCarid(str);
                str = dataObj.isNull("grabflag") ? "" : dataObj.getString("grabflag");// 司机是否已抢单，默认0是未抢单，1是已抢单
                goodsAvailable.setGrabflag(str);
                str = dataObj.isNull("grabresult") ? "" : dataObj.getString("grabresult");// 1审批中 2抢单成功 3抢单失败
                goodsAvailable.setGrabresult(str);
                str = dataObj.isNull("orderid") ? "" : dataObj.getString("orderid");// 抢单成功后对应的订单ID
                goodsAvailable.setOrderid(str);
                str = dataObj.isNull("failurereason") ? "" : dataObj.getString("failurereason");// 失败原因
                goodsAvailable.setFailurereason(str);

                goodsAvailable.setCreatetime(System.currentTimeMillis());

                mList.add(goodsAvailable);
            }
        }

    }

}
