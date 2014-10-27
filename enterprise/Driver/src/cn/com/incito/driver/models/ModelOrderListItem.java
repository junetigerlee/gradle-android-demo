
package cn.com.incito.driver.models;

/**
 * 我的订单-列表大的订单模型
 * 
 * @description 我的订单-列表大的订单模型
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelOrderListItem {
    private String order;// 订单 json

    private String goods;// 货源json

    private String consigne;// 收货人json

    private String shipper;// 发货人json

    private String agent;// 货代json

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getConsigne() {
        return consigne;
    }

    public void setConsigne(String consigne) {
        this.consigne = consigne;
    }

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

}
