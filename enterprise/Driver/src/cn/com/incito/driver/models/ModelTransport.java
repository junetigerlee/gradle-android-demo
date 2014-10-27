
package cn.com.incito.driver.models;

/**
 * 个人中心->运力信息
 * 
 * @description 个人中心->运力信息
 * @author lizhan
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelTransport {
    private String id;

    private String leftload;

    private String carcity;

    private String targetcity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeftload() {
        return leftload;
    }

    public void setLeftload(String leftload) {
        this.leftload = leftload;
    }

    public String getCarcity() {
        return carcity;
    }

    public void setCarcity(String carcity) {
        this.carcity = carcity;
    }

    public String getTargetcity() {
        return targetcity;
    }

    public void setTargetcity(String targetcity) {
        this.targetcity = targetcity;
    }

}
