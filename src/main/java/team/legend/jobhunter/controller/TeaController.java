package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.ParamErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.Teacher;
import team.legend.jobhunter.service.TeaInfoService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import javax.activation.CommandMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class TeaController {
    @Autowired
    TeaInfoService teaInfoService;


    @PostMapping(value = "/getTeaInfo",produces = "application/json;charset=UTF-8")
    public String getTeacher(@RequestBody JSONObject jsonObject){
        String teaId = jsonObject.getString("teaId");

        if(teaId == null ||teaId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"cannot get teaID");

        }
        Map<String,Object> map = teaInfoService.getTeaInfo(teaId);
        if(map.containsKey("fail")){
            return CommonUtil.returnFormatSimp(Constant.ERROR_Tea_InfoError,"get teaInfo fail");
        }

        return CommonUtil.returnFormat(200,"success",map);
    }



    @PostMapping(value = "/modifyTeaInfo",produces ="application/json;charset=UTF-8")
    public String updateInfo(@RequestParam(value = "headImg",required=false) MultipartFile headImg, @RequestParam("jsonStr") String jsonStr) throws ParamErrorException {
        JSONObject reqMsg = JSONObject.parseObject(jsonStr);

        String teaId = reqMsg.getString("teaId");
        if(teaId ==null){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }

        String imgUrl = teaInfoService.saveImg(teaId,headImg);
        Map<String,Object>  teaInfo = teaInfoService.modifyInfo(reqMsg,imgUrl);

        if(teaInfo !=null){
            return CommonUtil.returnFormat(200,"success",teaInfo);
        }
        return CommonUtil.returnFormatSimp(Constant.ERROR_Tea_InfoError,"cannot get teaInfo");
    }

    @PostMapping(value = "/verify",produces = "application/json;charset=UTF-8")
    public String verifyTea(@RequestBody JSONObject jsonStr) {
            String openid = jsonStr.getString("openid");
            String verifyCode = jsonStr.getString("verifyCode");
            String userId = jsonStr.getString("userId");
            String realname = jsonStr.getString("realName");

        String teaId = null;
        try {
            teaId = teaInfoService.verify(openid,verifyCode,userId,realname);
        } catch (SqlErrorException e) {
            e.printStackTrace();
        }
        if(teaId != null){
                if(teaId.equals("duplication")){
                    return CommonUtil.returnFormatSimp(Constant.ERROR_TEA_VERIFY_DUPLICATION,"verify duplication");
                }
                return CommonUtil.returnFormat(200,"success",teaId);
            }

            return CommonUtil.returnFormatSimp(Constant.ERROR_TEA_VERIFY_FAIL,"verify fail");
    }


    @PostMapping(value = "/getTeaHome",produces = "application/json;charset=UTF-8")
    public String getTeaHome(@RequestBody JSONObject jsonObject){
        String teaId = jsonObject.getString("teaId");
        if(teaId==null || teaId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param is error");
        }
        Map<String,Object> result = teaInfoService.getTeaHome(teaId);
        if(result != null){
            return CommonUtil.returnFormat(200,"success",result);
        }
        return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"Unknow Error");

    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        List<String> offer = new ArrayList<>();
        List<String> tele = new ArrayList<>();
        List<String> server = new ArrayList<>();
        map.put("teaId","123");
        map.put("teaName","jainhlaosh ");
        map.put("nickname","jaing");
        map.put("company","legned");
        map.put("isOnline",0);
        map.put("offer",offer);
        map.put("perDes","asdasd");
        map.put("tele",tele);
        map.put("position","asd");
        map.put("serviceType",server);
        String str = JSON.toJSONString(map);
        System.out.println(str);
        JSONObject jsonObject =  JSONObject.parseObject(str);
    }
}
