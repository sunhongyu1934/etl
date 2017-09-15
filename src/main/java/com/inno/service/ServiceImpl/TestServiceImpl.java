package com.inno.service.ServiceImpl;

import com.inno.dao.Impl.TestDaoImpl;
import com.inno.dao.TestDao;
import com.inno.service.TestService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestServiceImpl implements TestService{
    TestDao t=new TestDaoImpl();
    @Override
    public List<Map<String,Object>> find(int id) throws IOException {
        return t.find(id);
    }
}
