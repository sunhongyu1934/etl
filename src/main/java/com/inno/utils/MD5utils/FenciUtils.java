package com.inno.utils.MD5utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.io.StringReader;
import java.util.List;

public class FenciUtils {
    private static Segment seg= HanLP.newSegment().enablePlaceRecognize(true);

    static{
        List<String> list= MybatisUtils.getInstance().selectList("mapping.OnlyidMapper.selectadd");
        for(String s:list){
            CustomDictionary.insert(s, "ns 1024");
        }
    }

    public static String chuli(String cname){
        List<Term> list=seg.seg(cname);
        for(Term t:list){
            String ci=t.toString();
            if(t.toString().split("/")[1].equals("ns")){
                cname=t.word+cname.replace(t.word,"");
            }
        }
        if(cname.contains("中国")){
            cname="中国"+cname.replace("中国","");
        }
        String cc=cname;
        return cc;

    }

}
