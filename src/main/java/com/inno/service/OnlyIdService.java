package com.inno.service;

import com.inno.bean.OnlyidBean;

import java.util.Map;

public interface OnlyIdService {
    public OnlyidBean select(String cname);
    public void insert(Map<String,String> map);
}
