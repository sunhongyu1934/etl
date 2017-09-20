package com.inno.utils.spider;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Administrator on 2017/5/27.
 */
public class JsoupUtils {
    public static String getString(Document doc, String select, int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getString(Element doc, String select, int a){
        String key="";
        try{
            key=doc.select(select).get(a).text();
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static String getHref(Document doc, String select, String href, int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }

    public static String getHref(Element doc, String select, String href, int a){
        String key="";
        try{
            key=doc.select(select).get(a).attr(href);
        }catch (Exception e){
            key="";
        }
        return key;
    }


    public static Elements getElements(Document doc, String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Element getElement(Document doc, String select, int a){
        Element ele=null;
        try{
            ele=doc.select(select).get(a);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Element getElement(Element doc, String select, int a){
        Element ele=null;
        try{
            ele=doc.select(select).get(a);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc, String select){
        Elements ele=null;
        try{
            ele=doc.select(select);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }

    public static Elements getElements(Element doc, String select, int a, String select2){
        Elements ele=null;
        try{
            ele=doc.select(select).get(a).select(select2);
        }catch (Exception e){
            ele=null;
        }
        return ele;
    }
}
