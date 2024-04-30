package com.example.modules.simulation.utils;

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatientAPIUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    /**
     * @author: 邵梦丽 on 2020/8/26 17:11
     * @param:
     * @return:
     * @Description:定时调用的方法，也是调用外部接口的接口
     */
    public static StringBuffer getUrlRespDetails(String url, Map<String,String> map) throws IOException {
        HttpClient client = HttpClients.createDefault();
        String jsondata = JSONUtils.toJSONString(map);
        HttpPost httpPost = new HttpPost(url);
        StringEntity reqentity = null;
        reqentity = new StringEntity(jsondata);
        reqentity.setContentEncoding("UTF-8");     //定义编码格式为UTF-8
        reqentity.setContentType("application/json");    //发送json数据需要设置contentType
        httpPost.setEntity(reqentity);
        // 设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(20000).setConnectTimeout(20000).build();
        httpPost.setConfig(requestConfig);
        HttpResponse res = client.execute(httpPost);
        StringBuffer response = new StringBuffer(EntityUtils.toString(res.getEntity()));
        return response;
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
}
