package com.inno.bean;

import java.util.List;

public class ReadTableBean {
    private String dimname;
    private String tablename;
    private List<String> sources;
    private String companyfield;
    private String sleep;
    private List<String> projectfield;
    private String id;

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

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public List<String> getProjectfield() {
        return projectfield;
    }

    public void setProjectfield(List<String> projectfield) {
        this.projectfield = projectfield;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
