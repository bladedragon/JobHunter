package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import team.legend.jobhunter.service.ServService;

@Slf4j
@Controller
public class ServiceController {

    @Autowired
    ServService servService;

    @PostMapping(value = "/getTutorInfo",produces = "application/json;charset=UTF-8")
    public String getTutorInfo(@RequestBody JSONObject jsonStr){
        int pagesize = jsonStr.getInteger("pagesize");
        int page = jsonStr.getInteger("page");
        servService.showInfo(pagesize,page);
        return null;
    }
}
