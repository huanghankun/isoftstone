package com.system.sign.service.model;

public class Attence {
    private String attenceEmpNo;
    private String attenceEmpName;
    private Integer attenceType;
    private String attenceRemark;
    private Long attenceDeviceID;

    public String getAttenceEmpNo() {
        return attenceEmpNo;
    }

    public void setAttenceEmpNo(String attenceEmpNo) {
        this.attenceEmpNo = attenceEmpNo;
    }

    public String getAttenceEmpName() {
        return attenceEmpName;
    }

    public void setAttenceEmpName(String attenceEmpName) {
        this.attenceEmpName = attenceEmpName;
    }

    public Integer getAttenceType() {
        return attenceType;
    }

    public void setAttenceType(Integer attenceType) {
        this.attenceType = attenceType;
    }

    public String getAttenceRemark() {
        return attenceRemark;
    }

    public void setAttenceRemark(String attenceRemark) {
        this.attenceRemark = attenceRemark;
    }

    public Long getAttenceDeviceID() {
        return attenceDeviceID;
    }

    public void setAttenceDeviceID(Long attenceDeviceID) {
        this.attenceDeviceID = attenceDeviceID;
    }
}