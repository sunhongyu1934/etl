package com.inno.dao.Impl;

import com.inno.dao.WebDao;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.*;

public class WebDaoImpl implements WebDao {
    @Override
    public List<Map<String, Object>> findList(String lim) {
        String ns="mapping.WebMapper.getRead";
        Map<String,String> map=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        for(int x=0;x<=9;x++){
            map.put("tablename","comp_web_sum"+x);
            map.put("lim",lim);
            list.addAll(MybatisUtils.getInstance().selectList(ns,map));
        }
        return list;
    }

    @Override
    public void delete(List<Map<String,Object>> list) {
        String ns="mapping.WebMapper.delete";
        Set<String> set=new HashSet<>();
        for(Map<String,Object> mm:list){
            set.add((String) mm.get("source"));
        }

        for(String s:set){
            Map<String,Object> mmp=new HashMap<>();
            List<String> ll=new ArrayList<>();
            for(Map<String,Object> mm:list){
                if (s.equals(mm.get("source"))) {
                    ll.add((String) mm.get("only_id"));
                }
            }
            mmp.put("tablename", "comp_web");
            mmp.put("source",s);
            mmp.put("field",ll);
            if(ll!=null&&ll.size()>0) {
                MybatisUtils.getInstance().delete(ns, mmp);
            }
        }

    }

    @Override
    public void insert(List<Map<String,Object>> ll) {
        String ns="mapping.WebMapper.insert";
        Map<String,Object> mm=new HashMap<>();
        List<Map<String,Object>> list= Dup.quchong(ll,"only_id");
        mm.put("field",list);
        MybatisUtils.getInstance().insert(ns,mm);
    }
}
