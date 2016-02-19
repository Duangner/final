package com.qf.model;

import java.io.Serializable;

/**
 * 首页头部控件的数据实体s
 */
public class HeadEntity implements Serializable{
    private String houseid;
    private String title;
    private String picurl;

    public String getHouseid() {
        return houseid;
    }

    public void setHouseid(String houseid) {
        this.houseid = houseid;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
