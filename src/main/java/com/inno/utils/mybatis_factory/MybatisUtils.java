package com.inno.utils.mybatis_factory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtils {
    private static class SqlSessionHolder{
        private static final  SqlSession  session = new SqlSessionFactoryBuilder().build(SqlSessionHolder.class.getClassLoader().getResourceAsStream("Mybatis.xml")).openSession(true);
    }

    private MybatisUtils(){}
    public static final SqlSession getInstance(){
        return SqlSessionHolder.session;
    }
}
