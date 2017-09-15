package com.inno.controller.TestController;

import com.inno.bean.TestBean;
import com.inno.service.ServiceImpl.TestServiceImpl;
import com.inno.service.TestService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Test {
    public static void main(String args[]) throws IOException {
        TestService t=new TestServiceImpl();
        List<Map<String,Object>> tt=t.find(1);
        Map<String,Object> map=tt.get(0);
        for(Map.Entry<String,Object> entry:map.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
