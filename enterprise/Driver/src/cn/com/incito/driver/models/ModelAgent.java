
package cn.com.incito.driver.models;

/**
 * 我的订单-列表中 货代对象模型
 * 
 * @description
 * @author qiujiaheng
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelAgent {
    private String id;

    private String name;// 货代联系人

    private String tel;

    private String photo;

    private String agentno;

    private String starImg;// 货代积分(星级)

    private String address;

    private String company;// 货代公司名称

    private String location;

    private String province;// 定位货代的省

    private String city;// 定位货代的市

    private String country;// 定位货代的县

    private String street;// 定位货代的街道

    private String credit;// 星级评分

    private String creditcount;// 成交笔数

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCreditcount() {
        return creditcount;
    }

    public void setCreditcount(String creditcount) {
        this.creditcount = creditcount;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAgentno() {
        return agentno;
    }

    public void setAgentno(String agentno) {
        this.agentno = agentno;
    }

    public String getStarImg() {
        return starImg;
    }

    public void setStarImg(String starImg) {
        this.starImg = starImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
