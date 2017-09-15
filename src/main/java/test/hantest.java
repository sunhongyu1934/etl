package test;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

public class hantest {
    public static void main(String args[]){
        System.out.println("标准分词：");
        System.out.println(HanLP.segment("巴彦淖尔市因果树网络科技有限公司"));
        System.out.println("\n");
        CustomDictionary.insert("北京", "ns 1024");

        Segment seg=HanLP.newSegment().enablePlaceRecognize(true);
        List<Term> list=seg.seg("北京");
        for(Term t:list){
            System.out.println(t.toString());
        }
    }


}