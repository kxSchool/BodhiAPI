package com.example.modules.shenkang.utils;

import com.alibaba.fastjson.JSON;
import com.example.modules.shenkang.pojo.LogApiCommon;
import com.example.modules.shenkang.pojo.PatientInfo;
import com.example.modules.shenkang.pojo.WandaApiReq;
import com.example.modules.simulation.controller.SimulationDataController;
import com.example.utils.MD5util;
import com.example.utils.encrypt.DesUtil;
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
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShenkangUtils {
    public static Logger logger = LoggerFactory.getLogger(SimulationDataController.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    /**
     * @author: 邵梦丽 on 2020/11/22 11:22
     * @param:
     * @return:
     * @Description:获取前14天日期
     */
    public static String getDelayFormatday(int dayDelay){
        LocalDate nowDate = LocalDate.now();
        LocalDate yesterday = nowDate.minusDays(dayDelay);
        return yesterday.format(DATE_TIME_FORMATTER_DAY);
    }

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

    public static boolean isTokenFormat(com.alibaba.fastjson.JSONObject jsonObject){
        String tokenJson=com.alibaba.fastjson.JSONObject.toJSONString(jsonObject);
        if(tokenJson.contains("access_token")){
            return true;
        }else{
            return false;
        }
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

    //获取当前时间戳
    public static long currentTimestampTime(){
        Date date=new Date();
        return date.getTime();
    }

    public static int getPageParam(Object no,int size){
        int num;
        if(no==null){
            num=size;
        }else{
            num=new Integer(no.toString());
            if(num<=0){
                num=size;
            }
        }
        return num;
    }

    public static String getStringParam(Map<String, Object> param, String name){
        Object temp=param.get(name);
        String stringTemp=null;
        if(temp!=null){
            stringTemp=temp.toString();
        }
        return stringTemp;
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

    //时间戳转成时间（东八）
    public static String getDate(long timestamp){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(timestamp);
        String res = sdf.format(date);
        return res;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:解密患者信息，用于数据信息校验
     */
    public static PatientInfo descInfo02(PatientInfo patientnfo) throws Exception {
        PatientInfo patientInfo = JSON.parseObject(JSON.toJSON(patientnfo).toString(), PatientInfo.class);

        if (patientInfo.getName() != null && !"".equals(patientInfo.getName())) {
            patientInfo.setName(DesUtil.decrypt(patientInfo.getName()));
        }
        if (patientInfo.getName_spell() != null && !"".equals(patientInfo.getName_spell())) {
            patientInfo.setName_spell(DesUtil.decrypt(patientInfo.getName_spell()));
        }
        if (patientInfo.getMother_name() != null && !"".equals(patientInfo.getMother_name())) {
            patientInfo.setMother_name(DesUtil.decrypt(patientInfo.getMother_name()));
        }
        if (patientInfo.getSex() != null && !"".equals(patientInfo.getSex())) {
            patientInfo.setSex(DesUtil.decrypt(patientInfo.getSex()));
        }

        if (patientInfo.getHealth_card_no() != null && !"".equals(patientInfo.getHealth_card_no())) {
            patientInfo.setHealth_card_no(DesUtil.decrypt(patientInfo.getHealth_card_no()));
        }
        if (patientInfo.getContact_phone_no() != null && !"".equals(patientInfo.getContact_phone_no())) {
            patientInfo.setContact_phone_no(DesUtil.decrypt(patientInfo.getContact_phone_no()));
        }
        if (patientInfo.getAddress_detail() != null && !"".equals(patientInfo.getAddress_detail())) {
            patientInfo.setAddress_detail(DesUtil.decrypt(patientInfo.getAddress_detail()));
        }
        if (patientInfo.getWork_unit() != null && !"".equals(patientInfo.getWork_unit())) {
            patientInfo.setWork_unit(DesUtil.decrypt(patientInfo.getWork_unit()));
        }

        if (patientInfo.getInsurance_id() != null && !"".equals(patientInfo.getInsurance_id())) {
            patientInfo.setInsurance_id(DesUtil.decrypt(patientInfo.getInsurance_id()));
        }

        if (patientInfo.getIdentityID() != null && !"".equals(patientInfo.getIdentityID())) {
            patientInfo.setIdentityID(DesUtil.decrypt(patientInfo.getIdentityID()));
            patientInfo.setId_card_no(patientInfo.getIdentityID());
        }

        return patientInfo;
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

    /**
     * @author: 邵梦丽 on 2020/8/25 15:54
     * @param:
     * @return:
     * @Description:返回格式化的时间
     */
    public static String getTime(){
        LocalDate localDate2;
        LocalTime nowtime2;
        LocalDateTime localDateTime;
        String createDatatime;
        localDate2 = LocalDate.now();
        nowtime2 = LocalTime.now();
        localDateTime = LocalDateTime.of(localDate2,nowtime2);
        createDatatime=localDateTime.format(DATE_TIME_FORMATTER);
        return createDatatime;
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
        String random = uuid + pathNo + dataNow();
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

    public static String dataNow(){
        TimeZone time = TimeZone.getTimeZone("GMT+8"); //设置为东八区
        TimeZone.setDefault(time);// 设置时区
        Calendar calendar = Calendar.getInstance();// 获取实例
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//构造格式化模板
        Date date = calendar.getTime(); //获取Date对象
        String str = new String();
        str = format1.format(date);//对象进行格式化，获取字符串格式的输出
        System.out.println(str);
        return str;
    }

    /**
     * @author: 邵梦丽 on 2020/8/25 17:47
     * @param:
     * @return:
     * @Description:校验日期格式是否符合规定
     */
    public static boolean isRqFormat(String updateDataTime){
        String format = "([0-9]{4})\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(updateDataTime);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
            matcher = pattern.matcher(updateDataTime);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m-1, 1);
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;
    }
}
