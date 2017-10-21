package com.inno.dao.Impl;

import com.inno.dao.JijinDao;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JijinDaoImpl implements JijinDao {
    @Override
    public Map<String, Object> findList() {
        String ns="mapping.JijinMapper.getRead";
        Map<String,Object> map=new HashMap<>();
        Map<String,String> zmap=new HashMap<>();
        String[] tables=new String[]{"fund_base_sum","fund_base1_sum"};
        for(String s:tables){
            List<Map<String,Object>> list=new ArrayList<>();
            for(int x=0;x<=9;x++){
                zmap.put("tablename",s+x);
                list.addAll(MybatisUtils.getInstance().selectList(ns,zmap));
            }
            map.put(s,list);
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> findGid() {
        String ns="mapping.JijinMapper.getRead";
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for(int x=0;x<=9;x++){
            map.put("tablename","jigou_base_sum"+x);
            list.addAll(MybatisUtils.getInstance().selectList(ns,map));
        }
        return list;
    }

    @Override
    public void delete(Map<String, Object> map) {
        String ns="mapping.JijinMapper.delete";
        MybatisUtils.getInstance().delete(ns,map);
    }

    @Override
    public void insert(Map<String, Object> map) {
        String ns="mapping.JijinMapper.insert";
        Map<String,Object> mm=new HashMap<>();
        List<Map<String,Object>> list= Dup.quchong((List<Map<String, Object>>) map.get("field"),"fund_id");
        mm.put("field",list);
        MybatisUtils.getInstance().insert(ns,mm);
    }
}
