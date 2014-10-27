
package cn.com.incito.driver.models;

/**
 * 星级评分工具类
 * 
 * @description 星级评分工具类
 * @author zhangyushuang
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class StarLevelModel {

    public String server;// 服务质量

    public String desc;// 货源描述

    public String speed;// 发货速度

    public String date;// 交付日期

    public String quality;// 交付质量

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

}
