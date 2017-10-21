package com.inno.controller.CleanController;

import com.inno.service.JigouService;
import com.inno.service.ServiceImpl.JigouServiceImpl;
import com.inno.service.ServiceImpl.TuichuServiceImpl;
import com.inno.service.TuichuService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Tuichu {
    private static TuichuService j=new TuichuServiceImpl();
    public static void main(String args[]) throws IOException, InterruptedException {
        List<Map<String,Object>> list=j.find();
        List<Map<String,Object>> clist=j.clean(list);
        j.delete(clist);
        System.out.println("开始插入");;
        j.insert(clist);
        System.out.println("处理完毕");
    }
}
