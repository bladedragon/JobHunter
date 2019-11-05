package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.SecretUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@Slf4j
public class TeaController {

    @PostMapping(value = "/getTeaHome",produces = "application/json;charset=UTF-8")
    public String getTeacher(String teaId){

        return null;
    }

    @PostMapping(value = "modifyTeaInfo",produces ="application/json;charset=UTF-8")
    public String updateInfo(@RequestParam(value = "headImg",required=false) MultipartFile headImg, HttpServletRequest request){
        ServletInputStream is= null;
        String str = null;
        try {
            is = request.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            str =br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject reqMsg = JSONObject.parseObject(str);

        if(null != headImg){
            String originFileName = headImg.getOriginalFilename().toLowerCase();
            String[] strs = originFileName.split("\\.");
            String fileSuffix = strs[1] ;
            String preStr = strs[0];
            for (String suffix : Constant.ALLOW_SUFFIXS) {
                if(fileSuffix.equals(suffix)){
                    SecretUtil.MD5Encode(preStr);

                }
            }
        }
        return null;
    }



    public static void main(String[] args) {
        String  s = "asdasd.sds";
        String[] strs =  s.split("\\.");
        System.out.println(strs[1]);
    }
}
