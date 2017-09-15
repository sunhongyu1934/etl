package com.inno.dao;

import com.inno.bean.WriteTableBean;

import java.util.Map;

public interface WriteDao {
    public void insertList(Map<String,Object> map, WriteTableBean w);
    public void delete(Map<String,Object> map, WriteTableBean w);

}
