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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rw {
    private static ReadService re=new ReadServiceImpl();
    private static WriteService wr=new WriteServiceImpl();
    public static void main(String args[]) throws DocumentException, InterruptedException {
        while (true){
            duqu();
            break;
        }
    }

    public static void duqu() throws DocumentException, InterruptedException {
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
                        try {
                            List<Map<String, Object>> list2 = re.find(r, s);
                            date=new Date();
                            da=simpleDateFormat.format(date);
                            System.out.println(da+"  [ INFO ]  "+r.getDimname()+" 维度，"+s+" 源"+"读取成功，开始写入总表");
                            jiexi(list2, s, r);
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
        try {
            List<WriteTableBean> wlist = company_parse.getAllDimensionWrite();
            WriteTableBean ww = null;
            for (WriteTableBean w : wlist) {
                if (r.getDimname().equals(w.getDimname())) {
                    ww=w;
                    break;
                }
            }
            for (Map<String, Object> map : list) {
                map.put("source", source);
                map.put("dim", r.getDimname());
                String[] ids = MD5Util.Onlyid((String) map.get(r.getCompanyfield()),source);
                map.put("only_id", ids[0]);
                map.put("hasid", ids[1]);
                wr.delete(map,ww);
                wr.insert(map, ww);
            }
            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ INFO ]  "+"写入成功");
        }catch (Exception e){
            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ error ]  "+"写入失败，异常信息： "+e.getMessage());
        }
    }
}
