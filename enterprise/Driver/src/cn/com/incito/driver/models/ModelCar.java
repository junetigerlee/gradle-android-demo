
package cn.com.incito.driver.models;

/**
 * 个人中心->个人信息
 * 
 * @description
 * @author 个人中心->个人信息
 * @createDate 2014年10月16日
 * @version 1.0
 */
public class ModelCar {
    private String id;

    private String license;

    private String driverName;// 货代联系人

    private String tel;

    private String photo;

    private String starImg;// 积分(星级)

    private String address;

    private String company;// 公司名称

    private String location;

    private String credit;// 星级评分

    private String creditcount;// 成交笔数

    private String rankings;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

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

    public String getRankings() {
        return rankings;
    }

    public void setRankings(String rankings) {
        this.rankings = rankings;
    }

}
