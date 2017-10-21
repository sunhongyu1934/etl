package com.inno.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WebService {
    public List<Map<String,Object>> find(String lim);
    public void clean(List<Map<String,Object>> list) throws IOException;
}
