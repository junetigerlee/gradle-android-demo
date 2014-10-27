
package cn.com.incito.driver.net.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.incito.driver.models.ModelOrderListItem;

/**
 * 我的订单 json数据解析器 新版本
 * 
 * @description 我的订单 json数据解析器 新版本
 * @author qiujiaheng
 * @createDate 2014年10月14日
 * @version 1.0
 */
public class MyOrdersResponseParserNew {

    public static List<ModelOrderListItem> ParseResponse(JSONObject json) throws JSONException {
        List<ModelOrderListItem> ordersList = new ArrayList<ModelOrderListItem>();
        ModelOrderListItem orders = null;
        String str = null;
        int temp = 0;
        int count = json.getInt("total");
        JSONArray dataArray = json.getJSONArray("items");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject dataObj = dataArray.getJSONObject(i);
            orders = new ModelOrderListItem();
            if (!dataObj.isNull("order"))
                orders.setOrder(dataObj.getString("order"));
            if (!dataObj.isNull("goods"))
                orders.setGoods(dataObj.getString("goods"));
            if (!dataObj.isNull("consigne")) {
                if (!"{}".equals(dataObj.getString("consigne"))) {
                    orders.setConsigne(dataObj.getString("consigne"));
                }
            }
            if (!dataObj.isNull("shipper")) {
                if (!"{}".equals(dataObj.getString("shipper"))) {
                    orders.setShipper(dataObj.getString("shipper"));
                }
            }
            if (!dataObj.isNull("agent"))
                orders.setAgent(dataObj.getString("agent"));
            ordersList.add(orders);
        }
        return ordersList;
    }
}
