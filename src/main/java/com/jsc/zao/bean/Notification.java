package com.jsc.zao.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Notification {
    private Integer id;
    private String plmn;
    private String imsi;
    private String imei;
    private String isdn;
    private String isTarget;
    private String name;
    private  String area;   //归属地
    private String mode;
    private Timestamp catchTime;
    private String lng;
    private String  lat;
    private String desc;
    private String frtDevId;    //前端设备id
    private  String recordName;
    private String caseName;
    private  String  from;   //目标来源
    private  int state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn;
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

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getIsTarget() {
        return isTarget;
    }

    public void setIsTarget(String isTarget) {
        this.isTarget = isTarget;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonFormat(timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    public Timestamp getCatchTime() {
        return catchTime;
    }

    public void setCatchTime(Timestamp catchTime) {
        this.catchTime = catchTime;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrtDevId() {
        return frtDevId;
    }

    public void setFrtDevId(String frtDevId) {
        this.frtDevId = frtDevId;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
