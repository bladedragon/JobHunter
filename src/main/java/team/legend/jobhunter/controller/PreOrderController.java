package team.legend.jobhunter.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.annotation.AvoidDuplicate;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.service.PreOrderService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.RedisLockHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class PreOrderController {


    @Autowired
    PreOrderService preOrderService;
    @Autowired
    RedisLockHelper redisLockHelper;

    @PostMapping(value = "/savePreOrder",produces = "application/json;charset=UTF-8")
    public String CreatePreOrder(@RequestParam("jsonStr") String jsonStr, @RequestParam(value = "files",required = false) List<MultipartFile> files) throws SqlErrorException, UploadException {

        long  now = System.currentTimeMillis();
        log.error("---w------------------{}-----------------------",System.currentTimeMillis()-now);

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if(null ==jsonObject || jsonObject.isEmpty()){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param not match exception");
        }

        int failNum = 0;
        String stuId = jsonObject.getString("stuId");
        String teaId = jsonObject.getString("teaId");
        log.error("----w---------l--------{}-----------------------",System.currentTimeMillis()-now);
        //上锁
        String lockTimestamp = String.valueOf(System.currentTimeMillis()+ Constant.LOCK_EXPIRE_TIME);
        String lock_id = stuId+"."+teaId;
        if(!redisLockHelper.lock(lock_id,lockTimestamp)){
            return CommonUtil.returnFormatSimp(Constant.REPEAT_CODE,"request too quick");
        }
        Map<String, String> map = preOrderService.createPreOrder(jsonObject);
        redisLockHelper.unlock(lock_id,lockTimestamp);
        log.error("-----w------l----------{}-----------------------",System.currentTimeMillis()-now);
        //判断订单是否超时
        if(map.containsKey("overTime")){
            return  CommonUtil.returnFormatSimp(Constant.ERROR_ORDER_OVERTIME,"preOrder is overTime");
        }

        String preOrderId = map.get("preOrderId");

        log.error("-----w----f------------{}-----------------------",System.currentTimeMillis()-now);
        if(jsonObject.getInteger("isUploadFile") ==1 && files != null ){
            failNum = preOrderService.uploadFile(files,preOrderId);
        }
        log.error("-----w-----f-----------{}-----------------------",System.currentTimeMillis()-now);
        int code = Integer.valueOf(map.get("code"));

        Map<String,Object>  result = new HashMap<>(3);
        result.put("stuId",stuId);
        result.put("preOrderId",map.get("preOrderId"));
        if(failNum != 0){
            result.put("failNum",failNum);
            return CommonUtil.returnFormat(Constant.FAIL_UPLOAD,"success but failUpload",result);
        }

        return CommonUtil.returnFormat(code,"success",result);
    }

    @PostMapping(value = "/cancelPreOrder",produces = "application/json;charset=UTF-8")
    public String deletePreOrder(@RequestBody JSONObject jsonObject){
        String preOrderId = jsonObject.getString("preOrderId");
        if(preOrderId==null || preOrderId.equals("")){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param not match exception");
        }
        String stuId = jsonObject.getString("stuId");
        int num = preOrderService.cancelPreOrder(stuId,preOrderId);
        switch(num){
            case 1: return CommonUtil.returnFormatSimp(200,"success cancel");
            case 2: return CommonUtil.returnFormatSimp(Constant.INFO_INVALID_CODE,"preOrder is not exist");
            case 0: return CommonUtil.returnFormatSimp(Constant.EMPTY_CODE," cancel fail");
            default:
                log.error("Unknow Error which return code =[{}]",num);
                return CommonUtil.returnFormatSimp(Constant.ERROR_CODE,"Unknow Error");
        }
    }
}
