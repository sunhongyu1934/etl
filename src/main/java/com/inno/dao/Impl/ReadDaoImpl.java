package com.inno.dao.Impl;

import com.inno.dao.ReadDao;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.List;
import java.util.Map;

public class ReadDaoImpl implements ReadDao{
    private String ns;
    public ReadDaoImpl(){
        this.ns="mapping.ReadMapper.getRead";
    }


    @Override
    public List<Map<String, Object>> findTimeList(Map<String,String> map) {
        return MybatisUtils.getInstance().selectList(ns,map);
    }
}
