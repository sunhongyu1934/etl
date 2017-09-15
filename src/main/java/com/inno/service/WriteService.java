package com.inno.service;

import com.inno.bean.WriteTableBean;

import java.util.Map;

public interface WriteService {
    public void insert(Map<String,Object> map, WriteTableBean w);
    public void delete(Map<String,Object> map, WriteTableBean w);
}
