package com.inno.controller.CleanController;

import com.inno.service.GudongService;
import com.inno.service.ServiceImpl.GudongServiceImpl;
import com.inno.utils.redisUtils.RedisClu;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Gudong {
    private static GudongService j=new GudongServiceImpl();
    private static RedisClu rd=new RedisClu();
    public static void main(String args[]) throws IOException, InterruptedException, ParseException, SQLException {
        /*int p=0;
        while (true) {
            try {
                List<Map<String, Object>> list = j.find(String.valueOf(p));
                if (list == null || list.size() == 0) {
                    break;
                }
                j.clean(list);
                p = p + 50000;
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

        Set<String> set=rd.getAllset("comp_in");
        for(int a=0;a<=9;a++) {
            try {
                List<String> list = new ArrayList<>();
                for (String s : set) {
                    String has = s.substring(s.length() - 1);
                    if (Integer.parseInt(has) == a) {
                        list.add(s);
                    }
                }
                List<Map<String, Object>> ll = j.findshang(list, String.valueOf(a));
                j.clean(ll);
            }catch (Exception e){

            }
        }
    }
}
