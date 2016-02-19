package com.qf.model;

import java.io.Serializable;

/**
 * 城市实体
 */
public class CityEntity implements Serializable{

    private int cityid;
    private String cityname;
    private int type = 1;//0--表示标签  1--表示城市

    public CityEntity(){

    }

    public CityEntity(String cityname, int type) {
        this.cityname = cityname;
        this.type = type;
    }

    public boolean eq(String label){
        if(this.type == 0 && cityname.equals(label)){
            return true;
        }
        return false;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
