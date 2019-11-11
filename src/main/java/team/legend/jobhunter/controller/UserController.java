package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.legend.jobhunter.exception.AuthorizeErrorException;
import team.legend.jobhunter.exception.ParamErrorException;
import team.legend.jobhunter.model.WXUser;
import team.legend.jobhunter.service.UserService;
import team.legend.jobhunter.utils.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController("/wx")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login",produces = "application/json;charset=UTF-8")
    public String userLogin(@RequestBody JSONObject reqMsg) throws ParamErrorException, AuthorizeErrorException {

        Map<String,Object> data = new HashMap<>();
        String responseMsg = "success";

        if(!reqMsg.containsKey("code")){
            log.warn("login without code");
            throw new ParamErrorException("code参数异常");

        }
        String code = reqMsg.getString("code");
        Map<Integer,String> login_result = userService.login(code);
        if(login_result.containsKey(0)){
            data.put("openid",login_result.get(1));
            data.put("user_id",login_result.get(3));
            data.put("token",login_result.get(6));
            data.put("create_date",login_result.get(4));
            data.put("last_login",login_result.get(5));
            if(login_result.containsKey(10)){
                data.put("isTeacher","true");
            }else{
                data.put("isTeacher","false");
            }
        }else{
            return CommonUtil.returnFormatSimp(Integer.parseInt(login_result.get(-2)), login_result.get(-1));
        }

        if(reqMsg.containsKey("rawData")&&reqMsg.containsKey("signature")){
            log.info(">>log:authorize start");
            Map<String,Object> authorize_result = userService.authorizeData(login_result.get(3),login_result.get(1),reqMsg.getString("rawData"),reqMsg.getString("signature"));
            data.put("wx_user",authorize_result);
        }else{
            Map<String,Object> oldData = userService.getOldUserData(login_result.get(3));
            if(!data.containsKey("empty")){
                data.put("wx_user",oldData);
                responseMsg = "successs and getOldData";
            }
        }

//        //获取敏感信息
//        if(reqMsg.containsKey("encryptedData")&reqMsg.containsKey("iv")){
//            log.info(">>log:authorize start");
//            Map<String,Object> authorize_result = userService.authorizeEncrypted(login_result.get(3),login_result.get(1),reqMsg.getString("encryptedData"),reqMsg.getString("iv"));
//
//            data.put("wx_user",authorize_result);
//            responseMsg = "success and authorized";
//        }else{
//            Map<String,Object> oldData = userService.getOldUserData(login_result.get(3));
//            if(!data.containsKey("empty")){
//                data.put("wx_user",oldData);
//                responseMsg = "successs and getOldData";
//            }
//        }

        return CommonUtil.returnFormat(200,responseMsg,data);
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map1 = new HashMap<>();
        map.put("test1","test1");
        map1.put("map1","map1");
        map1.put("map2",1);
        map.put("map",map1);
        System.out.println(CommonUtil.returnFormat(200,"lalal",map));
    }
}
