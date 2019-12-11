package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.legend.jobhunter.model.DO.OfferDO;
import team.legend.jobhunter.model.OfferInfo;
import team.legend.jobhunter.service.OfferService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import java.util.List;
import java.util.Map;

@RestController
public class ShowOfferController {

    @Autowired
    OfferService offerService;

    @GetMapping(value = "/getInfo/offers",produces = "application/json;charset=UTF-8")
    public String getOfferList(@RequestParam int pagesize, @RequestParam int page){
        Map<String,Object> offerInfoList =  offerService.getOfferList(page,pagesize);
        if(offerInfoList ==null){
            return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,"get info is null");
        }
        return CommonUtil.returnFormat(200,"success",offerInfoList);
    }

    @PostMapping(value = "/getdetail",produces = "application/json;charset=UTF-8")
    public String getdetail(@RequestBody JSONObject jsonObject){
        String offerId = jsonObject.getString("offerId");
        if(offerId==null || offerId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }

        String stuId = jsonObject.getString("stuId");
        Map<String,Object> result = offerService.getDetail(offerId,stuId);

        if(result == null || result.isEmpty()){
            return CommonUtil.returnFormatSimp(Constant.INFO_EMPTY_CODE,"offer is not exist");

        }
        return CommonUtil.returnFormat(200,"success",result);

    }
}
