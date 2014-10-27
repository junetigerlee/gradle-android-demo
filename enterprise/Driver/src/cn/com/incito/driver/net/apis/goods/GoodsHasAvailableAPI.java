
package cn.com.incito.driver.net.apis.goods;

import cn.com.incito.driver.dao.GoodsHasAvailable;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 已抢货源列表API
 * 
 * @description 已抢货源列表API
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class GoodsHasAvailableAPI extends WisdomCityServerAPI {
    private static final String RELATIVE_URL = "/logistics/grabgoods/carGrabgoods";

    public GoodsHasAvailableAPI(Map<String, Object> params) {
        super(RELATIVE_URL, params, new String[] {
                ListAbstractModel.VCOLUMN_CAR_ID, ListAbstractModel.VCOLUMN_START, ListAbstractModel.VCOLUMN_PAGE_SIZE
        });
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new GoodsHasAvailableAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class GoodsHasAvailableAPIResponse extends BasicResponse {

        public final List<GoodsHasAvailable> mList;

        public GoodsHasAvailableAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mList = new ArrayList<GoodsHasAvailable>();

            GoodsHasAvailable goodsHasAvailable = null;
            String str = null;
            JSONArray dataArray = json.getJSONArray("items");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObj = dataArray.getJSONObject(i);
                goodsHasAvailable = new GoodsHasAvailable();

                str = dataObj.isNull("id") ? "" : dataObj.getString("id");// 推送ID
                goodsHasAvailable.setMId(str);
                str = dataObj.isNull("originalprovince") ? "" : dataObj.getString("originalprovince");// 发货地省
                goodsHasAvailable.setOriginalprovince(str);
                str = dataObj.isNull("originalcity") ? "" : dataObj.getString("originalcity");// 发货地城市
                goodsHasAvailable.setOriginalcity(str);
                str = dataObj.isNull("originalregion") ? "" : dataObj.getString("originalregion");// 发货地区/县
                goodsHasAvailable.setOriginalregion(str);
                str = dataObj.isNull("receiptprovince") ? "" : dataObj.getString("receiptprovince");// 收货地省
                goodsHasAvailable.setReceiptprovince(str);
                str = dataObj.isNull("receiptcity") ? "" : dataObj.getString("receiptcity");// 收货地城市
                goodsHasAvailable.setReceiptcity(str);
                str = dataObj.isNull("receiptregion") ? "" : dataObj.getString("receiptregion");// 收货地区/县
                goodsHasAvailable.setReceiptregion(str);
                str = dataObj.isNull("goodsname") ? "" : dataObj.getString("goodsname");// 货物名称
                goodsHasAvailable.setGoodsname(str);
                str = dataObj.isNull("goodstype") ? "" : dataObj.getString("goodstype");// 货物类型
                goodsHasAvailable.setGoodstype(str);
                str = dataObj.isNull("fare") ? "" : dataObj.getString("fare");// 运费
                goodsHasAvailable.setFare(str);
                str = dataObj.isNull("infofare") ? "" : dataObj.getString("infofare");// 信息费
                goodsHasAvailable.setInfofare(str);
                str = dataObj.isNull("weight") ? "" : dataObj.getString("weight");// 重量
                goodsHasAvailable.setWeight(str);
                str = dataObj.isNull("volume") ? "" : dataObj.getString("volume");// 体积
                goodsHasAvailable.setVolume(str);
                str = dataObj.isNull("count") ? "" : dataObj.getString("count");// 货源建设
                goodsHasAvailable.setCount(str);
                str = dataObj.isNull("carlength") ? "" : dataObj.getString("carlength");// 车长
                goodsHasAvailable.setCarlength(str);
                str = dataObj.isNull("generatetime") ? "" : dataObj.getString("generatetime");// 发布时间
                goodsHasAvailable.setGeneratetime(str);
                str = dataObj.isNull("goodstime") ? "" : dataObj.getString("goodstime");// 发布时间
                goodsHasAvailable.setGoodstime(str);
                str = dataObj.isNull("completetime") ? "" : dataObj.getString("completetime");// 抢单时间
                goodsHasAvailable.setCompletetime(str);
                str = dataObj.isNull("goodsid") ? "" : dataObj.getString("goodsid");// 货源ID
                goodsHasAvailable.setGoodsid(str);
                str = dataObj.isNull("carid") ? "" : dataObj.getString("carid");// 车辆ID
                goodsHasAvailable.setCarid(str);
                str = dataObj.isNull("grabflag") ? "" : dataObj.getString("grabflag");// 司机是否已抢单，默认0是未抢单，1是已抢单
                goodsHasAvailable.setGrabflag(str);
                str = dataObj.isNull("grabresult") ? "" : dataObj.getString("grabresult");// 1审批中 2抢单成功 3抢单失败
                goodsHasAvailable.setGrabresult(str);
                str = dataObj.isNull("orderid") ? "" : dataObj.getString("orderid");// 抢单成功后对应的订单ID
                goodsHasAvailable.setOrderid(str);
                str = dataObj.isNull("failurereason") ? "" : dataObj.getString("failurereason");// 失败原因
                goodsHasAvailable.setFailurereason(str);

                goodsHasAvailable.setCreatetime(System.currentTimeMillis());

                mList.add(goodsHasAvailable);
            }
        }

    }

}
