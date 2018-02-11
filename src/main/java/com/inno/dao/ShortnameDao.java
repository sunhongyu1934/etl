package com.inno.dao;

import java.util.List;
import java.util.Map;

public interface ShortnameDao {
    public List<Map<String,Object>> find(Map<String,String> map);
    public void update(Map<String,String> map);
    public List<Map<String,Object>> findfin(Map<String,String> map);
}
