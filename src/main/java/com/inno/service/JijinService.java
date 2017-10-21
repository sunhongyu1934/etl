package com.inno.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JijinService {
    public Map<String,Object> find();
    public List<Map<String,Object>> clean(Map<String,Object> map) throws IOException, InterruptedException;
    public void delete(List<Map<String,Object>> list);
    public void insert(List<Map<String,Object>> list);
}
