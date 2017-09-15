package com.inno.dao;

import com.inno.bean.OnlyidBean;

import java.util.Map;

public interface OnlyIdDao {
    public OnlyidBean select(String cname);
    public void insert(Map<String,String> map);
}
