package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class test {
    public static void main(String args[]) throws IOException, InterruptedException, ParseException, DocumentException {
        List<String> list=new ArrayList<>();
        list.add("美团租车");


        String acname = FenciUtils.chuli("上海路团科技有限公司");
        System.out.println(UnsignedLong.valueOf(getMD5String(acname+list.toString()).substring(8, 24), 16).toString());


    }
}
