package com.inno.bean;

import java.util.List;

public class ReadTableBean {
    private String dimname;
    private String tablename;
    private List<String> sources;
    private String companyfield;
    private String time;
    private String loadtime;
    private String sleep;
    private int a;
    private String tt;
    private String projectfield;

    public String getDimname() {
        return dimname;
    }

    public void setDimname(String dimname) {
        this.dimname = dimname;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getCompanyfield() {
        return companyfield;
    }

    public void setCompanyfield(String companyfield) {
        this.companyfield = companyfield;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLoadtime() {
        return loadtime;
    }

    public void setLoadtime(String loadtime) {
        this.loadtime = loadtime;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getProjectfield() {
        return projectfield;
    }

    public void setProjectfield(String projectfield) {
        this.projectfield = projectfield;
    }
}
