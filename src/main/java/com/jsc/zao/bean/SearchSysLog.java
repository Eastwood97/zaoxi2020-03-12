package com.jsc.zao.bean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
public class SearchSysLog {
    private String pageCode;
    private String pageSize;
    private String username;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createDate;

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
