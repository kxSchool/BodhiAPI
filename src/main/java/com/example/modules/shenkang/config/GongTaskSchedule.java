package com.example.modules.shenkang.config;

import com.example.modules.shenkang.pojo.Result;
import com.example.modules.shenkang.service.MedicalSimpleInfoService;
import com.example.modules.shenkang.service.WanDaPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Configurable
@RestController
public class GongTaskSchedule {
    private final Logger logger= LoggerFactory.getLogger(getClass());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private WanDaPushService wanDaPushService;

    @Autowired
    MedicalSimpleInfoService medicalSimpleInfoService;

    /**
     * @author: 邵梦丽 on 2020/8/26 17:27
     * @param:
     * @return:
     * @Description:定时推送信息到万达
     */
    @Scheduled(cron ="${shenkang.cronTime.cron3}")
    @GetMapping("/langjiaInterface/Taskspush")
    public void Taskspush(){
        long startTime=System.currentTimeMillis();   //获取开始时间
        logger.info( "Start pushing data: " + LocalDateTime.now());
        LocalDate nowDate = LocalDate.now();
        String updateDateTime=nowDate.format(DATE_TIME_FORMATTER);
        Result result=new Result();
        try{
            result=medicalSimpleInfoService.pushDatesByState();
        }catch(Exception e){
            result.setMsg("未知异常");
            result.setCode(300);
            logger.error("推送数据异常");
        }
        int code=result.getCode();
        if(code!=200){
            logger.error(result.getMsg());
            logger.error("pushDates Program-----------call 'pushDates' failed! updateDateTime is "+ updateDateTime+"!error!"+result.getMsg());
        }else{
            logger.info("pushDates Program"+updateDateTime+" "+result.getMsg());
        }
        long endTime=System.currentTimeMillis();   //获取结束时间
        logger.info("pushDates Program running time:"+(endTime-startTime)+"ms");
    }

    /**
     * @author: 邵梦丽 on 2020/8/26 17:27
     * @param:
     * @return:
     * @Description:定时调用万达token接口,定时器停用
     */
    @Scheduled(cron ="${shenkang.cronTime.cron4}")
    @GetMapping("/langjiaInterface/getToken")
    public void TasksGetToken()
    {
        long startTime=System.currentTimeMillis();   //获取开始时间
        logger.info( "Key acquisition started at：" + LocalDateTime.now());
        LocalDate nowDate = LocalDate.now();
        String updateDateTime=nowDate.format(DATE_TIME_FORMATTER);
        Result result=new Result();
        String temp=null;
        try{
            temp=wanDaPushService.getAccessToken();
            if(temp!=null||temp.trim().length()!=0){
                result.setCode(200);
                result.setMsg("getToken：获取密钥成功");
            }else{
                result.setCode(300);
                result.setMsg("getToken：获取密钥失败");
            }
        }catch(Exception e){
            result.setCode(300);
            result.setMsg("getToken：获取密钥失败");
        }
        int code=result.getCode();
        if(code!=200){
            logger.error(result.getMsg());
            logger.error("IOException:call 'getToken' failed! updateDateTime is "+ updateDateTime+"!error!"+result.getMsg());
        }else{
            logger.info("getToken Program--------"+updateDateTime+" "+result.getMsg());
        }
        long endTime=System.currentTimeMillis();   //获取结束时间
        logger.info("getToken Program running time:"+(endTime-startTime)+"ms");
    }
}
