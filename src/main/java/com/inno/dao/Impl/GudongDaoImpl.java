package com.inno.dao.Impl;

import com.inno.dao.GudongDao;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GudongDaoImpl implements GudongDao {
    @Override
    public List<Map<String, Object>> findList(String lim) {
        String ns="mapping.GudongMapper.getRead";
        Map<String,String> map=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        for(int x=0;x<=9;x++){
            map.put("tablename","comp_gudong_sum"+x);
            map.put("lim",lim);
            list.addAll(MybatisUtils.getInstance().selectList(ns,map));
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findshang(List<String> ll,String has) {
        String ns="mapping.GudongMapper.getSh";
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        map.put("tablename","comp_gudong_sum"+has);
        map.put("field",ll);
        list.addAll(MybatisUtils.getInstance().selectList(ns,map));
        return list;
    }

    @Override
    public void delete(Map<String, Object> map) {
        String ns="mapping.GudongMapper.delete";
        MybatisUtils.getInstance().delete(ns,map);
    }

    @Override
    public void insert(Map<String, Object> map) {
        String ns="mapping.GudongMapper.insert";
        Map<String,Object> mm=new HashMap<>();
        List<Map<String,Object>> list= Dup.quchong((List<Map<String, Object>>) map.get("field"),"tail_id");
        mm.put("field",list);
        MybatisUtils.getInstance().insert(ns,mm);
    }
}
