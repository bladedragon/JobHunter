package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.legend.jobhunter.model.DO.ShowTeaDO;
import team.legend.jobhunter.service.ServService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ServiceController {

    @Autowired
    ServService servService;

    @GetMapping(value = "/getInfo/tutor",produces = "application/json;charset=UTF-8")
    public String getTutorInfo(@RequestParam("pagesize") int pagesize, @RequestParam("page") int page){
//        int pagesize = jsonStr.getInteger("pagesize");
//        int page = jsonStr.getInteger("page");

        Map<String,Object> result  = servService.showTutorInfo(pagesize,page);
        if(result.containsKey("empty")){
            return CommonUtil.returnFormatSimp(Constant.EMPTY_CODE,"empty");
        }


        return CommonUtil.returnFormat(200,"success",result);
    }
    @GetMapping(value = "/getInfo/resume",produces = "application/json;charset=UTF-8")
    public String getResumeInfo(@RequestParam("pagesize") int pagesize, @RequestParam("page") int page){
//        int pagesize = jsonStr.getInteger("pagesize");
//        int page = jsonStr.getInteger("page");
        Map<String,Object> result  = servService.showResumeInfo(pagesize,page);
        if(result.containsKey("empty")){
            return CommonUtil.returnFormatSimp(Constant.EMPTY_CODE,"empty");
        }

        return CommonUtil.returnFormat(200,"success",result);
    }
}
