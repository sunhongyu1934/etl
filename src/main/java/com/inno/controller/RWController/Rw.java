package com.inno.controller.RWController;

import com.inno.bean.ReadTableBean;
import com.inno.bean.WriteTableBean;
import com.inno.service.ReadService;
import com.inno.service.ServiceImpl.ReadServiceImpl;
import com.inno.service.ServiceImpl.WriteServiceImpl;
import com.inno.service.WriteService;
import com.inno.utils.ConfigureUtils.company_parse;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.MD5Util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rw {
    private static ReadService re=new ReadServiceImpl();
    private static WriteService wr=new WriteServiceImpl();
    private static Map<String,String> map=new HashMap<>();

    public static void main(String args[]) throws DocumentException, InterruptedException, FileNotFoundException {
        while (true){
            duqu();
            break;
        }
    }

    public synchronized static void getid(){
        SAXReader saxReader=new SAXReader();
        try {
            File file=new File("/data1/etl/src/main/resources/tablepro/id.xml");
            if(file.exists()) {
                Document doc = saxReader.read(new FileInputStream(file));
                Element root=doc.getRootElement();
                List<Element> list=root.elements();
                for(Element e:list){
                    map.put(e.getName(), e.getText());
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void duqu() throws DocumentException, InterruptedException, FileNotFoundException {
        ExecutorService pool= Executors.newFixedThreadPool(3);
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
                            while (true) {
                                getid();
                                if(map!=null&&map.get(r.getTablename() + "_" + s)!=null){
                                    r.setId(map.get(r.getTablename() + "_" + s));
                                }else{
                                    r.setId("0");
                                }
                                System.out.println(r.getDimname()+"   来自源： "+s+"   当前id："+map.get(r.getTablename() + "_" + s));
                                List<Map<String, Object>> list2 = re.find(r, s);
                                if(list2!=null&&list2.size()>1){
                                    jiexi(list2, s, r);
                                }else{
                                    break;
                                }

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
                            if(map.get(r.getCompanyfield())!=null&&Dup.nullor((String) map.get(r.getCompanyfield()))&&((String) map.get(r.getCompanyfield())).length()<20){
                                List<String> lp=new ArrayList<>();
                                for(String ss:r.getProjectfield()){
                                    if(Dup.nullor((String) map.get(ss))) {
                                        lp.add((String) map.get(ss));
                                    }else if(r.getProjectfield().size()==1&&Dup.nullor(r.getProjectfield().get(0))){
                                        lp.add("");
                                    }
                                }

                                map.put("source", source);
                                String[] ids = MD5Util.Onlyid((String) map.get(r.getCompanyfield()), source, lp);
                                map.put("only_id", ids[0]);
                                map.put("hasid", ids[1]);
                                map.put("taid", ids[2]);
                                a++;
                                ll.add(map);
                            }
                            if(a%20000==0&&a!=0){
                                wr.delete(ll,w);
                                wr.insert(ll, w);
                                ll=new ArrayList<>();
                                if(map.get("id")!=null&&Dup.nullor(map.get("id").toString())) {
                                    lo(r.getTablename() + "_" + source, map.get("id").toString());
                                    System.out.println(simpleDateFormat.format(new Date()) + "  write id success id:" + map.get("id") + "  tablename:" + r.getTablename() + "_" + source + "   20000");
                                }
                            }
                            if(a==0&&map.get("id")!=null&&Dup.nullor(map.get("id").toString())){
                                lo(r.getTablename() + "_" + source, map.get("id").toString());
                                System.out.println(simpleDateFormat.format(new Date()) + "  write id success id:"+map.get("id")+"  tablename:"+r.getTablename() + "_" + source+"   0");
                            }
                            //System.out.println(simpleDateFormat.format(new Date()) + "  [ INFO ]  " + r.getDimname() + " 维度 " + source + " 源" + "已写入:" + a + "条");
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println(simpleDateFormat.format(new Date()) + "  [ error ]  " + r.getDimname() + " 维度 " + source + " 源" + "写入失败一条");

                        }
                    }
                    if(ll.size()>0) {
                        wr.delete(ll,w);
                        wr.insert(ll, w);
                        if(ll.get(ll.size()-1).get("id")!=null&&Dup.nullor(ll.get(ll.size()-1).get("id").toString())) {
                            lo(r.getTablename() + "_" + source,ll.get(ll.size() - 1).get("id").toString());
                            System.out.println(simpleDateFormat.format(new Date()) + "  write id success id:"+ll.get(ll.size() - 1).get("id")+"    tablename:"+r.getTablename() + "_" + source+"   end");
                        }
                    }
                }
            }

            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ INFO ]  "+r.getDimname()+" 维度 "+source+" 源"+"写入成功");
        }catch (Exception e){
            e.printStackTrace();
            date=new Date();
            da=simpleDateFormat.format(date);
            System.out.println(da+"  [ error ]  "+r.getDimname()+" 维度 "+source+" 源"+"写入失败，异常信息： "+e.getMessage());
        }
    }

    public static void lo(String ta,String id) throws IOException, DocumentException {
        File file=new File("/data1/etl/src/main/resources/tablepro/id.xml");
        if(file.exists()) {
            SAXReader saxReader=new SAXReader();
            Document doc=saxReader.read(new FileInputStream(file));
            Element root=doc.getRootElement();
            Element tt=root.element(ta);
            if(tt==null){
                tt=root.addElement(ta);
                tt.setText(id);
            }else{
                tt.setText(id);
            }

            OutputFormat xmlFormat = new OutputFormat();
            xmlFormat.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream("/data1/etl/src/main/resources/tablepro/id.xml"),xmlFormat);
            writer.write(doc);
            writer.close();
        }else{
            Document doc = DocumentHelper.createDocument();
            Element root = doc.addElement("root");
            Element t = root.addElement(ta);
            t.setText(id);

            OutputFormat xmlFormat = new OutputFormat();
            xmlFormat.setEncoding("UTF-8");
            // 设置换行
            xmlFormat.setNewlines(true);
            // 生成缩进
            xmlFormat.setIndent(true);
            // 使用4个空格进行缩进, 可以兼容文本编辑器
            xmlFormat.setIndent("    ");

            XMLWriter writer = new XMLWriter(new FileOutputStream("/data1/etl/src/main/resources/tablepro/id.xml"),xmlFormat);
            writer.write(doc);
            writer.close();
        }
    }
}
