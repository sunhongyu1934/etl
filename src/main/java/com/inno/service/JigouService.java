package com.inno.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JigouService {
    public List<Map<String,Object>> find();
    public List<Map<String,Object>> clean(List<Map<String,Object>> list) throws IOException;
    public void delete(List<Map<String,Object>> list);
    public void insert(List<Map<String,Object>> list);
}
