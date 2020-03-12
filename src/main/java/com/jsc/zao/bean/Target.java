package com.jsc.zao.bean;

/**
 * 目标库实体类
 */
public class Target {
    private int id;
    private  String imsi;
    private String imei;
    private String name;
    private String isdn;
    private String caseName;
    private String desc;
    private String from;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Target{" +
                "id=" + id +
                ", imsi='" + imsi + '\'' +
                ", imei='" + imei + '\'' +
                ", 目标名称='" + name + '\'' +
                ", 电话号码='" + isdn + '\'' +
                ", 案件名='" + caseName + '\'' +
                ", 描述='" + desc + '\'' +
                ", 来源='" + from + '\'' +
                '}';
    }
}
