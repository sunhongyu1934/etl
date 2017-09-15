package com.inno.dao.Impl;

import com.inno.bean.WriteTableBean;
import com.inno.dao.WriteDao;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.Map;

public class WriteDaoImpl implements WriteDao{
    private String ns;

    @Override
    public void insertList(Map<String, Object> map, WriteTableBean w) {
        this.ns="mapping.WriteMapper.insert"+map.get("dim");
        map.put("tablename",w.getTablename()+map.get("hasid"));
        MybatisUtils.getInstance().insert(ns,map);
    }

    @Override
    public void delete(Map<String, Object> map, WriteTableBean w) {
        this.ns="mapping.WriteMapper.delete";
        map.put("tablename",w.getTablename()+map.get("hasid"));
        MybatisUtils.getInstance().delete(ns,map);
    }
}
