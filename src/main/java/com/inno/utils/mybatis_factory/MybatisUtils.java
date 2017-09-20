package com.inno.utils.mybatis_factory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MybatisUtils {
    private static class SqlSessionHolder{
        private static InputStream in;
        private static SqlSession  session;

        static{
            try {
                in=new FileInputStream("/data1/spider/etl/src/main/resources/Mybatis.xml");
                session= new SqlSessionFactoryBuilder().build(in).openSession(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private MybatisUtils(){}
    public static final SqlSession getInstance(){
        return SqlSessionHolder.session;
    }
}
