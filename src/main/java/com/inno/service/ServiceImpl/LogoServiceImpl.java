package com.inno.service.ServiceImpl;

import com.inno.dao.Impl.LogoDaoImpl;
import com.inno.dao.Impl.WebDaoImpl;
import com.inno.dao.LogoDao;
import com.inno.dao.WebDao;
import com.inno.service.LogoService;
import com.inno.service.WebService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.spider.JsoupUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.swing.text.Document;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogoServiceImpl implements LogoService {
    private static LogoDao w=new LogoDaoImpl();
    @Override
    public List<Map<String, Object>> find(String lim) {
        List<Map<String,Object>> list=w.findList(lim);
        return list;
    }

    @Override
    public void clean(List<Map<String, Object>> list) throws IOException {
        List<Map<String,Object>> ll=new ArrayList<>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int a=0;
        int j=0;
        for(Map<String,Object> mm:list) {
            if (mm.get("comp_full_name") != null&&mm.get("web_url")!=null) {
                Map<String, Object> map = new HashMap<>();
                String quan = mm.get("comp_full_name").toString();
                String[] va=web_url(mm.get("web_url").toString(),mm.get("source_y").toString());
                if(va!=null) {
                    String weburl = va[0];
                    String fen = va[1];
                    if (Dup.nullor(weburl)) {
                        map.put("logo_url", weburl);
                        map.put("only_id", mm.get("only_id"));
                        map.put("com_name", quan);
                        map.put("source", mm.get("source_y"));
                        map.put("f_en", fen);

                        ll.add(map);

                        a++;
                    }
                }
                j++;
                System.out.println(simpleDateFormat.format(new Date()) + "  [INFO]  " + "正在清理第： " + j + "   条");
                if (a % 1000 == 0&&ll != null && ll.size() > 0) {
                    w.delete(ll);
                    w.insert(ll);
                    ll = new ArrayList<>();
                }
            }
        }
        if (ll != null && ll.size() > 0) {
            w.delete(ll);
            w.insert(ll);
        }
    }

    public static String[] web_url(String key,String source) throws IOException {
        if(Dup.nullor(key)) {
            Connection.Response doc =null;
            while (true) {
                try {
                    doc = Jsoup.connect(key)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .timeout(3000)
                            .method(Connection.Method.GET)
                            .execute();
                    if (doc != null && doc.body().length() > 44) {
                        break;
                    }
                }catch (Exception e){
                    doc=null;
                    break;
                }
            }

            if(doc!=null){
                if(doc.statusCode()==200||doc.statusCode()==302){
                    return new String[]{doc.url().toString(), String.valueOf(100+Integer.parseInt(yuanfen(source)))};
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    public static String yuanfen(String source){
        if(source.equals("cyb")){
            return "100";
        }else if(source.equals("it")){
            return "80";
        }else if(source.equals("itleida")){
            return "60";
        }else if(source.equals("qcc")){
            return "40";
        }else if(source.equals("tyc")){
            return "30";
        }else if(source.equals("xixiu")){
            return "20";
        }else{
            return "0";
        }
    }
}
