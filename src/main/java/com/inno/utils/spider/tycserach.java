package com.inno.utils.spider;


import com.mysql.cj.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class tycserach {
    public static Proxy proxy;
    final static String ProxyHost = "proxy.abuyun.com";
    final static Integer ProxyPort = 9020;
    final static String ProxyUser="H6STQJ2G9011329D";
    final static String ProxyPass="E946B835EC9D2ED7";

    static{
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ProxyUser, ProxyPass.toCharArray());
            }
        });
      proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyHost, ProxyPort));
    }

    public static boolean serach(String key) throws IOException, InterruptedException {
        Document doc = detailget("http://www.tianyancha.com/search?key=" + URLEncoder.encode(key, "utf-8") + "&checkFrom=searchBox");
        Elements eles = JsoupUtils.getElements(doc, "div.search_result_single.search-2017.pb20.pt20.pl30.pr30");
        if (eles != null) {
            for (Element e : eles) {
                String cname = JsoupUtils.getString(e, "div.col-xs-10.search_repadding2.f18 a", 0).replace(" ", "").trim();
                if(key.equals(cname)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static Document detailget(String url) throws IOException, InterruptedException {
        System.out.println(url);
        Document doc=null;
        while (true) {
            try {
                doc = Jsoup.connect(url.replace("https","http"))
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                        .timeout(5000)
                        .header("Host", "www.tianyancha.com")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .proxy(proxy)
                        .get();
                if (!doc.outerHtml().contains("获取验证码")&&!StringUtils.isNullOrEmpty(doc.outerHtml().replace("<html>", "").replace("<head></head>", "").replace("</body>", "").replace("<body>", "").replace("</html>", "").replace("\n", "").trim())&&!doc.outerHtml().contains("访问拒绝")&&!doc.outerHtml().contains("abuyun")&&!doc.outerHtml().contains("Unauthorized")&&!doc.outerHtml().contains("访问禁止")) {
                    break;
                }
            }catch (Exception e){
                Thread.sleep(500);
                System.out.println("time out detail");
            }
        }
        return doc;
    }
}
