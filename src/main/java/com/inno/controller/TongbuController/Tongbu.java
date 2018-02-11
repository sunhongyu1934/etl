package com.inno.controller.TongbuController;

import com.inno.bean.ReadTableBean;
import com.inno.bean.TongbuBean;
import com.inno.bean.WriteTableBean;
import com.inno.service.ServiceImpl.TongbuServiceImpl;
import com.inno.service.TongbuService;
import com.inno.utils.ConfigureUtils.company_parse;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tongbu {
    private static TongbuService t=new TongbuServiceImpl();
    public static void main(String args[]) throws DocumentException, InterruptedException, FileNotFoundException {
        while (true){
            duqu();
            break;
        }
    }



    public static void duqu() throws DocumentException, InterruptedException, FileNotFoundException {
        ExecutorService pool= Executors.newCachedThreadPool();
        Map<String,Object> map=company_parse.getTongbu();
        List<TongbuBean.Read> list= (List<TongbuBean.Read>) map.get("read");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(TongbuBean.Read r:list){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    Date date=new Date();
                    String da=simpleDateFormat.format(date);
                    System.out.println(da+"  [ INFO ]  "+"开始读取维度： "+r.getDimname()+"   来自表： "+r.getTablename());
                    long ti=(date.getTime()/1000)-Long.parseLong(r.getTime());
                    r.setTt(simpleDateFormat.format(new Date(ti*1000)));
                    try {
                        int a=0;
                        while (true) {
                            r.setA(a);
                            List<Map<String, Object>> list2 = t.find(r);
                            if(list2!=null&&list2.size()>0){
                                jiexi(list2, r);
                            }else{
                                break;
                            }
                            a=a+500000;
                        }
                        date = new Date();
                        da = simpleDateFormat.format(date);
                        System.out.println(da + "  [ INFO ]  " + r.getDimname() + " 维度，" + r.getTablename() + " 表" + "读取成功，开始写入总表");
                    } catch (Exception e) {
                        date=new Date();
                        da=simpleDateFormat.format(date);
                        System.out.println(da+"  [ error ]  "+r.getDimname()+" 维度，"+r.getTablename()+" 表"+"读取失败，异常信息： "+e.getMessage());
                    }

                    try {
                        da=simpleDateFormat.format(new Date());
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





    public static void jiexi(List<Map<String,Object>> list,TongbuBean.Read r) throws DocumentException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        String da;
        int a=0;
        try {
            List<TongbuBean.Write> wlist = null;
            try {
                wlist = (List<TongbuBean.Write>) company_parse.getTongbu().get("write");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(wlist!=null) {
                List<Map<String, Object>> ll = new ArrayList<>();
                for (TongbuBean.Write w : wlist) {
                    if(r.getDimname().equals(w.getDimname())){
                        for(Map<String,Object> mm:list){
                            try {
                                ll.add(mm);
                                a++;
                                if (a % 20000 == 0) {
                                    t.delete(ll, w);
                                    t.insert(ll, w);
                                    ll=new ArrayList<>();
                                }
                                System.out.println(simpleDateFormat.format(new Date()) + "  [ INFO ]  " + r.getDimname() + " 维度 " + r.getTablename() + " 表" + "已写入:" + a + "条");
                            }catch (Exception e){
                                e.printStackTrace();
                                System.out.println(simpleDateFormat.format(new Date()) + "  [ INFO ]  " + r.getDimname() + " 维度 " + r.getTablename() + " 表" + "写入失败一条");
                            }
                        }
                        if(ll!=null&&ll.size()>0){
                            t.delete(ll, w);
                            t.insert(ll, w);
                        }
                    }
                }

                date = new Date();
                da = simpleDateFormat.format(date);
                System.out.println(da + "  [ INFO ]  " + r.getDimname() + " 维度 " + r.getTablename() + " 表" + "写入成功");
            }
        }catch (Exception e){
            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ error ]  "+r.getDimname()+" 维度 "+r.getTablename()+" 表"+"写入失败，异常信息： "+e.getMessage());
        }
    }
}
