package com.inno.controller.CleanController;

import com.inno.service.JigouService;
import com.inno.service.JijinService;
import com.inno.service.ServiceImpl.JigouServiceImpl;
import com.inno.service.ServiceImpl.JijinServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Jijin {
    private static JijinService j=new JijinServiceImpl();
    public static void main(String args[]) throws IOException, InterruptedException {
        Map<String,Object> list=j.find();
        List<Map<String,Object>> clist=j.clean(list);
        j.delete(clist);
        System.out.println("清洗完毕，开始入库");
        j.insert(clist);
        System.out.println("处理完毕");
    }
}
