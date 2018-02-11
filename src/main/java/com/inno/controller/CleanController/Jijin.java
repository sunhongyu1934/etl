package com.inno.controller.CleanController;

import com.inno.service.JigouService;
import com.inno.service.JijinService;
import com.inno.service.ServiceImpl.JigouServiceImpl;
import com.inno.service.ServiceImpl.JijinServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Jijin {
    private static JijinService j=new JijinServiceImpl();
    public static void main(String args[]) throws IOException, InterruptedException {
        Map<String,Object> list=j.find();
        List<Map<String,Object>> clist=j.clean(list);
        List<Map<String,Object>> ll=new ArrayList<>();
        for(int a=0;a<clist.size();a++) {
            ll.add(clist.get(a));
            if(a%10000==0&&a!=0) {
                j.delete(ll);
                System.out.println("清洗完毕，开始入库");
                j.insert(ll);
                System.out.println("处理完毕");
                ll.clear();
            }
        }
        if(ll!=null&&ll.size()>0){
            j.delete(ll);
            System.out.println("清洗完毕，开始入库");
            j.insert(ll);
            System.out.println("处理完毕");
        }
    }
}
