package com.system.sign.service.model;

/**
 * @Description:员工信息
 * @param
 * @return:
 * @auther: ex_huanghk5
 * @date: 2018-07-12 10:38
 */
public class EmployeeInfo {
    /*{
            "BaseCurrency": 719,
            "Comp": "软通动力技术服务有限公司广州分公司",
            "Dep": "广州DTG企业金融资金实施部",
            "DepNo": "10032015",
            "DomainName": "hkhuangc",
            "Email": "hkhuangc@isoftstone.com",
            "Ext": "",
            "ID": 1175215,
            "Name": "黄汉坤",
            "No": "154351",
            "Position": "高级软件工程师",
            "UserTel": "13265111992"
    }*/
    private int BaseCurrency;
    private String Comp;
    private String Dep;
    private String DepNo;
    private String DomainName;
    private String Email;
    private String Ext;
    private String ID;
    private String Name;
    private String No;
    private String Position;
    private String UserTel;

    public int getBaseCurrency() {
        return BaseCurrency;
    }

    public void setBaseCurrency(int baseCurrency) {
        BaseCurrency = baseCurrency;
    }

    public String getComp() {
        return Comp;
    }

    public void setComp(String comp) {
        Comp = comp;
    }

    public String getDep() {
        return Dep;
    }

    public void setDep(String dep) {
        Dep = dep;
    }

    public String getDepNo() {
        return DepNo;
    }

    public void setDepNo(String depNo) {
        DepNo = depNo;
    }

    public String getDomainName() {
        return DomainName;
    }

    public void setDomainName(String domainName) {
        DomainName = domainName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getUserTel() {
        return UserTel;
    }

    public void setUserTel(String userTel) {
        UserTel = userTel;
    }


}
