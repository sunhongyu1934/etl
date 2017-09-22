package com.inno.controller.JgController;

import com.inno.service.JigouService;
import com.inno.service.ServiceImpl.JigouServiceImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JG {
    private static JigouService j=new JigouServiceImpl();
    public static void main(String args[]) throws IOException {
        List<Map<String,Object>> list=j.find();
        List<Map<String,Object>> clist=j.clean(list);
        j.delete(clist);
        j.insert(clist);
    }
}
