package com.inno.utils.ConfigureUtils;

import com.inno.bean.ReadTableBean;
import com.inno.bean.WriteTableBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class company_parse {
    public static void main(String args[]) throws DocumentException, FileNotFoundException {
        getAllDimensionRead();
    }

    public static List<ReadTableBean> getAllDimensionRead() throws DocumentException, FileNotFoundException {
        SAXReader sax=new SAXReader();
        String linpath="/data1/spider/etl/src/main/resources/tablepro/table.xml";
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

            redt.setDimname(ele.element("dimname").getText());
            redt.setCompanyfield(ele.element("companyfield").getText());
            redt.setTablename(ele.element("tablename").getText());
            redt.setSources(sources);
            redt.setTime(ele.element("time").getText());
            redt.setLoadtime(ele.element("loadtime").getText());
            redt.setSleep(ele.element("sleep").getText());
            redt.setProjectfield(ele.element("projectfield").getText());
            list.add(redt);
        }
        return list;
    }

    public static List<WriteTableBean> getAllDimensionWrite() throws DocumentException, FileNotFoundException {
        SAXReader sax=new SAXReader();
        String linpath="/data1/spider/etl/src/main/resources/tablepro/table.xml";
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


}
