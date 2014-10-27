
package cn.com.incito.driver.models.goods;

/**
 * 个人中心->货源数量
 * 
 * @description 个人中心->货源数量
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class GoodsTotal {
    private String allTotal;

    private String todayTotal;

    public GoodsTotal() {
        // TODO Auto-generated constructor stub
    }

    public String getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(String allTotal) {
        this.allTotal = allTotal;
    }

    public String getTodayTotal() {
        return todayTotal;
    }

    public void setTodayTotal(String todayTotal) {
        this.todayTotal = todayTotal;
    }

}
