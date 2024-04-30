package com.example.utils;


import lombok.SneakyThrows;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Toomth
 * @date 2020/12/2 14:45
 * @explain
 */
public class DateUtils {
    public static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    @SneakyThrows
    public static String addDateForOneDay(String today) {
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sj.parse(today);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DATE, 1);
        System.out.println("明天：" + sj.format(calendar.getTime()));
        return sj.format(calendar.getTime());
    }

    @SneakyThrows
    public static String delDateForOneDay(String today) {
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sj.parse(today);
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DATE, -1);
        System.out.println("昨天：" + sj.format(calendar.getTime()));
        return sj.format(calendar.getTime());
    }

    /*
     * 该方法用于比较两个用字符串表示的日期
     * param strDate1,strDate2 要求的格式为'yyyy-mm-dd'
     * 返回值为两个日期相差的天数
     */
    public static int compareDate(String strDate1, String strDate2) {
        int returnVal = 0;

        try {
            Date date1 = getDateFromString(strDate1 + " 00:00:00");
            Date date2 = getDateFromString(strDate2 + " 00:00:00");
            returnVal = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnVal;
    }


    static String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static Date getDateFromString(String s) {
        Date returnDate = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(datetimeFormat);
            returnDate = sdf.parse(s);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }
    //获取当前时间戳
    public static long currentTimestampTime(){
        Date date=new Date();
        return date.getTime();
    }
    public static String DataNow(){
        TimeZone time = TimeZone.getTimeZone("GMT+8"); //设置为东八区
        TimeZone.setDefault(time);// 设置时区
        Calendar calendar = Calendar.getInstance();// 获取实例
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//构造格式化模板
        Date date = calendar.getTime(); //获取Date对象
        String str = new String();
        str = format1.format(date);//对象进行格式化，获取字符串格式的输出
        System.out.println(str);
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        return str;
    }

    public static String upFileToLocal(MultipartFile file, String pathNo, String imageUploadPath) throws Exception{
        File imageUploadPathFile = new File(imageUploadPath);
        if (!imageUploadPathFile.exists()) {
            imageUploadPathFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String now = sdf.format(new Date());
        imageUploadPathFile = new File(imageUploadPath + File.separator + now);
        if (!imageUploadPathFile.exists()) {
            imageUploadPathFile.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        logger.info("开始+++++++++++++++++++++++++++++++++");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String random = uuid + pathNo + DateUtils.DataNow();
        String filePath = MD5util.string2MD5(random) + "." + extension;
        String imageFileTemp=File.separator+ now + File.separator + filePath;
        String imageFile = imageUploadPath + imageFileTemp;
        logger.info("文件地址++++++++++++++++++++++++++++++++" + imageFile);
        if (!new File(imageFile).exists()) {
            FileOutputStream fos = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int c1 = 0;
            InputStream is = file.getInputStream();
            while ((c1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, c1);
            }
            fos.flush();
            fos.close();
        }
        return imageFileTemp;
    }

    //时间戳转成时间（东八）
    public static String getDate(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(timestamp);
        String res = sdf.format(date);
        return res;
    }

    //将日期格式转换成时间戳
    public static Long dateParseToLong(String dataTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Long temp;
        try{
            temp=sdf.parse(dataTime).getTime();
        }catch(Exception e){
            return null;
        }
        return temp;
    }

    public static String getStringParam(Map<String, Object> param,String name){
        Object temp=param.get(name);
        String stringTemp=null;
        if(temp!=null){
            stringTemp=temp.toString();
        }
        return stringTemp;
    }

    public static boolean isLangjiaFormat(StringBuffer detalisResp){
        //处理返回信息
        JSONObject jsonParam= null;
        String msg=null;
        JSONObject response=null;
        String code=null;
        JSONArray details=null;
        JSONArray medicalInfoList=null;
        try {
            jsonParam = new JSONObject(detalisResp.toString());
            response=jsonParam.getJSONObject("Response");
            System.out.println(response.toString());
            msg=response.getString("Msg");
            code=response.getString("Code");
            if(!code.equals("0"))
            {
                return false;
            }else{
                return true;
            }
        }catch(Exception e){
            return false;
        }
    }

    public static boolean isWandaFormat(com.alibaba.fastjson.JSONObject jsonParam){
        String code=jsonParam.getString("code");
        if(!code.equals("0"))
        {
            return false;
        }else{
            return true;
        }
    }
}
