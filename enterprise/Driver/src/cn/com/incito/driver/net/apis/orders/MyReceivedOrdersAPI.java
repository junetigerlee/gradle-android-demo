
package cn.com.incito.driver.net.apis.orders;

import cn.com.incito.driver.dao.OrdersReceived;
import cn.com.incito.driver.models.ListAbstractModel;
import cn.com.incito.driver.models.ModelOrderListItem;
import cn.com.incito.driver.net.response.MyOrdersResponseParserNew;
import cn.com.incito.wisdom.sdk.net.http.BasicResponse;
import cn.com.incito.wisdom.sdk.net.http.WisdomCityServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 访问服务器获取我的待接受订单的API
 * 
 * @description 访问服务器获取我的待接受订单的API
 * @author lizhan
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyReceivedOrdersAPI extends WisdomCityServerAPI {
    private static final String RELATIVE_URL = "/logistics/order/carOrders";

    public MyReceivedOrdersAPI(Map<String, Object> params) {
        super(RELATIVE_URL, params, new String[] {
                ListAbstractModel.VCOLUMN_CAR_ID, ListAbstractModel.VCOLUMN_START, ListAbstractModel.VCOLUMN_PAGE_SIZE
        });
    }

    @Override
    protected BasicResponse parseResponse(JSONObject json) {
        try {
            return new MyReceivedOrdersAPIResponse(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class MyReceivedOrdersAPIResponse extends BasicResponse {

        public final List<OrdersReceived> mList;

        public MyReceivedOrdersAPIResponse(JSONObject json) throws JSONException {
            super(json);
            mList = new ArrayList<OrdersReceived>();
            List<ModelOrderListItem> ordersList = MyOrdersResponseParserNew.ParseResponse(json);
            for (ModelOrderListItem orders : ordersList) {
                OrdersReceived entity = new OrdersReceived();
                entity.setMyorder(orders.getOrder());
                entity.setGoods(orders.getGoods());
                entity.setConsigne(orders.getConsigne());
                entity.setShipper(orders.getShipper());
                entity.setAgent(orders.getAgent());
                entity.setCreatetime(System.currentTimeMillis());
                mList.add(entity);
            }
        }

    }

}
