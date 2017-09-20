package com.inno.utils.MD5utils;

import com.google.common.primitives.UnsignedLong;
import com.inno.bean.OnlyidBean;
import com.inno.service.OnlyIdService;
import com.inno.service.ServiceImpl.OnlyIdServiceImpl;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.redisUtils.RedisAction;
import com.inno.utils.spider.tycserach;
import com.mysql.cj.core.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/9.
 */
public class MD5Util {
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;
    private static OnlyIdService onn;
    private static RedisAction rd;


    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
            onn=new OnlyIdServiceImpl();
            rd=new RedisAction("10.44.51.90",6379);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5FileUtil messagedigest初始化失败");
        }
    }

    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    public static String fileMD5(String inputFile) throws IOException {



        // 缓冲区大小（这个可以抽出一个参数）

        int bufferSize = 256 * 1024;


        FileInputStream fileInputStream = null;

        DigestInputStream digestInputStream = null;



        try {

            // 拿到一个MD5转换器（同样，这里可以换成SHA1）

            MessageDigest messageDigest =MessageDigest.getInstance("MD5");



            // 使用DigestInputStream

            fileInputStream = new FileInputStream(inputFile);

            digestInputStream = new DigestInputStream(fileInputStream,messageDigest);



            // read的过程中进行MD5处理，直到读完文件

            byte[] buffer =new byte[bufferSize];

            while (digestInputStream.read(buffer) > 0);



            // 获取最终的MessageDigest

            messageDigest= digestInputStream.getMessageDigest();



            // 拿到结果，也是字节数组，包含16个元素

            byte[] resultByteArray = messageDigest.digest();



            // 同样，把字节数组转换成字符串

            return bufferToHex(resultByteArray);



        } catch (NoSuchAlgorithmException e) {

            return null;

        } finally {

            try {

                digestInputStream.close();

            } catch (Exception e) {

            }

            try {

                fileInputStream.close();

            } catch (Exception e) {

            }

        }

    }

    public static long parseMd5L16ToLong(String md5L16){
        if (md5L16 == null) {
            throw new NumberFormatException("null");
        }
        md5L16 = md5L16.toLowerCase();
        byte[] bA = md5L16.getBytes();
        long re = 0L;
        for (int i = 0; i < bA.length; i++) {
            //加下一位的字符时，先将前面字符计算的结果左移4位
            re <<= 4;
            //0-9数组
            byte b = (byte) (bA[i] - 48);
            //A-F字母
            if (b > 9) {
                b = (byte) (b - 39);
            }
            //非16进制的字符
            if (b > 15 || b < 0) {
                throw new NumberFormatException("For input string '" + md5L16);
            }
            re += b;
        }
        return re;
    }

    public static long parseString16ToLong(String str16){
        if (str16 == null) {
            throw new NumberFormatException("null");
        }
        //先转化为小写
        str16 = str16.toLowerCase();
        //如果字符串以0x开头，去掉0x
        str16 = str16.startsWith("0x") ? str16.substring(2) : str16;
        if (str16.length() > 16) {
            throw new NumberFormatException("For input string '" + str16 + "' is to long");
        }
        return parseMd5L16ToLong(str16);
    }

    public static synchronized String[] Onlyid(String cname,String source,String xiangmu) throws IOException, InterruptedException {
        String acname=FenciUtils.chuli(cname.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","")).replace(" ","");

        String onid;
        String hasid;
        String md5;
        String taid;
        if(!StringUtils.isNullOrEmpty(xiangmu)){
            md5 = getMD5String(acname+xiangmu).substring(8, 24);
            taid=UnsignedLong.valueOf(md5, 16).toString();
            onid=UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
            hasid=onid.substring(onid.length()-1);
        }else{
            md5 = getMD5String(acname).substring(8, 24);
            onid=UnsignedLong.valueOf(md5, 16).toString();
            hasid=onid.substring(onid.length()-1);
            taid="";
        }


        boolean bo;
        while (true) {
            try {
                bo = rd.get("zhuce", onid);
                break;
            }catch (Exception e){
                Thread.sleep(500);
            }
        }
        if(!bo) {
            if(source.equals("tyc")){
                Map<String,String> map=new HashMap<>();
                map.put("be_company_name",cname);
                map.put("af_company_name",acname);
                map.put("only_id",onid);
                map.put("hash_id",hasid);
                map.put("register_or","0");
                onn.insert(map);
                while (true) {
                    try {
                        rd.set("zhuce", onid);
                        break;
                    }catch (Exception ee){
                        Thread.sleep(500);
                    }
                }
                return new String[]{onid,hasid,taid};
            }else{
                Map<String, String> map = new HashMap<>();
                map.put("be_company_name", cname);
                map.put("af_company_name", acname);
                map.put("only_id", onid);
                map.put("hash_id", hasid);
                map.put("register_or", "1");
                onn.insert(map);
                return new String[]{onid,hasid,taid};
            }/*else {
                boolean bb = tycserach.serach(cname);
                if (bb) {
                    String md5 = getMD5String(acname).substring(8, 24);
                    onid = UnsignedLong.valueOf(md5, 16).toString();
                    hasid = onid.substring(onid.length() - 1);
                    Map<String, String> map = new HashMap<>();
                    map.put("be_company_name", cname);
                    map.put("af_company_name", acname);
                    map.put("only_id", onid);
                    map.put("hash_id", hasid);
                    map.put("register_or", "0");
                    onn.insert(map);
                    while (true) {
                        try {
                            rd.set("zhuce", cname);
                            break;
                        }catch (Exception ee){
                            Thread.sleep(500);
                        }
                    }
                } else {
                    String md5 = getMD5String(acname).substring(8, 24);
                    onid = UnsignedLong.valueOf(md5, 16).toString();
                    hasid = onid.substring(onid.length() - 1);
                    Map<String, String> map = new HashMap<>();
                    map.put("be_company_name", cname);
                    map.put("af_company_name", acname);
                    map.put("only_id", onid);
                    map.put("hash_id", hasid);
                    map.put("register_or", "1");
                    onn.insert(map);
                }
            }*/
        }else{
            Map<String,String> map=new HashMap<>();
            map.put("be_company_name",cname);
            map.put("af_company_name",acname);
            map.put("only_id",onid);
            map.put("hash_id",hasid);
            return new String[]{onid,hasid,taid};
        }
    }

    public static String Hasid(String lo){
        return String.valueOf(lo.hashCode()).substring(-1);
    }

}
