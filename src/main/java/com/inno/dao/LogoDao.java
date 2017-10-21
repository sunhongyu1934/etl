package com.inno.dao;

import java.util.List;
import java.util.Map;

public interface LogoDao {
    public List<Map<String,Object>> findList(String lim);
    public void delete(List<Map<String,Object>> list);
    public void insert(List<Map<String,Object>> list);
}
