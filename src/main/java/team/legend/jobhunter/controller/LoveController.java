package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.service.LoveService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoveController {

    @Autowired
    LoveService loveService;

    @PostMapping(value = "/love",produces = "application/json;charset=UTF-8")
    public String love(@RequestBody JSONObject jsonObject) throws SqlErrorException {
        String stuId = jsonObject.getString("stuId");
        if(stuId==null || stuId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }
        Map<String,Integer> data = new HashMap<>(1);
        String offerId = jsonObject.getString("offerId");

        log.info("stuId={}, offerId={}",stuId,offerId);
        int num = loveService.love(stuId,offerId);
        if(num != 0 && num != 1){
            return  CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"Unknow Error");
        }
        data.put("isLove",num);
        return CommonUtil.returnFormat(200,"success",data);

    }

    @PostMapping(value = "/getLove",produces = "application/json;charset=UTF-8")
    public String getLove(@RequestBody JSONObject jsonObject){
        String stuId = jsonObject.getString("stuId");
        if(stuId==null || stuId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }
        int page = jsonObject.getInteger("page");
        int pagesize = jsonObject.getInteger("pagesize");

        Map<String,Object> map = loveService.getLove(stuId,page,pagesize);

        if(map == null){
            return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,"empty");
        }
        return CommonUtil.returnFormat(200,"success",map);
    }


}
