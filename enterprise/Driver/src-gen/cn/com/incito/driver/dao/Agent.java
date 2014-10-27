package cn.com.incito.driver.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table AGENT.
 */
public class Agent {

    private Long id;
    private String mId;
    private String agentno;
    private String tel;
    private String company;
    private String address;
    private String name;
    private String location;
    private String photo;
    private String status;
    private String agentLevel;
    private Long createtime;

    public Agent() {
    }

    public Agent(Long id) {
        this.id = id;
    }

    public Agent(Long id, String mId, String agentno, String tel, String company, String address, String name, String location, String photo, String status, String agentLevel, Long createtime) {
        this.id = id;
        this.mId = mId;
        this.agentno = agentno;
        this.tel = tel;
        this.company = company;
        this.address = address;
        this.name = name;
        this.location = location;
        this.photo = photo;
        this.status = status;
        this.agentLevel = agentLevel;
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMId() {
        return mId;
    }

    public void setMId(String mId) {
        this.mId = mId;
    }

    public String getAgentno() {
        return agentno;
    }

    public void setAgentno(String agentno) {
        this.agentno = agentno;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

}
