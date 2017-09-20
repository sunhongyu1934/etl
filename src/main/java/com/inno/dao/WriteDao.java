package com.inno.dao;

import com.inno.bean.WriteTableBean;

import java.util.List;
import java.util.Map;

public interface WriteDao {
    public void insertList(List<Map<String,Object>> map, WriteTableBean w);
    public void delete(List<Map<String,Object>> map, WriteTableBean w);

}
