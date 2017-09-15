package com.inno.utils.ConfigureUtils;

import com.inno.bean.ReadTableBean;
import com.inno.bean.WriteTableBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.List;

public class company_parse {
    private static SAXReader sax=new SAXReader();
    public static void main(String args[]) throws DocumentException {
        getAllDimensionRead();
    }

    public static List<ReadTableBean> getAllDimensionRead() throws DocumentException {
        Document doc=sax.read(company_parse.class.getClassLoader().getResourceAsStream("tablepro/table.xml"));
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
            list.add(redt);
        }
        return list;
    }

    public static List<WriteTableBean> getAllDimensionWrite() throws DocumentException {
        SAXReader sax=new SAXReader();
        Document doc=sax.read(company_parse.class.getClassLoader().getResourceAsStream("tablepro/table.xml"));
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
