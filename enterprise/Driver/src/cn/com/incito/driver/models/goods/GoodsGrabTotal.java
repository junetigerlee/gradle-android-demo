
package cn.com.incito.driver.models.goods;

/**
 * 抢货源数量
 * 
 * @description 抢货源数量
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class GoodsGrabTotal {
    private String availableGoods;// 可抢货源数量

    private String hasAvailableGoods;// 已抢货源数量

    private String succGrabNum;// 已抢货源成功的数量

    private String failGrabNum;// 已抢货源失败的数量

    public GoodsGrabTotal() {
    }

    public String getAvailableGoods() {
        return availableGoods;
    }

    public void setAvailableGoods(String availableGoods) {
        this.availableGoods = availableGoods;
    }

    public String getHasAvailableGoods() {
        return hasAvailableGoods;
    }

    public void setHasAvailableGoods(String hasAvailableGoods) {
        this.hasAvailableGoods = hasAvailableGoods;
    }

    public String getSuccGrabNum() {
        return succGrabNum;
    }

    public void setSuccGrabNum(String succGrabNum) {
        this.succGrabNum = succGrabNum;
    }

    public String getFailGrabNum() {
        return failGrabNum;
    }

    public void setFailGrabNum(String failGrabNum) {
        this.failGrabNum = failGrabNum;
    }

}
