package com.inno.dao;

import java.util.List;
import java.util.Map;

public interface ReadDao {
    public List<Map<String,Object>> findTimeList(Map<String,String> map);
}
