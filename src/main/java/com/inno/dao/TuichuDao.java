package com.inno.dao;

import java.util.List;
import java.util.Map;

public interface TuichuDao {
    public List<Map<String,Object>> findList();
    public void delete(Map<String,Object> map);
    public void insert(Map<String,Object> map);
}
