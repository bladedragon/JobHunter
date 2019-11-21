package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import team.legend.jobhunter.service.EvaluateServiec;

import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;


@RestController
public class EvaluateController {

    @Autowired
    EvaluateServiec evaluateServiec;

    @PostMapping(value = "/evaluate",produces = "application/json;charset=UTF-8")
    public String evaluate(@RequestBody JSONObject jsonObject){
        String stuId = jsonObject.getString("stuId");
        if(stuId==null || stuId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }

        String teaId = jsonObject.getString("teaId");
        String  orderId = jsonObject.getString("orderId");
        String  commment = jsonObject.getString("comment");
        int degree = jsonObject.getInteger("degree");

        int result = evaluateServiec.evaluate(stuId,teaId,orderId,commment,degree);

        if(result == 1){
            return CommonUtil.returnFormat(200,"success",result);
        }else{
            return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"Unknow Error");
        }


    }


}
