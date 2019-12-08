package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.service.OfferService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.Map;

@RestController
public class ShowOfferController {

    @Autowired
    OfferService offerService;

    @GetMapping(value = "/getOfferHome",produces = "application/json;charset=UTF-8")
    public String getOfferList(@RequestBody JSONObject jsonObject){
        return null;
    }

    @PostMapping(value = "/getdetail",produces = "application/json;charset=UTF-8")
    public String evaluate(@RequestBody JSONObject jsonObject){
        String offerId = jsonObject.getString("offerId");
        if(offerId==null || offerId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }

        String stuId = jsonObject.getString("stuId");
        Map<String,Object> result = offerService.getDetail(offerId,stuId);

        if(result == null || result.isEmpty()){
            return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,"offer is not exist");

        }else{

        }
        return CommonUtil.returnFormat(200,"success",result);

    }
}
