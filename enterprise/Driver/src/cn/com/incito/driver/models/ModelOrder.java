
package cn.com.incito.driver.models;

/**
 * 我的订单列表中返回的订单模型
 * 
 * @description 我的订单列表中返回的订单模型
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelOrder {
    private String id;

    private String status;// 订单状态：0
                          // 全部、1待付款、2待配货、3待签收、4待评价、5退款订单、6拒绝退款、7已取消、8已预订、9待收款

    private String orderno;

    private String generatetime;

    private String goodscustom; // 标识是公共货源订单还是自有货源订单，默认0是公共货源订单

    private String weight;// 指派的吨位

    private String volume;// 指派的吨位

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getGeneratetime() {
        return generatetime;
    }

    public void setGeneratetime(String generatetime) {
        this.generatetime = generatetime;
    }

    public String getGoodscustom() {
        return goodscustom;
    }

    public void setGoodscustom(String goodscustom) {
        this.goodscustom = goodscustom;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

}
