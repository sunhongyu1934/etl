package com.inno.utils.dbcputils;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class dbcpDatasourceFactory implements DataSourceFactory{

    private DataSource dataSource;

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public DataSource getDataSource() {
        InputStream in = null;
        try {
            in = new FileInputStream("/data1/spider/etl/src/main/resources/dbcppro/dbcpconfig.properties");
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
