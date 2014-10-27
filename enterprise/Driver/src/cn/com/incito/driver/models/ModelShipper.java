
package cn.com.incito.driver.models;

/**
 * 我的订单-列表中返回的 发货人、收货人对象模型
 * 
 * @description 我的订单-列表中返回的 发货人、收货人对象模型
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelShipper {
    private String id;

    private String name;

    private String paystatus;// 付款方式 0货代付款，1回单付款

    private String company;

    private String address;

    private String mobile;

    private String backstatus;

    private String generatetime;// 回单状态，默认””(null)，0原件，1传真件，2扫描件

    private String province; // 省

    private String city; // 市

    private String country;// 区

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGeneratetime() {
        return generatetime;
    }

    public void setGeneratetime(String generatetime) {
        this.generatetime = generatetime;
    }

}
