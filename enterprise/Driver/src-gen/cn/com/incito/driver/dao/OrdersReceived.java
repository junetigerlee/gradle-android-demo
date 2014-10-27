package cn.com.incito.driver.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ORDERS_RECEIVED.
 */
public class OrdersReceived {

    private Long id;
    private String myorder;
    private String goods;
    private String consigne;
    private String shipper;
    private String agent;
    private Long createtime;

    public OrdersReceived() {
    }

    public OrdersReceived(Long id) {
        this.id = id;
    }

    public OrdersReceived(Long id, String myorder, String goods, String consigne, String shipper, String agent, Long createtime) {
        this.id = id;
        this.myorder = myorder;
        this.goods = goods;
        this.consigne = consigne;
        this.shipper = shipper;
        this.agent = agent;
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMyorder() {
        return myorder;
    }

    public void setMyorder(String myorder) {
        this.myorder = myorder;
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

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

}
