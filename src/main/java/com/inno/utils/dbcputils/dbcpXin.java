package com.inno.utils.dbcputils;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class dbcpXin implements DataSourceFactory{

    private DataSource dataSource;

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public DataSource getDataSource() {
        InputStream in = null;
        String linpath="/home/spider/etl/src/main/resources/dbcppro/dbcpconfigxin.properties";
        String winpath="D:\\工作\\代码\\etl\\src/main/resources/dbcppro/dbcpconfigxin.properties";
        String path;
        String os=System.getProperty("os.name");
        if(os.contains("Windows")){
            path=winpath;
        }else{
            path=linpath;
        }
        try {
            in = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        try {
            prop.load(in);
            this.dataSource= BasicDataSourceFactory.createDataSource(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.dataSource;
    }
}
