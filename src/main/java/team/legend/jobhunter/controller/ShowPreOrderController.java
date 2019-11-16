package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.service.ShowPreOrderService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ShowPreOrderController {

    @Autowired
    ShowPreOrderService showPreOrderService;

    @PostMapping(value = "/showPreOrder",produces = "application/json;charset=UTF-8")
    public String showPreOrder(@RequestBody JSONObject jsonObject){

        List<Map<String,Object>> mapList = null;

        String stuId = jsonObject.getString("stuId");
        if(stuId == null || stuId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"stuId is null");
        }

        mapList = showPreOrderService.showPreOrder(stuId);
        if(null == mapList || mapList.isEmpty()){
            return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,"empty");
        }

        return CommonUtil.returnFormat(200,"success",mapList);
    }


}
