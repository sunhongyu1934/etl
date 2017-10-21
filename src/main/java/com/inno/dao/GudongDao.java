package com.inno.dao;

import java.util.List;
import java.util.Map;

public interface GudongDao {
    public List<Map<String,Object>> findList(String lim);
    public List<Map<String,Object>> findshang(List<String> ll,String has);
    public void delete(Map<String,Object> map);
    public void insert(Map<String,Object> map);
}
