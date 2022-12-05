package com.augurit.agcloud.agcom.agsupport.sc.regainWork.service;

import java.util.Date;

public class Project {
    private String gcbm;//工程编码
    private String gcmc;//工程名称
    private String jslb;//建设类别
    private float xzb;//X坐标
    private float yzb;//Y坐标
    private String szqx;//工程所在区县
    private String szqx1;//管理单位所在区县
    private String xmzt;//项目状态
    private String nkgrq;//(拟)复/开工日期
    private String yqfzr;//疫情负责人
    private String yqfzrsjhm;//疫情负责人手机号码
    private String gdzrys;//工地总人员数
    private String xcglrys;//现场管理人员数
    private String yxzyrys;//一线作业人员数
    private String yfgzrs;//已返岗总人数
    private String yqzblrs;//已确诊病例人数
    private String yjcqzysblrs;//有接触确诊、疑似病例人数
    private String yfghbjhwzjrs;//已返岗湖北籍或温州籍人数
    private String ylxyyxchbhwzfycyrs;//已联系有意向从湖北或温州返粤从业人数
    private String ysblrs;//疑似病例人数
    private int status;//复工状态：1-拟已复工、2-未复工、3-拟已复工存在疫情隐患

    public String getGcbm() {
        return gcbm;
    }

    public void setGcbm(String gcbm) {
        this.gcbm = gcbm;
    }

    public String getGcmc() {
        return gcmc;
    }

    public void setGcmc(String gcmc) {
        this.gcmc = gcmc;
    }

    public String getJslb() {
        return jslb;
    }

    public void setJslb(String jslb) {
        this.jslb = jslb;
    }

    public float getXzb() {
        return xzb;
    }

    public void setXzb(float xzb) {
        this.xzb = xzb;
    }

    public float getYzb() {
        return yzb;
    }

    public void setYzb(float yzb) {
        this.yzb = yzb;
    }

    public String getSzqx() {
        return szqx;
    }

    public void setSzqx(String szqx) {
        this.szqx = szqx;
    }

    public String getSzqx1() {
        return szqx1;
    }

    public void setSzqx1(String szqx1) {
        this.szqx1 = szqx1;
    }

    public String getXmzt() {
        return xmzt;
    }

    public void setXmzt(String xmzt) {
        this.xmzt = xmzt;
    }

    public String getNkgrq() {
        return nkgrq;
    }

    public void setNkgrq(String nkgrq) {
        this.nkgrq = nkgrq;
    }

    public String getYqfzr() {
        return yqfzr;
    }

    public void setYqfzr(String yqfzr) {
        this.yqfzr = yqfzr;
    }

    public String getYqfzrsjhm() {
        return yqfzrsjhm;
    }

    public void setYqfzrsjhm(String yqfzrsjhm) {
        this.yqfzrsjhm = yqfzrsjhm;
    }

    public String getGdzrys() {
        return gdzrys;
    }

    public void setGdzrys(String gdzrys) {
        this.gdzrys = gdzrys;
    }

    public String getXcglrys() {
        return xcglrys;
    }

    public void setXcglrys(String xcglrys) {
        this.xcglrys = xcglrys;
    }

    public String getYxzyrys() {
        return yxzyrys;
    }

    public void setYxzyrys(String yxzyrys) {
        this.yxzyrys = yxzyrys;
    }

    public String getYfgzrs() {
        return yfgzrs;
    }

    public void setYfgzrs(String yfgzrs) {
        this.yfgzrs = yfgzrs;
    }

    public String getYqzblrs() {
        return yqzblrs;
    }

    public void setYqzblrs(String yqzblrs) {
        this.yqzblrs = yqzblrs;
    }

    public String getYjcqzysblrs() {
        return yjcqzysblrs;
    }

    public void setYjcqzysblrs(String yjcqzysblrs) {
        this.yjcqzysblrs = yjcqzysblrs;
    }

    public String getYfghbjhwzjrs() {
        return yfghbjhwzjrs;
    }

    public void setYfghbjhwzjrs(String yfghbjhwzjrs) {
        this.yfghbjhwzjrs = yfghbjhwzjrs;
    }

    public String getYlxyyxchbhwzfycyrs() {
        return ylxyyxchbhwzfycyrs;
    }

    public void setYlxyyxchbhwzfycyrs(String ylxyyxchbhwzfycyrs) {
        this.ylxyyxchbhwzfycyrs = ylxyyxchbhwzfycyrs;
    }

    public String getYsblrs() {
        return ysblrs;
    }

    public void setYsblrs(String ysblrs) {
        this.ysblrs = ysblrs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
