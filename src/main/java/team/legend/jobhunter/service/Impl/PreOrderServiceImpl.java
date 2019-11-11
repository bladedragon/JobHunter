package team.legend.jobhunter.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.dao.FileDao;
import team.legend.jobhunter.dao.ItemDao;
import team.legend.jobhunter.dao.PreOrderDao;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.model.TimeItem;
import team.legend.jobhunter.service.PreOrderService;
import team.legend.jobhunter.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 逻辑要大改
 */

@Slf4j
@Service
public class PreOrderServiceImpl implements PreOrderService {

    @Value("${file.fileUrl}")
    String fileUrl;

    @Autowired
    RedisTemplate<String,PreOrder> preOrderRedisTemplate;
    @Autowired
    IDGenerator idGenerator;
    @Autowired
    PreOrderDao preOrderDao;
    @Autowired
    FileDao fileDao;

    //TODO 防止订单重复提交

    @Override
    public Map<String, PreOrder> createPreOrder(String service_type,String service_id, String stu_id, String tea_id,TimeItem timeItem) {
        Map<String,PreOrder> res_map = new HashMap<>(10);

        String lockTimestamp = String.valueOf(System.currentTimeMillis()+ Constant.LOCK_EXPIRE_TIME);

//        redisLockHelper.lock(service_id,lockTimestamp);

        PreOrder preOrder = preOrderRedisTemplate.opsForValue().get(service_id);
        if(preOrder == null){
            //获取ID计算公式等待完善
            String preOrderId = idGenerator.createPreOrderId(service_id,1);
            String orderId = idGenerator.createOrderId(service_id,1);
            //价格计算公式等待完善
//            String price = PriceUtil.getPrice(timeItem.getItem_origin_price(),timeItem.getItem_discount());
//            String currTime = String.valueOf(System.currentTimeMillis()/1000);
//            preOrder = new PreOrder(preOrderId,orderId,service_id,tea_id,stu_id,timeItem.getItem_id(),timeItem.getItem_time(),
//                    price,timeItem.getIsonline(),timeItem.getItem_time_detail(),currTime
//                    ,service_type,System.currentTimeMillis()/1000);
            //插入redis数据库
            preOrderRedisTemplate.opsForValue().set(service_id,preOrder);
            PreOrder preOrder_res = preOrderRedisTemplate.opsForValue().get(service_id);
            res_map.put("success",preOrder_res);

        }else{
            res_map.put("be created!",preOrder);
        }

//        redisLockHelper.unlock(service_id,lockTimestamp);
        return res_map;
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public int uploadFile(List<MultipartFile> files,String preOrderId) throws UploadException {

        int failNum = 0;
        if (null != files && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if(file.getSize()> Constant.MAX_FILE_SIZE){
                    throw new UploadException("exceed max size");
                }
                String originFileName = file.getOriginalFilename();
                String[] strs = originFileName.split("\\.");
                String fileSuffix = strs[1];
                String preName = SecretUtil.MD5Encode(preOrderId).substring(0, 5);
                String fileName = preName + CommonUtil.getNowTime();
                String fileFullName = fileName + "." + fileSuffix;
                String fileFullUrl = fileUrl + preOrderId.substring(0,5)+"/"+fileFullName;
                File dir = new File(fileUrl+preOrderId.substring(0,5));
                dir.mkdirs();
                File savedfile = new File(fileFullUrl);
                try {
                    file.transferTo(savedfile);
                    int num = fileDao.insertFileUrl(preOrderId, fileFullUrl, CommonUtil.getNowDate(),1);

                } catch (IOException e) {
                    e.printStackTrace();
                   log.error(">>log filename:{} upload fail",fileFullName);
                    failNum++;
                }
            }
            return failNum;
        }
        log.error(">>log :file is null");
        throw new UploadException("files upload fail");

    }

    //支付订单前必须先预付款
    //如果重复提交就返回提醒
    //如果订单超时就放入过期表

    @Override
    @Transactional(rollbackFor = SqlErrorException.class)
    public Map<String, String> createPreOrder(JSONObject jsonObject) throws SqlErrorException {
        Map<String,String> res_map = new HashMap<>(3);
        String stuId = jsonObject.getString("stuId");
        String teaId = jsonObject.getString("teaId");
        String realName = jsonObject.getString("realName");
        String tele = jsonObject.getString("tele");
        String experience = jsonObject.getString("experience");
        String requirement = jsonObject.getString("guidance");
        int isonline = jsonObject.getInteger("isonline");
        int price = jsonObject.getInteger("price");
        int discount = jsonObject.getInteger("discount");
        String order_type = jsonObject.getString("orderType");



        //插入待付款订单，存在即更新

        String preOrder_id = null;
        PreOrder preOrder = preOrderDao.selectAll(teaId,stuId);

        if(preOrder == null){
            String service_id = idGenerator.createServiceId(teaId);
            int count = preOrderDao.getCount();
            preOrder_id = idGenerator.createPreOrderId(service_id,count);
                int num = preOrderDao.insertPreOrder(new PreOrder(preOrder_id, service_id, teaId, stuId, realName, tele, experience,
                        requirement, isonline, order_type, price, discount, 3));
            if(num != 1){
                log.error(">>log insert preOrder fail");
                throw new SqlErrorException("插入预订单失败");
            }
            res_map.put("code","201");
        }else{
            //只更新用户的几个字段
            preOrderDao.updatePreOrder(realName,tele,experience,requirement,isonline,price,discount,CommonUtil.getNowDate());
            res_map.put("code","202");
        }

        preOrder = preOrderDao.selectAll(teaId, stuId);

        //是否超过过期时间

        if(preOrder.getExpire()-System.currentTimeMillis()/1000< 0){
            //放入失效订单表
            preOrderDao.insertInvaludOrder(preOrder.getPreorder_id(),preOrder.getTea_id(),preOrder.getStu_id(),
                    preOrder.getRealname(),preOrder.getTele(),preOrder.getExperience(),preOrder.getRequirement(),
                    preOrder.getIsonline(),preOrder.getCreate_date(),preOrder.getOrder_type(),preOrder.getPrice(),preOrder.getDiscount());
            preOrderDao.deletePreOrder(preOrder_id);
            res_map.put("overTime",null);
            return res_map;
        }else{
//            res_map.put("stuId",stuId);
            res_map.put("preOrderId",preOrder.getPreorder_id());
        }

        return res_map;
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("stuId","123456");
        map.put("teaId","324325");
        map.put("orderType","resume");
        map.put("realName","蒋龙");
        map.put("tele","123454654");
        map.put("experience","sdoiashdoiashf");
        map.put("guidance","大撒大撒安神颗粒的你看受到了看上了看你的");
        map.put("isonline",1);
        map.put("price",20000);
        map.put("discount",98);
        map.put("isUploadFile",1);

        System.out.println(JSON.toJSONString(map));

    }

}
