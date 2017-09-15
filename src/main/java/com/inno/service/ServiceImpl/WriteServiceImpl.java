package com.inno.service.ServiceImpl;

import com.inno.bean.WriteTableBean;
import com.inno.dao.Impl.WriteDaoImpl;
import com.inno.dao.WriteDao;
import com.inno.service.WriteService;

import java.util.Map;

public class WriteServiceImpl implements WriteService{
    WriteDao wr=new WriteDaoImpl();
    @Override
    public void insert(Map<String, Object> map, WriteTableBean w) {
        wr.insertList(map,w);
    }

    @Override
    public void delete(Map<String, Object> map, WriteTableBean w) {
        wr.delete(map,w);
    }
}
