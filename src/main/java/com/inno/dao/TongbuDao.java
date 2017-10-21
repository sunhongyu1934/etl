package com.inno.dao;

import com.inno.bean.TongbuBean;
import com.inno.bean.WriteTableBean;

import java.util.List;
import java.util.Map;

public interface TongbuDao {
    public List<Map<String,Object>> findTimeList(Map<String,String> map);
    public void insertList(List<Map<String,Object>> map, TongbuBean.Write w);
    public void delete(List<Map<String,Object>> map, TongbuBean.Write w);
}
