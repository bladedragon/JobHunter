package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.service.StuHomeService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.Map;

@RestController
public class ShowStuController {

    @Autowired
    StuHomeService stuHomeService;

    @PostMapping(value = "/getHome",produces = "application/json;charset=UTF-8")
    public String showStuHome(@RequestBody JSONObject jsonObject){
        String stuId = jsonObject.getString("stuId");
        if(stuId==null || stuId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }
        Map<String,Object> result = stuHomeService.getStuHome(stuId);
        if(result != null){
            return CommonUtil.returnFormat(200,"success",result);
        }
        return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"Unknow Error");
    }
}
