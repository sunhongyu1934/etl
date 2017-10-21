package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.bean.ReadTableBean;
import com.inno.dao.WriteDao;
import com.inno.service.ServiceImpl.WriteServiceImpl;
import com.inno.service.WriteService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.redisUtils.RedisAction;
import com.inno.utils.spider.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class test {
    public static void main(String args[]) throws IOException, InterruptedException {
        List<String> list=new ArrayList<>();
        list.add("深圳比亚迪戴姆勒新技术有限公司");
        list.add("湖南长丰汽车制造股份有限公司");
        list.add("华菱星马汽车(集团)股份有限公司");
        list.add("力帆实业(集团)股份有限公司");
        list.add("浙江雷诺尔电气有限公司");
        list.add("湖南江南红箭股份有限公司");
        list.add("山东时风集团有限责任公司");
        list.add("特斯拉汽车销售服务（北京）有限公司");
        list.add("行之有道汽车服务股份有限公司");
        list.add("湖南省百胜信息科技有限公司");
        list.add("深圳市天捷恒大科技发展有限公司");
        list.add("陕西易开汽车租赁有限公司");
        list.add("北京聚能优创智能科技有限公司");
        list.add("艾特国际控股有限公司");
        list.add("深圳市金强力电池科技有限公司");
        list.add("苏州市正祥新能源有限公司");
        list.add("湖南星城石墨科技股份有限公司");
        list.add("中航太克(厦门)电力技术股份有限公司");
        list.add("徐州金泉电子科技有限公司");
        list.add("武汉志实科技有限责任公司");
        list.add("武汉市交通物流能源服务有限公司");
        list.add("厦门市今点通科技发展有限公司");
        list.add("湖南蓝眼科技发展有限公司");
        list.add("河南省天星科技有限公司");
        list.add("河北迈博威电子科技有限公司");

        for(String s:list) {
            System.out.println(UnsignedLong.valueOf(getMD5String(s).substring(8, 24), 16).toString());
        }
        String[] tt="aaa".split(",");
        for(String s:tt){
            System.out.println(s);
        }
    }
}
