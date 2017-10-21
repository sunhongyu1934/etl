package com.inno.utils.ConfigureUtils;

import com.inno.bean.ReadTableBean;
import com.inno.bean.TongbuBean;
import com.inno.bean.WriteTableBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class company_parse {
    public static void main(String args[]) throws DocumentException, FileNotFoundException {
        getAllDimensionRead();
    }

    public static List<ReadTableBean> getAllDimensionRead() throws DocumentException, FileNotFoundException {
        SAXReader sax=new SAXReader();
        String linpath="/home/spider/etl/src/main/resources/tablepro/table.xml";
        String winpath="D:\\工作\\代码\\etl\\src/main/resources/tablepro/table.xml";
        String path;
        String os=System.getProperty("os.name");
        if(os.contains("Windows")){
            path=winpath;
        }else{
            path=linpath;
        }
        Document doc=sax.read(new FileInputStream(path));
        Element root=doc.getRootElement();
        Element read=root.element("read");
        List<Element> dims=read.elements("dimension");

        List<ReadTableBean> list=new ArrayList<>();
        for(Element ele:dims){
            ReadTableBean redt=new ReadTableBean();

            List<String> sources=new ArrayList<>();
            for(Element e:(List<Element>)ele.element("sources").elements()){
                sources.add(e.getText());
            }

            List<String> pros=new ArrayList<>();
            for(Element e: (List<Element>)ele.element("projectfields").elements()){
                pros.add(e.getText());
            }

            redt.setDimname(ele.element("dimname").getText());
            redt.setCompanyfield(ele.element("companyfield").getText());
            redt.setTablename(ele.element("tablename").getText());
            redt.setSources(sources);
            redt.setTime(ele.element("time").getText());
            redt.setLoadtime(ele.element("loadtime").getText());
            redt.setSleep(ele.element("sleep").getText());
            redt.setProjectfield(pros);
            list.add(redt);
        }
        return list;
    }

    public static List<WriteTableBean> getAllDimensionWrite() throws DocumentException, FileNotFoundException {
        SAXReader sax=new SAXReader();
        String linpath="/home/spider/etl/src/main/resources/tablepro/table.xml";
        String winpath="D:\\工作\\代码\\etl\\src/main/resources/tablepro/table.xml";
        String path;
        String os=System.getProperty("os.name");
        if(os.contains("Windows")){
            path=winpath;
        }else{
            path=linpath;
        }
        Document doc=sax.read(new FileInputStream(path));
        Element root=doc.getRootElement();
        Element write=root.element("write");
        List<Element> dims=write.elements("dimension");

        List<WriteTableBean> list=new ArrayList<>();
        for(Element ele:dims){
            WriteTableBean redt=new WriteTableBean();

            redt.setDimname(ele.element("dimname").getText());
            redt.setTablename(ele.element("tablename").getText());
            list.add(redt);
        }
        return list;
    }


    public static Map<String,Object> getTongbu() throws DocumentException, FileNotFoundException {
        SAXReader sax=new SAXReader();
        String linpath="/data1/spider/etl/src/main/resources/tablepro/tongbutable.xml";
        String winpath="D:\\工作\\代码\\etl\\src/main/resources/tablepro/tongbutable.xml";
        String path;
        String os=System.getProperty("os.name");
        if(os.contains("Windows")){
            path=winpath;
        }else{
            path=linpath;
        }
        Map<String,Object> map=new HashMap<>();
        Document doc=sax.read(new FileInputStream(path));
        Element root=doc.getRootElement();
        Element read=root.element("read");
        List<Element> dims=read.elements("dimension");

        List<TongbuBean.Read> list=new ArrayList<>();
        for(Element ele:dims){
            TongbuBean.Read redt=new TongbuBean.Read();
            redt.setDimname(ele.element("dimname").getText());
            redt.setTablename(ele.element("tablename").getText());
            redt.setLoadtime(ele.element("loadtime").getText());
            redt.setSleep(ele.element("sleep").getText());
            redt.setTime(ele.element("time").getText());
            list.add(redt);
        }

        Element write=root.element("write");
        List<Element> dimss=write.elements("dimension");
        List<TongbuBean.Write> list2=new ArrayList<>();
        for(Element ele:dimss){
            TongbuBean.Write redt=new TongbuBean.Write();
            redt.setDimname(ele.element("dimname").getText());
            redt.setTablename(ele.element("tablename").getText());
            redt.setOnid(ele.element("onid").getText());
            list2.add(redt);
        }

        map.put("read",list);
        map.put("write",list2);

        return map;
    }


}
