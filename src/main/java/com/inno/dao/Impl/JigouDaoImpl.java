package com.inno.dao.Impl;

import com.inno.dao.JigouDao;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JigouDaoImpl implements JigouDao {
    @Override
    public List<Map<String, Object>> findList() {
        String ns="mapping.JigouMapper.getRead";
        Map<String,String> map=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        for(int x=0;x<=9;x++){
            map.put("tablename","jigou_base_sum"+x);
            list.addAll(MybatisUtils.getInstance().selectList(ns,map));
        }
        return list;
    }

    @Override
    public void delete(Map<String, Object> map) {
        String ns="mapping.JigouMapper.delete";
        MybatisUtils.getInstance().delete(ns,map);
    }

    @Override
    public void insert(Map<String, Object> map) {
        String ns="mapping.JigouMapper.insert";
        Map<String,Object> mm=new HashMap<>();
        List<Map<String,Object>> list= Dup.quchong((List<Map<String, Object>>) map.get("field"),"comp_id");
        mm.put("field",list);
        MybatisUtils.getInstance().insert(ns,mm);
    }
}
