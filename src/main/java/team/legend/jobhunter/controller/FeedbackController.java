package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.service.FeedBackService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.Map;

@Slf4j
@RestController
public class FeedbackController {
    @Autowired
    FeedBackService feedBackService;

    @PostMapping(value = "/feedback",produces = "application/json;charset=UTF-8")
    public String feedBack(@RequestParam(value = "pic",required=false) MultipartFile pic, @RequestParam("jsonStr") String jsonStr){
        log.info("feedBack:jsonStr:{}",jsonStr);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        String stuId = jsonObject.getString("stuId");
        if(stuId==null || stuId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }
        String feedBack = jsonObject.getString("feedback");
        String tele = jsonObject.getString("tele");

        int map = feedBackService.feedBack(stuId,feedBack,tele);
        log.info(">> feedback num  = [{}]",map);

        if(map ==0){
            return CommonUtil.returnFormat(200,"success",map);
        }else{
            return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"feedback fail");
        }


    }
}
