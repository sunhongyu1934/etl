package com.inno.dao.Impl;

import com.inno.dao.ShortnameDao;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.util.List;
import java.util.Map;

public class ShortnameDaoImpl implements ShortnameDao {
    @Override
    public List<Map<String, Object>> find(Map<String,String> map) {
        String ns="mapping.ShortnameMapper.getRead";
        return MybatisUtils.getInstance().selectList(ns,map);
    }

    @Override
    public void update(Map<String, String> map) {
        String ns="mapping.ShortnameMapper.update";
        MybatisUtils.getInstance().update(ns,map);
    }

    @Override
    public List<Map<String,Object>> findfin(Map<String, String> map) {
        String ns="mapping.ShortnameMapper.getFin";
        return MybatisUtils.getInstance().selectList(ns,map);
    }
}
