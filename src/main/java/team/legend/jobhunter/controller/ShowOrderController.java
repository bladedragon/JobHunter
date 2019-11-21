package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.service.ShowOrderService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.List;
import java.util.Map;

@RestController
public class ShowOrderController {

    @Autowired
    ShowOrderService showOrderService;

    @PostMapping(value = "/serving",produces = "application/json;charset=UTF-8")
    public String showServingOrder(@RequestBody JSONObject jsonObject){

        String userId = jsonObject.getString("userId");
        int isTea = jsonObject.getInteger("isTea");
        if(userId == null || userId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"stuId is null");
        }
        int page = jsonObject.getInteger("page");
        int pagesize = jsonObject.getInteger("pagesize");

        Map<String,Object> mapList = showOrderService.showOrder(userId,isTea,0, page,pagesize);
        if(mapList ==null){
            return CommonUtil.returnFormatSimp(Constant.EMPTY_CODE,"cannot find orders");
        }
        return CommonUtil.returnFormat(200,"success",mapList);
    }

    @PostMapping(value = "/accomplish",produces = "application/json;charset=UTF-8")
    public String showAccomplishOrder(@RequestBody JSONObject jsonObject){
        String userId = jsonObject.getString("userId");
        int isTea = jsonObject.getInteger("isTea");
        if(userId == null || userId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"stuId is null");
        }
        int page = jsonObject.getInteger("page");
        int pagesize = jsonObject.getInteger("pagesize");
        Map<String,Object> mapList = showOrderService.showOrder(userId,isTea,1,page,pagesize);
        if(mapList ==null){
            return CommonUtil.returnFormatSimp(Constant.EMPTY_CODE,"cannot find orders");
        }
        return CommonUtil.returnFormat(200,"success",mapList);

    }

}
