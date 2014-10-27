
package cn.com.incito.driver.models.orders;

/**
 * 我的订单，分类总数
 * 
 * @description 我的订单，分类总数
 * @author zhangyushuang
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class MyOrdersTotal {
    private String allorder;

    private String payorder;

    private String pickingorder;

    private String signorder;

    private String evelorder;

    private String cancelorder;

    private String receiveorder;

    public MyOrdersTotal() {
        // TODO Auto-generated constructor stub
    }

    public String getAllorder() {
        return allorder;
    }

    public void setAllorder(String allorder) {
        this.allorder = allorder;
    }

    public String getPayorder() {
        return payorder;
    }

    public void setPayorder(String payorder) {
        this.payorder = payorder;
    }

    public String getPickingorder() {
        return pickingorder;
    }

    public void setPickingorder(String pickingorder) {
        this.pickingorder = pickingorder;
    }

    public String getSignorder() {
        return signorder;
    }

    public void setSignorder(String signorder) {
        this.signorder = signorder;
    }

    public String getEvelorder() {
        return evelorder;
    }

    public void setEvelorder(String evelorder) {
        this.evelorder = evelorder;
    }

    public String getCancelorder() {
        return cancelorder;
    }

    public void setCancelorder(String cancelorder) {
        this.cancelorder = cancelorder;
    }

    public String getReceiveorder() {
        return receiveorder;
    }

    public void setReceiveorder(String receiveorder) {
        this.receiveorder = receiveorder;
    }

}
