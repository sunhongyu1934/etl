package com.inno.service;

import com.inno.bean.TestBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TestService {
    public List<Map<String,Object>> find(int id) throws IOException;
}
