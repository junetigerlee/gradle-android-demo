package cn.com.incito.driver.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table COMMENT.
 */
public class Comment {

    private Long id;
    private String status;
    private String level;
    private String content;
    private Long createtime;

    public Comment() {
    }

    public Comment(Long id) {
        this.id = id;
    }

    public Comment(Long id, String status, String level, String content, Long createtime) {
        this.id = id;
        this.status = status;
        this.level = level;
        this.content = content;
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

}
