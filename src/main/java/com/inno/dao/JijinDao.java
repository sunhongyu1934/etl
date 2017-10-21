package com.inno.dao;

import java.util.List;
import java.util.Map;

public interface JijinDao {
    public Map<String,Object> findList();
    public List<Map<String,Object>> findGid();
    public void delete(Map<String,Object> map);
    public void insert(Map<String,Object> map);
}
