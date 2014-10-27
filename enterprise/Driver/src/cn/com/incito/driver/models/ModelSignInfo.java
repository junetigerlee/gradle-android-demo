
package cn.com.incito.driver.models;

/**
 * 签收信息对象
 * 
 * @description 签收信息对象
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelSignInfo {
    private String signtime;

    private String memo;

    private String photo;

    private String refusal;//拒签理由

    private String type;// 0 签收，1拒签

    public String getRefusal() {
        return refusal;
    }

    public void setRefusal(String refusal) {
        this.refusal = refusal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSigntime() {
        return signtime;
    }

    public void setSigntime(String signtime) {
        this.signtime = signtime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
