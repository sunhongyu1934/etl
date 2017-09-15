package com.inno.dao.Impl;

import com.inno.bean.TestBean;
import com.inno.dao.TestDao;
import com.inno.utils.mybatis_factory.MybatisUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TestDaoImpl implements TestDao {

    private String ns;
    public TestDaoImpl(){
        this.ns="mapping.TestMapper.getTest";
    }

    @Override
    public List<Map<String,Object>> find(int id) throws IOException {
        return MybatisUtils.getInstance().selectList(ns,id);
    }
}
