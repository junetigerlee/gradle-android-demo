
package cn.com.incito.driver.models;

/**
 * 我的订单-列表中返回的 订单里面的货源对象模型
 * 
 * @description 我的订单-列表中返回的 订单里面的货源对象模型
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelGoods {
    // modify by zys.增加 回单 和 付款方式
    private String backstatus;

    private String paystatus;

    private String id;

    private String goodsno;

    private String goodsname;

    private String goodstype;// 货源类型 0： 重货 1：轻货 3：设备

    // private String status;// 货源状态 0：新货源，1：已预定，2：已成交 3:过期

    private String weight;

    private String volume;

    private String count;

    private String fare;// 运价

    private String infofare;// 信息费

    private String declaredvalue;// 声明价值

    // private String generatetime;// 创建时间
    // private String fetchdate;// 有效时间
    // private String completedate;// 完成时间
    private String memo;

    // private String assigncarno;// 发布对象 指定车辆 [空：全部]
    private String receiptprovince;

    private String receiptcity;

    private String receiptregion;

    private String originalprovince;

    private String originalcity;

    private String originalregion;

    private String agentid;

    public String getBackstatus() {
        return backstatus;
    }

    public void setBackstatus(String backstatus) {
        this.backstatus = backstatus;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getAgentid() {
        return agentid;
    }

    public String getCount() {
        return count;
    }

    public String getDeclaredvalue() {
        return declaredvalue;
    }

    public String getFare() {
        return fare;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public String getGoodsno() {
        return goodsno;
    }

    public String getGoodstype() {
        return goodstype;
    }

    public String getId() {
        return id;
    }

    public String getInfofare() {
        return infofare;
    }

    public String getMemo() {
        return memo;
    }

    public String getOriginalcity() {
        return originalcity;
    }

    public String getOriginalprovince() {
        return originalprovince;
    }

    public String getOriginalregion() {
        return originalregion;
    }

    public String getReceiptcity() {
        return receiptcity;
    }

    public String getReceiptprovince() {
        return receiptprovince;
    }

    public String getReceiptregion() {
        return receiptregion;
    }

    public String getVolume() {
        return volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setDeclaredvalue(String declaredvalue) {
        this.declaredvalue = declaredvalue;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public void setGoodsno(String goodsno) {
        this.goodsno = goodsno;
    }

    public void setGoodstype(String goodstype) {
        this.goodstype = goodstype;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfofare(String infofare) {
        this.infofare = infofare;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setOriginalcity(String originalcity) {
        this.originalcity = originalcity;
    }

    public void setOriginalprovince(String originalprovince) {
        this.originalprovince = originalprovince;
    }

    public void setOriginalregion(String originalregion) {
        this.originalregion = originalregion;
    }

    public void setReceiptcity(String receiptcity) {
        this.receiptcity = receiptcity;
    }

    public void setReceiptprovince(String receiptprovince) {
        this.receiptprovince = receiptprovince;
    }

    public void setReceiptregion(String receiptregion) {
        this.receiptregion = receiptregion;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

}
