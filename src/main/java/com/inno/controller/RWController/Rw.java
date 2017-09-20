package com.inno.controller.RWController;

import com.inno.bean.ReadTableBean;
import com.inno.bean.WriteTableBean;
import com.inno.service.ReadService;
import com.inno.service.ServiceImpl.ReadServiceImpl;
import com.inno.service.ServiceImpl.WriteServiceImpl;
import com.inno.service.WriteService;
import com.inno.utils.ConfigureUtils.company_parse;
import com.inno.utils.MD5utils.MD5Util;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rw {
    private static ReadService re=new ReadServiceImpl();
    private static WriteService wr=new WriteServiceImpl();
    public static void main(String args[]) throws DocumentException, InterruptedException, FileNotFoundException {
        while (true){
            duqu();
            break;
        }
    }

    public static void duqu() throws DocumentException, InterruptedException, FileNotFoundException {
        ExecutorService pool= Executors.newCachedThreadPool();
        List<ReadTableBean> list= company_parse.getAllDimensionRead();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(ReadTableBean r:list){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    for (String s : r.getSources()) {
                        Date date=new Date();
                        String da=simpleDateFormat.format(date);
                        System.out.println(da+"  [ INFO ]  "+"开始读取维度： "+r.getDimname()+"   来自源： "+s);
                        long ti=(date.getTime()/1000)-Long.parseLong(r.getTime());
                        r.setTt(simpleDateFormat.format(new Date(ti*1000)));
                        try {
                            int a=0;
                            while (true) {
                                r.setA(a);
                                List<Map<String, Object>> list2 = re.find(r, s);
                                if(list2!=null&&list2.size()>0){
                                    jiexi(list2, s, r);
                                }else{
                                    break;
                                }
                                a=a+500000;
                            }
                            date = new Date();
                            da = simpleDateFormat.format(date);
                            System.out.println(da + "  [ INFO ]  " + r.getDimname() + " 维度，" + s + " 源" + "读取成功，开始写入总表");
                        } catch (Exception e) {
                            date=new Date();
                            da=simpleDateFormat.format(date);
                            System.out.println(da+"  [ error ]  "+r.getDimname()+" 维度，"+s+" 源"+"读取失败，异常信息： "+e.getMessage());
                        }
                    }
                    try {
                        Date date=new Date();
                        String da=simpleDateFormat.format(date);
                        System.out.println(da+"  [ INFO ]  "+r.getDimname()+"维度处理完毕，开始睡眠，睡眠时间： "+Long.parseLong(r.getSleep())+"秒");
                        Thread.sleep(Long.parseLong(r.getSleep())*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    public static void jiexi(List<Map<String,Object>> list,String source,ReadTableBean r) throws DocumentException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        String da;
        int a=0;
        try {
            List<WriteTableBean> wlist = company_parse.getAllDimensionWrite();
            List<Map<String,Object>> ll=new ArrayList<>();
            for (WriteTableBean w : wlist) {
                if (r.getDimname().equals(w.getDimname())) {
                    for (Map<String, Object> map : list) {
                        try {
                            map.put("source", source);
                            String[] ids = MD5Util.Onlyid((String) map.get(r.getCompanyfield()), source,(String) map.get(r.getProjectfield()));
                            map.put("only_id", ids[0]);
                            map.put("hasid", ids[1]);
                            map.put("taid",ids[2]);
                            a++;
                            ll.add(map);
                            if(a%20000==0){
                                wr.delete(ll,w);
                                wr.insert(ll, w);
                                ll=new ArrayList<>();
                            }
                            System.out.println(simpleDateFormat.format(new Date()) + "  [ INFO ]  " + r.getDimname() + " 维度 " + source + " 源" + "已写入:" + a + "条");
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println(simpleDateFormat.format(new Date()) + "  [ INFO ]  " + r.getDimname() + " 维度 " + source + " 源" + "写入失败一条");

                        }
                    }
                    if(ll.size()>0) {
                        wr.delete(ll,w);
                        wr.insert(ll, w);
                    }
                }
            }

            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ INFO ]  "+r.getDimname()+" 维度 "+source+" 源"+"写入成功");
        }catch (Exception e){
            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ error ]  "+r.getDimname()+" 维度 "+source+" 源"+"写入失败，异常信息： "+e.getMessage());
        }
    }
}
