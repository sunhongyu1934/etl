package com.inno.dao.Impl;

import com.inno.bean.WriteTableBean;
import com.inno.dao.WriteDao;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.mysql.cj.core.util.StringUtils;

import java.util.*;

public class WriteDaoImpl implements WriteDao{
    @Override
    public void insertList(List<Map<String, Object>> list, WriteTableBean w) {
        String ns="mapping.WriteMapper.insert"+w.getDimname();

        for(int x=0;x<=9;x++) {
            List<Map<String, Object>> ll = new ArrayList<>();
            Map<String,Object> mmp=new HashMap<>();
            for (Map<String, Object> mm : list) {
                String hid = (String) mm.get("hasid");
                if (Integer.parseInt(hid) == x) {
                    ll.add(mm);
                }
            }
            mmp.put("tablename", w.getTablename() + x);
            mmp.put("field",ll);
            MybatisUtils.getInstance().insert(ns,mmp);
        }
    }



    @Override
    public void delete(List<Map<String, Object>> list, WriteTableBean w) {
        String ns="mapping.WriteMapper.delete";

        for(int x=0;x<=9;x++){
            Set<String> set=new HashSet<>();
            for(Map<String,Object> mm:list){
                String hid = (String) mm.get("hasid");
                if (Integer.parseInt(hid) == x) {
                    set.add((String) mm.get("source"));
                }
            }

            for(String s:set){
                Map<String,Object> mmp=new HashMap<>();
                List<String> ll=new ArrayList<>();
                for(Map<String,Object> mm:list){
                    String hid = (String) mm.get("hasid");
                    if (Integer.parseInt(hid) == x&&s.equals(mm.get("source"))) {
                        if(StringUtils.isNullOrEmpty((String) mm.get("taid"))){
                            ll.add((String) mm.get("only_id"));
                        }else{
                            ns="mapping.WriteMapper.deleteproject";
                            ll.add((String) mm.get("taid"));
                        }

                    }
                }
                mmp.put("tablename", w.getTablename() + x);
                mmp.put("source",s);
                mmp.put("field",ll);
                MybatisUtils.getInstance().delete(ns,mmp);
            }
        }

    }
}
