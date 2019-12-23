package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.service.OrderService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;

import javax.servlet.annotation.MultipartConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    OrderService orderService;

    @PostMapping(value = "/uploadFile",produces = "application/json;charset=UTF-8")
    public String upload(@RequestParam("orderId") String orderId,@RequestParam("fileName") String fileName,@RequestParam(value = "files") List<MultipartFile> files) throws UploadException {

        Map<String,Object> result = new HashMap<>();

        if(null == orderId || orderId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param not match exception");
        }

        int failNum = orderService.uploadFile(files,orderId, Constant.TEA_FILE,fileName);

        if(failNum != 0){
            result.put("orderId",orderId);
            result.put("failNum",failNum);
            return CommonUtil.returnFormat(Constant.UPLOAD_ERROR,"failUpload!",result);
        }

        return CommonUtil.returnFormatSimp(200,"success");
    }
}
