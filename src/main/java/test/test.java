package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.bean.ReadTableBean;
import com.inno.dao.WriteDao;
import com.inno.service.ServiceImpl.WriteServiceImpl;
import com.inno.service.WriteService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.spider.JsoupUtils;
import com.mysql.cj.core.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class test {
    public static void main(String args[]) throws IOException {
        String a=null;
        String onid=UnsignedLong.valueOf(getMD5String(a).substring(8, 24), 16).toString();
        System.out.println(onid);

    }
}
