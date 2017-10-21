package com.inno.dao.Impl;

import com.inno.dao.TuichuDao;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuichuDaoImpl implements TuichuDao {
    @Override
    public List<Map<String, Object>> findList() {
        String ns="mapping.TuichuMapper.getRead";
        Map<String,String> map=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        for(int x=0;x<=9;x++){
            map.put("tablename","fund_exit_sum"+x);
            list.addAll(MybatisUtils.getInstance().selectList(ns,map));
        }
        return list;
    }

    @Override
    public void delete(Map<String, Object> map) {
        String ns="mapping.TuichuMapper.delete";
        MybatisUtils.getInstance().delete(ns,map);
    }

    @Override
    public void insert(Map<String, Object> map) {
        String ns="mapping.TuichuMapper.insert";
        Map<String,Object> mm=new HashMap<>();
        List<Map<String,Object>> list= Dup.quchong((List<Map<String, Object>>) map.get("field"),"tail_id");
        mm.put("field",list);
        MybatisUtils.getInstance().insert(ns,map);
    }

}
