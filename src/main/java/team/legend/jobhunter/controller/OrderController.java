package team.legend.jobhunter.controller;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.PreferHeapByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.service.OrderService;
import team.legend.jobhunter.utils.CommonUtil;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.RedisLockHelper;

import javax.management.openmbean.OpenMBeanConstructorInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    RedisLockHelper redisLockHelper;

    @PostMapping(value = "payOrder",produces = "application/json;charset=UTF-8")
    public String createPreOrder(@RequestParam("jsonStr") String jsonStr, @RequestParam(value = "files",required = false) List<MultipartFile> files) throws SqlErrorException, OrderErrorException, UploadException {

        Map<String,Object> map = new HashMap<>(3);
        int num = 0;

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if(null == jsonObject || jsonObject.isEmpty()){
            return CommonUtil.returnFormatSimp(Constant.PARAM_CODE,"param not match exception");
        }
        String teaId = jsonObject.getString("teaId");
        String stuId = jsonObject.getString("stuId");
        String filename = jsonObject.getString("fileName");

        //上锁
        String lockTimestamp = String.valueOf(System.currentTimeMillis()+ Constant.LOCK_EXPIRE_TIME);
        String lock_id = stuId+"."+teaId;
        if(!redisLockHelper.lock(lock_id,lockTimestamp)){
            return CommonUtil.returnFormatSimp(Constant.REPEAT_CODE,"request too quick");
        }


        //可能需要添加preorderId为空的情况
        if(jsonObject.containsKey("preOrderId")){
            String preOrderId = jsonObject.getString("preOrderId");
            map = orderService.createOrder(preOrderId);
        }else{

            String order_type = jsonObject.getString("orderType");
            int price = jsonObject.getInteger("price");
            int discount = jsonObject.getInteger("discount");
            int isonline = jsonObject.getInteger("isonline");
            String experience = jsonObject.getString("experience");
            String requirement = jsonObject.getString("requirement");
            String realName = jsonObject.getString("realName");
            String tele = jsonObject.getString("tele");

            map = orderService.createOrder(teaId,stuId,null,order_type,price,discount,isonline,realName,tele,experience,requirement);
        }
        redisLockHelper.unlock(lock_id,lockTimestamp);

        if(map == null){
            return CommonUtil.returnFormatSimp(Constant.ERROR_ORDER_CODE,"order is null");

        }
        if(map.containsKey("repeat")){
            return CommonUtil.returnFormatSimp(Constant.ERROR_DUPLICATION_ORDER,"order has exist");
        }


        if(jsonObject.getInteger("isUploadFile")==1 && files != null){
            if(files.isEmpty()){
                map.put("failNum",1);
                return CommonUtil.returnFormat(Constant.FAIL_UPLOAD,"success but failUpload",map);
            }
             num = orderService.uploadFile(files, (String) map.get("orderId"),Constant.STU_FILE,filename);
        }


        if(num != 0){
            map.put("failNum",num);
            return CommonUtil.returnFormat(Constant.FAIL_UPLOAD,"success but failUpload",map);
        }

        return CommonUtil.returnFormat(200,"success",map);

    }

}
