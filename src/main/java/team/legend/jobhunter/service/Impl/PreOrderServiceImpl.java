package team.legend.jobhunter.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.dao.FileDao;
import team.legend.jobhunter.dao.PreOrderDao;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.service.PreOrderService;
import team.legend.jobhunter.utils.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 逻辑要大改
 */

@Slf4j
@Service
public class PreOrderServiceImpl implements PreOrderService {

    @Value("${file.fileUrl}")
    String fileUrl;
    @Value("${file.webFileUrl}")
    String webFileUrl;

    @Autowired
    RedisTemplate<String,PreOrder> preOrderRedisTemplate;
    @Autowired
    IDGenerator idGenerator;
    @Autowired
    PreOrderDao preOrderDao;
    @Autowired
    FileDao fileDao;





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
                String preName = SecretUtil.MD5Encode(originFileName).substring(0, 10);
                String fileName = preName + "." + fileSuffix;
                String fileFullName = preOrderId.substring(0,5)+"/"+fileName;
                String fileFullUrl = fileUrl + fileFullName;
                File dir = new File(fileUrl+preOrderId.substring(0,5));
                dir.mkdirs();

                File savedfile = new File(fileFullUrl);
                try {
                    file.transferTo(savedfile);
                    int num = fileDao.insertFileUrl(preOrderId, webFileUrl+fileFullName, CommonUtil.getNowDate("yyyy-MM-dd HH:mm:ss"),0,originFileName);

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

        Long now = System.currentTimeMillis();
        log.error("-----------------------------{}----------------------------------------",System.currentTimeMillis()-now);
        Map<String,String> res_map = new LinkedHashMap<>();
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
        PreOrder preOrder = preOrderDao.selectAllByTeaIdAndStuId(teaId,stuId);
        log.error("-----------------------------{}----------------------------------------",System.currentTimeMillis()-now);

        //是否超过过期时间
        if(preOrder != null && preOrder.getExpire()-System.currentTimeMillis()/1000< 0){
            //放入失效订单表
            preOrderDao.insertInvaludOrder(preOrder.getPreorder_id(),preOrder.getTea_id(),preOrder.getStu_id(),
                    preOrder.getRealname(),preOrder.getTele(),preOrder.getExperience(),preOrder.getRequirement(),
                    preOrder.getIsonline(),preOrder.getCreate_date(),preOrder.getOrder_type(),preOrder.getPrice(),preOrder.getDiscount());
            int num = preOrderDao.deletePreOrder(preOrder.getPreorder_id());
                //置preOrder为null
            preOrder = null;
            res_map.put("overTime","203");
        }
        log.error("-----------------------------{}----------------------------------------",System.currentTimeMillis()-now);


        if(preOrder == null){

            String service_id = idGenerator.createServiceId(teaId);
            int count = preOrderDao.getCount();
            preOrder_id = idGenerator.createPreOrderId(service_id,count);
                int num = preOrderDao.insertPreOrder(new PreOrder(preOrder_id, service_id, teaId, stuId, realName, tele, experience,
                        requirement, isonline, order_type, price, discount, Constant.PREORDER_EXPIRE_DAY));
            if(num != 1){
                log.error(">>log insert preOrder fail");
                throw new SqlErrorException("插入预订单失败");
            }
            res_map.put("code","201");
        }else{
            //只更新用户的几个字段
            preOrderDao.updatePreOrder(realName,tele,experience,requirement,isonline,price,discount,System.currentTimeMillis());
            res_map.put("code","202");
        }

        preOrder = preOrderDao.selectAllByTeaIdAndStuId(teaId, stuId);

        res_map.put("preOrderId",preOrder.getPreorder_id());

        log.error("-----------------------------{}----------------------------------------",System.currentTimeMillis()-now);
        return res_map;
    }

    @Override
    public int cancelPreOrder(String stuId, String preOrderId) {

        String stuIdInBase = preOrderDao.selectStuIdByPreOrderId(preOrderId);
        if(stuId.equals(stuIdInBase)){
            int num = preOrderDao.deletePreOrder(preOrderId);
            return num;
        }
        log.error("stuId is not equal with stuId in Database or null");
        return 2;
    }


    public static void main(String[] args) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("stuId","123456");
//        map.put("teaId","324325");
//        map.put("orderType","resume");
//        map.put("realName","蒋龙");
//        map.put("tele","123454654");
//        map.put("experience","sdoiashdoiashf");
//        map.put("guidance","大撒大撒安神颗粒的你看受到了看上了看你的");
//        map.put("isonline",1);
//        map.put("price",20000);
//        map.put("discount",98);
//        map.put("isUploadFile",1);
//
//        System.out.println(JSON.toJSONString(map));
        long expire = 1576067217;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expireTime = simpleDateFormat.format(expire*1000);
        Date date = new Date();
        System.out.println(expireTime);
        String date1 = simpleDateFormat.format(date.getTime()/1000);



    }

}
