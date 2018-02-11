package com.inno.utils.mybatis_factory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MybatisXionganUtils {
    private static class SqlSessionHolder{
        private static InputStream in;
        private static SqlSession  session;

        static{
            try {
                String linpath="/data1/etl/src/main/resources/MybatisXiongan.xml";
                String winpath="D:\\工作\\代码\\etl\\src/main/resources/MybatisXiongan.xml";
                String path;
                String os=System.getProperty("os.name");
                if(os.contains("Windows")){
                    path=winpath;
                }else{
                    path=linpath;
                }
                in=new FileInputStream(path);
                session= new SqlSessionFactoryBuilder().build(in).openSession(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private MybatisXionganUtils(){}
    public static final SqlSession getInstance(){
        return SqlSessionHolder.session;
    }
}
