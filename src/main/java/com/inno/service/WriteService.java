package com.inno.service;

import com.inno.bean.WriteTableBean;

import java.util.List;
import java.util.Map;

public interface WriteService {
    public void insert(List<Map<String,Object>> map, WriteTableBean w);
    public void delete(List<Map<String,Object>> map, WriteTableBean w);
}
