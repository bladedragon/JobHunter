package team.legend.jobhunter.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.service.WxPayService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.IPAddrUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@Slf4j
public class WxPayController {
    @Autowired
    WxPayService wxPayService;

    @RequestMapping(value = "/wx/wxPay", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String wxPay(@RequestBody JSONObject reqBody,HttpRequest request){
            //填充的自定义数据，有待完善
            String randomStr = CommonUtil.getRandomStr(32);
            String orderId = reqBody.getString("orderId");
            String userId  = reqBody.getString("stuId");
            String service_id = reqBody.getString("serviceId");
            //body用于填充自定义数据
            //订单数据
            String body = "";
            Order order = reqBody.getObject("order",Order.class);
            //获取客户端的ip地址
            String spbill_create_ip = IPAddrUtil.getIpAddr(request);

            Map<String ,Object> result = wxPayService.wxPay(randomStr,order,body,spbill_create_ip);


            //待优化
            if(result.containsKey("Result_Fail")){
               if(result.get("Result_Fail").equals("NOTENOUGH")){
                    return CommonUtil.returnFormatSimp(Constant.ERROR_PAY_NOTENOUGH, (String) result.get("err_code_des"));
                }
               if(result.get("Result_Fail").equals("ORDERPAID")){
                   return CommonUtil.returnFormatSimp(Constant.ERROR_PAY_ORDERPAID, (String) result.get("err_code_des"));
               }
               if(result.get("Result_Fail").equals("SYSTEMERROR")){
                   return CommonUtil.returnFormatSimp(Constant.ERROR_PAY_SYSTEMERROR, (String) result.get("err_code_des"));
               }
                return CommonUtil.returnFormatSimp(Constant.ERROR_PAY_CODE,(String)result.get("err_code_des"));
            }
            if(result.containsKey("Return_Fail")){
                return CommonUtil.returnFormatSimp(Constant.ERROR_CODE, (String) result.get("Return_Fail"));
            }
            return CommonUtil.returnFormat(200,"SUCCESS",result);


    }

    @PostMapping("/wx/wxNotify")
    public String wxNotify(HttpServletRequest request, HttpServletResponse response){
            wxPayService.wxNotify(response,request);

            return null;
    }
}
