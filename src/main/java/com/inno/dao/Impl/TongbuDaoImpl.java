package com.inno.dao.Impl;

import com.inno.bean.TongbuBean;
import com.inno.bean.WriteTableBean;
import com.inno.dao.TongbuDao;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.mybatis_factory.MybatisXionganUtils;

import java.util.*;

public class TongbuDaoImpl implements TongbuDao {
    @Override
    public List<Map<String, Object>> findTimeList(Map<String,String> map) {
        String ns="mapping.TongbuMapper.getRead";
        return MybatisUtils.getInstance().selectList(ns,map);
    }
    @Override
    public void insertList(List<Map<String, Object>> list, TongbuBean.Write w) {
        String ns="mapping.TongbuMapper.insert"+w.getDimname();
        List<Map<String,Object>> lists;
        lists = Dup.quchong(list, w.getOnid());


        Map<String,Object> mmp=new HashMap<>();
        mmp.put("tablename", w.getTablename());
        mmp.put("field",lists);
        if(lists!=null&&lists.size()>0) {
            MybatisXionganUtils.getInstance().insert(ns, mmp);
        }

    }



    @Override
    public void delete(List<Map<String, Object>> list, TongbuBean.Write w) {
        String ns="mapping.TongbuMapper.delete";

        Map<String,Object> mmp=new HashMap<>();
        List<String> ll=new ArrayList<>();
        boolean bo=false;
        int a=0;
        for(Map<String,Object> mm:list){
            if(Dup.nullor((String) mm.get("tail_id"))){
                bo=true;
                break;
            }
            a++;
            if(a>=500){
                break;
            }
        }
        if(bo){
            for(Map<String,Object> mm:list){
                ll.add((String) mm.get("tail_id"));
            }
        }else{
            for(Map<String,Object> mm:list){
                ll.add((String) mm.get("only_id"));
            }
        }

        mmp.put("onid",w.getOnid());
        mmp.put("tablename", w.getTablename() );
        mmp.put("field",ll);
        if(ll!=null&&ll.size()>0) {
            MybatisXionganUtils.getInstance().delete(ns, mmp);
        }



    }
}
