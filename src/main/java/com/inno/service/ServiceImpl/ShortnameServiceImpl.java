package com.inno.service.ServiceImpl;

import com.google.common.primitives.UnsignedLong;
import com.inno.dao.Impl.ShortnameDaoImpl;
import com.inno.dao.ShortnameDao;
import com.inno.service.ShortnameService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class ShortnameServiceImpl implements ShortnameService {
    private static ShortnameDao s=new ShortnameDaoImpl();
    private static List<String> itlist=new ArrayList<>();
    private static List<String> cyblist=new ArrayList<>();
    private static List<String> kelist=new ArrayList<>();
    private static List<String> silist=new ArrayList<>();
    private static List<String> xilist=new ArrayList<>();
    private static int aa=0;

    private static String ta2;

    @Override
    public void upshort(String ta2) throws ParseException {
        this.ta2=ta2;
        String tables[]=new String[]{"dw_online.it_company_pc###company_full_name###sName","spider.cyb_company###full_name###name","spider.36ke_company###com_full_name###pro_name","spider.si_company###quan_cheng###zhong_jian","spider.xiniu_com###com_fullName###item_name"};
        Map<String,Map<String,Integer>> map=new HashMap<>();
        for(int a=0;a<tables.length;a++) {
            Map<String,String> mm=new HashMap<>();
            String quan=tables[a].split("###")[1];
            String jian=tables[a].split("###")[2];
            mm.put("tablename",tables[a].split("###")[0]);
            mm.put("quan",quan);
            mm.put("jian",jian);
            mm.put("tablename2",ta2);
            for (Map<String, Object> m : s.find(mm)){
                String qva=Dup.renull((String) m.get(quan));
                String jva=Dup.renull((String) m.get(jian)).split("\\(")[0].split("/")[0].split("-")[0].split("（")[0];

                if(a==0){
                    itlist.add(jva);
                }else if(a==1){
                    cyblist.add(jva);
                }else if(a==2){
                    kelist.add(jva);
                }else if(a==3){
                    silist.add(jva);
                }else{
                    xilist.add(jva);
                }

                if(map.get(qva)!=null){
                    if(map.get(qva).get(jva)!=null){
                        map.get(qva).put(jva,map.get(qva).get(jva)+1);
                    }else{
                        map.get(qva).put(jva,1);
                    }
                }else{
                    Map<String,Integer> jj=new HashMap<>();
                    jj.put(jva,1);
                    map.put(qva,jj);
                }
            }
        }
        yiceng(map);

    }

    public static void yiceng(Map<String,Map<String,Integer>> map) throws ParseException {
        for(Map.Entry<String,Map<String,Integer>> entry:map.entrySet()){
            String acname = FenciUtils.chuli(entry.getKey().replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
            String compid= UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
            Set<String> set=getMax(entry.getValue());
            Map<String,String> mm=new HashMap<>();
            String jian = null;
            if(set.size()==1){
                for(String j:set){
                    jian=j;
                }
                mm.put("tablename",ta2);
                mm.put("jian","comp_short_name");
                mm.put("jva",jian);
                mm.put("id","comp_id");
                mm.put("comp_id",compid);
                s.update(mm);
            }else if(set.size()>1){
                jian=Chose(set);
                mm.put("tablename",ta2);
                mm.put("jian","comp_short_name");
                mm.put("jva",jian);
                mm.put("id","comp_id");
                mm.put("comp_id",compid);
                s.update(mm);
            }
            aa++;
            System.out.println(aa+"***********************************************************");
        }
    }


    public static Set<String> getMax(Map<String,Integer> map){
        Set<String> set=new HashSet<>();
        List<Integer> list1=new ArrayList<>();
        for(Map.Entry<String,Integer> entry:map.entrySet()){
            list1.add(entry.getValue());
        }
        Collections.sort(list1);
        int max=list1.get(list1.size()-1);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == max) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    public static String Chose(Set<String> set) throws ParseException {
        List<String> itss=new ArrayList<>();
        List<String> cybss=new ArrayList<>();
        List<String> kess=new ArrayList<>();
        List<String> siss=new ArrayList<>();
        List<String> xiss=new ArrayList<>();

        for(String s: set){
            if(itlist.contains(s)){
                itss.add(s);
            }else if(cyblist.contains(s)){
                cybss.add(s);
            }else if(kelist.contains(s)){
                kess.add(s);
            }else if(silist.contains(s)){
                siss.add(s);
            }else{
                xiss.add(s);
            }
        }

        if(itss.size()==1){
            return itss.get(0);
        }else if(itss.size()>1){
            return fintime(itss,"it");
        }else{
            if(siss.size()==1){
                return siss.get(0);
            }else if(siss.size()>1){
                return siss.get(0);
            }else{
                if(cybss.size()==1){
                    return cybss.get(0);
                }else if(cybss.size()>1){
                    return fintime(cybss,"cyb");
                }else{
                    if(xiss.size()==1){
                        return xiss.get(0);
                    }else if(xiss.size()>1){
                        return xiss.get(0);
                    }else{
                        if(kess.size()==1){
                            return kess.get(0);
                        }else if(kess.size()>1){
                            return fintime(kess,"36ke");
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String fintime(List<String> set,String source) throws ParseException {
        Map<String,String> map=new HashMap<>();
        SimpleDateFormat simpleDateFormat;
        Map<String,Long> map1=new HashMap<>();
        if(source.equals("it")){
            simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
            for(String ss:set) {
                map.put("fintime", "financing_time");
                map.put("tablename", "dw_online.it_finacing_pc");
                map.put("jian", "juzi_name");
                map.put("jvas", ss+"%");
                List<Map<String,Object>> list=s.findfin(map);
                List<Long> list1=new ArrayList<>();

                for(Map<String,Object> mm:list){
                    String ftime=((String) mm.get("financing_time")).replaceAll("[^0-9]","");
                    list1.add(simpleDateFormat.parse(ftime).getTime());
                }

                if(list1!=null&&list1.size()>0) {
                    map1.put(ss, Collections.max(list1));
                }
            }
            List<Long> list2=new ArrayList<>();
            if(map1!=null&&map1.size()>0) {
                for (Map.Entry<String, Long> m : map1.entrySet()) {
                    list2.add(m.getValue());
                }
                Long max = Collections.max(list2);
                for (Map.Entry<String, Long> m : map1.entrySet()) {
                    if (m.getValue() == max) {
                        return m.getKey();
                    }
                }
            }else{
                return set.get(set.size()-1);
            }
        }else if(source.equals("cyb")){
            simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
            for(String ss:set) {
                map.put("fintime", "time");
                map.put("tablename", "spider.cyb_finacing");
                map.put("jian", "name");
                map.put("jvas", ss+"%");
                List<Map<String,Object>> list=s.findfin(map);
                List<Long> list1=new ArrayList<>();

                for(Map<String,Object> mm:list){
                    String ftime=((String) mm.get("time")).replaceAll("[^0-9]","");
                    list1.add(simpleDateFormat.parse(ftime).getTime());
                }
                if(list1!=null&&list1.size()>0) {
                    map1.put(ss, Collections.max(list1));
                }
            }
            List<Long> list2=new ArrayList<>();
            if(map1!=null&&map1.size()>0) {
                for (Map.Entry<String, Long> m : map1.entrySet()) {
                    list2.add(m.getValue());
                }
                Long max = Collections.max(list2);
                for (Map.Entry<String, Long> m : map1.entrySet()) {
                    if (m.getValue() == max) {
                        return m.getKey();
                    }
                }
            }else{
                return set.get(set.size()-1);
            }
        }else if(source.equals("36ke")){
            simpleDateFormat=new SimpleDateFormat("yyyyMM");
            for(String ss:set) {
                map.put("fintime", "fin_time");
                map.put("tablename", "spider.36ke_fin");
                map.put("jian", "ke_name");
                map.put("jvas", ss+"%");
                List<Map<String,Object>> list=s.findfin(map);
                List<Long> list1=new ArrayList<>();

                for(Map<String,Object> mm:list){
                    String ftime=((String) mm.get("fin_time")).replaceAll("[^0-9]","");
                    list1.add(simpleDateFormat.parse(ftime).getTime());
                }
                if(list1!=null&&list1.size()>0) {
                    map1.put(ss, Collections.max(list1));
                }
            }
            List<Long> list2=new ArrayList<>();
            if(map1!=null&&map1.size()>0) {
                for (Map.Entry<String, Long> m : map1.entrySet()) {
                    list2.add(m.getValue());
                }
                Long max = Collections.max(list2);
                for (Map.Entry<String, Long> m : map1.entrySet()) {
                    if (m.getValue() == max) {
                        return m.getKey();
                    }
                }
            }else{
                return set.get(set.size()-1);
            }
        }
        return null;
    }
}
