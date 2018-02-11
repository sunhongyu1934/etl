package com.inno.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dup {

    public static List<Map<String,Object>> quchong(List<Map<String,Object>> list,String qukey){
        List<Map<String,Object>> ll=new ArrayList<>();
        for(Map<String,Object> mm:list){
            String onid= String.valueOf(mm.get(qukey));
            for (int x=0;x<ll.size();x++) {
                String no = String.valueOf(ll.get(x).get(qukey));
                if (onid.equals(no)) {
                    ll.remove(x);
                }
            }

            ll.add(mm);
        }
        return ll;
    }

    public static boolean nullor(String key){
        if(key!=null&&key.replaceAll("\\s","").replace(" ","").length()>0){
            return true;
        }else {
            return false;
        }
    }

    public static String renull(String key){
        if(Dup.nullor(key)) {
            Pattern p = Pattern.compile("[a-zA-z]");
            Matcher m = p.matcher(key);
            if(m.find()){
                return key.trim();
            }else{
                return key.replaceAll("\\s", "").replace(" ", "").trim();
            }
        }else{
            return null;
        }
    }

    public static void mapvaluepaixu(Map<String,Integer> us){
        Set<Map.Entry<String, Integer>> ks = us.entrySet();
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                ks);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                if (o1.getValue() < o2.getValue())
                    return -1;
                else if (o1.getValue() > o2.getValue())
                    return 1;
                return 0;
            }
        });
        System.out.println(list);
    }
}
