package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.service.ConfirmService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.RedisLockHelper;

import javax.swing.*;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
public class ConfirmController {

    @Autowired
    ConfirmService confirmService;
    @Autowired
    RedisLockHelper redisLockHelper;

    @PostMapping(value = "/fillTimeLocation",produces = "application/json;charset=UTF-8")
    public String teaFill(@RequestBody JSONObject jsonObject) throws SqlErrorException {
        String orderId = jsonObject.getString("orderId");
        if(orderId==null || orderId.equals("")){
            log.error(">>log:param error :orderId is null");
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is null or invalid");
        }
        String location = jsonObject.getString("location");
        String time = jsonObject.getString("time");
        String teaId = jsonObject.getString("teaId");
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            log.error(">>log: teaFill: date parse fail");
            e.printStackTrace();

        }
        Long timestamp = date.getTime();
        Map<String,String> map = confirmService.teaFill(teaId,orderId,location,timestamp);

        if(map.containsKey("success")){
            return CommonUtil.returnFormatSimp(200,"success");
        }else{
            if(map.containsKey("nullError")){
                return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,map.get("nullError"));
            }
            if(map.containsKey("invalid")){
                return CommonUtil.returnFormatSimp(Constant.INFO_INVALID_CODE,map.get("invalid"));
            }
            return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"UnKnow Error");
        }

    }

    @PostMapping(value = "/confirmTime",produces = "application/json;charset=UTF-8")
    public String confirm(@RequestBody JSONObject jsonObject){
        String orderId = jsonObject.getString("orderId");
        if(orderId==null || orderId.equals("")){
            log.error(">>log:param error :orderId is null");
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is null or invalid");
        }
        String stuId = jsonObject.getString("stuId");
        int response = confirmService.confirm(stuId,orderId);
        switch (response){
            case 0:
                return CommonUtil.returnFormatSimp(200,"success");
            case 1:
                return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,"order is null or student not belong it!");
            case 2:
                return CommonUtil.returnFormatSimp(Constant.INFO_NOTFILLED_CODE,"location or tiemstamp is not filled");
            case 3:
                return CommonUtil.returnFormatSimp(Constant.INFO_INVALID_CODE,"commit is invalid");
            default:
                return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"UnKnow Error");

        }

    }

    @PostMapping(value = "/confirm",produces = "application/json;charset=UTF-8")
    public String confirmAccomplish(@RequestBody JSONObject jsonObject) throws SqlErrorException {
        int isTea = 0;
        String orderId = jsonObject.getString("orderId");
        if(orderId==null || orderId.equals("")){
            log.error(">>log:param error :orderId is null");
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is null or invalid");
        }
        String userId = jsonObject.getString("stuId");
        if(userId ==null ||userId.equals("")){
            userId = jsonObject.getString("teaId");
            isTea = 1;
        }

        String lockTimestamp = String.valueOf(System.currentTimeMillis()+ Constant.LOCK_EXPIRE_TIME);
        redisLockHelper.lock(orderId,lockTimestamp);
        Map<String,String> map = confirmService.confirmAccomplish(userId,orderId,isTea);
        redisLockHelper.unlock(orderId,lockTimestamp);

        if(map.containsKey("success")){
            return CommonUtil.returnFormatSimp(200,"success");
        }else{
            if(map.containsKey("error")){
                return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,map.get("error"));
            }
            if(map.containsKey("invalid")){
                return CommonUtil.returnFormatSimp(Constant.INFO_INVALID_CODE,map.get("invalid"));
            }
            return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"UnKnow Error");
        }

    }

}
