package com.inno.dao;

import com.inno.bean.TestBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TestDao {
    public List<Map<String,Object>> find(int id) throws IOException;
}
