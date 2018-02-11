package com.inno.service;

import java.util.List;
import java.util.Map;

public interface XionganService {
    public List<Map<String,Object>> find();
    public void insert(List<Map<String,Object>> map);
    public void delete(List<Map<String,Object>> map);
}
