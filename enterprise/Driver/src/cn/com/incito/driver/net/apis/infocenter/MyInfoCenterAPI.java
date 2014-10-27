
package cn.com.incito.driver.net.apis.infocenter;

import cn.com.incito.driver.dao.Agent;
import cn.com.incito.driver.models.ModelCar;
import cn.com.incito.driver.models.ModelNotices;
import cn.com.incito.driver.models.ModelTransport;
import cn.com.incito.driver.models.goods.GoodsTotal;
import cn.com.incito.driver.models.orders.MyOrdersTotal;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.RequestParams;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心
 * 
 * @description
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyInfoCenterAPI extends WisdomCityServerAPI {

    private static final String RELATIVE_URL = "/logistics/driver/center";

    private final String mCarId;

    private final String mLocationCity;

    public MyInfoCenterAPI(String carId, String locationCity) {
        super(RELATIVE_URL);
        mCarId = carId;
        mLocationCity = locationCity;
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new MyInfoCenterAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RequestParams getRequestParams() {
        RequestParams ret = super.getRequestParams();
        ret.put("carid", mCarId);
        ret.put("localcity", mLocationCity);
        return ret;
    }

    public class MyInfoCenterAPIResponse extends BasicResponse {

        public final ModelTransport mModelTransport;

        public final GoodsTotal mGoodsTotal;

        public final MyOrdersTotal mMyOrdersTotal;

        public final List<Agent> mAgentList;

        public final ModelCar mCar;

        public final List<ModelNotices> mNoticesList;

        public MyInfoCenterAPIResponse(JSONObject json) throws JSONException {
            super(json);
            String str = null;

            // 公告
            mNoticesList = new ArrayList<ModelNotices>();
            JSONArray noticesJsonArray = json.getJSONArray("notics");
            for (int i = 0; i < noticesJsonArray.length(); i++) {
                JSONObject noticeJsonObject = noticesJsonArray.getJSONObject(i);
                ModelNotices modelNotice = new ModelNotices();
                str = noticeJsonObject.isNull("id") ? "" : noticeJsonObject.getString("id");
                modelNotice.setId(str);

                str = noticeJsonObject.isNull("content") ? "" : noticeJsonObject.getString("content");
                modelNotice.setNotice(str);

                mNoticesList.add(modelNotice);
            }

            // 个人信息
            JSONObject carJsonObject = json.getJSONObject("car");
            mCar = new ModelCar();

            str = carJsonObject.isNull("id") ? "" : carJsonObject.getString("id");
            mCar.setId(str);

            str = carJsonObject.isNull("license") ? "" : carJsonObject.getString("license");
            mCar.setLicense(str);

            str = carJsonObject.isNull("starImg") ? "" : carJsonObject.getString("starImg");
            mCar.setStarImg(str);

            str = carJsonObject.isNull("creditcount") ? "" : carJsonObject.getString("creditcount");
            mCar.setCreditcount(str);

            str = carJsonObject.isNull("drivername") ? "" : carJsonObject.getString("drivername");
            mCar.setDriverName(str);

            str = carJsonObject.isNull("company") ? "" : carJsonObject.getString("company");
            mCar.setCompany(str);

            str = carJsonObject.isNull("photo") ? "" : carJsonObject.getString("photo");
            mCar.setPhoto(str);

            str = carJsonObject.isNull("level") ? "" : carJsonObject.getString("level");
            mCar.setCredit(str);

            str = carJsonObject.isNull("dealRanking") ? "" : carJsonObject.getString("dealRanking");
            mCar.setRankings(str);

            // // 运力信息
            JSONObject transportJsonObject = json.getJSONObject("releaseload");
            mModelTransport = new ModelTransport();

            str = transportJsonObject.isNull("carcity") ? "" : transportJsonObject.getString("carcity");
            mModelTransport.setCarcity(str);

            str = transportJsonObject.isNull("targetcity") ? "" : transportJsonObject.getString("targetcity");
            mModelTransport.setTargetcity(str);

            str = transportJsonObject.isNull("leftload") ? "" : transportJsonObject.getString("leftload");
            mModelTransport.setLeftload(str);

            str = transportJsonObject.isNull("id") ? "" : transportJsonObject.getString("id");
            mModelTransport.setId(str);

            // 货源信息
            JSONObject goodsJsonObject = json.getJSONObject("goods");
            mGoodsTotal = new GoodsTotal();
            mGoodsTotal.setTodayTotal(goodsJsonObject.getString("nearlytoday"));// 今日新增数量
            mGoodsTotal.setAllTotal(goodsJsonObject.getString("nearlyall"));// 货源总数量

            // 订单信息
            JSONObject orderJsonObject = json.getJSONObject("orders");
            mMyOrdersTotal = new MyOrdersTotal();
            // mMyOrdersTotal.setAllorder(orderJsonObject.getString("allorder"));
            mMyOrdersTotal.setPayorder(orderJsonObject.getString("payorder"));// 待付款
            mMyOrdersTotal.setPickingorder(orderJsonObject.getString("pickingorder"));// 待配货
            mMyOrdersTotal.setSignorder(orderJsonObject.getString("signorder"));// 待签收
            mMyOrdersTotal.setEvelorder(orderJsonObject.getString("evelorder"));// 待评价
            mMyOrdersTotal.setCancelorder(orderJsonObject.getString("cancelorder"));// 取消

            // 货代信息
            mAgentList = new ArrayList<Agent>();
            JSONArray agentJsonArray = json.getJSONArray("agents");
            for (int i = 0; i < agentJsonArray.length(); i++) {
                Agent mAgent = new Agent();
                JSONObject agentJsonObject = agentJsonArray.getJSONObject(i);
                mAgent.setId(Long.parseLong(String.valueOf(i + 1)));
                mAgent.setMId(agentJsonObject.getString("id"));
                // mAgent.setLocation(agentJsonObject.getString("location"));
                // mAgent.setAddress(agentJsonObject.getString("address"));
                // mAgent.setName(agentJsonObject.getString("name"));
                // mAgent.setTel(agentJsonObject.getString("tel"));
                mAgent.setCompany(agentJsonObject.getString("company"));
                // mAgent.setAgentno(agentJsonObject.getString("agentno"));
                mAgentList.add(mAgent);
            }

        }
    }
}
