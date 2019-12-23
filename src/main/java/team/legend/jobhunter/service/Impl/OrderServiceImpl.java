package team.legend.jobhunter.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.legend.jobhunter.dao.*;
import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.exception.UploadException;
import team.legend.jobhunter.model.DO.OrderTeaDO;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.model.Teacher;
import team.legend.jobhunter.service.OrderService;
import team.legend.jobhunter.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Value("${file.fileUrl}")
    String fileUrl;
    @Value("${file.webFileUrl}")
    String webFileUrl;
    @Autowired
    PreOrderDao preOrderDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    FileDao fileDao;
    @Autowired
    TeaDao teaDao;
    @Autowired
    IDGenerator idGenerator;


    @Override
    @Transactional(rollbackFor = SqlErrorException.class)
    public Map<String, Object> createOrder(String tea_id,String stu_id,String service_id,String service_type,int price,
                                          int discount,int isonline,String realname,String tele,String experience,String requirement) throws OrderErrorException, SqlErrorException {


        Map<String,Object> res_map = new HashMap<>(10);
        String teaTele = "";
        Order order = orderDao.selectByTeaIdAndStuId(tea_id,stu_id);
            if(order != null){
                log.error(">>log :Order has exist");
                res_map.put("repeat",null);
                return res_map;
            }
       try {

           int count = orderDao.getCount();
           String order_id = idGenerator.createOrderId(null, count);
           Teacher teacher = teaDao.selectByTeaId(tea_id);
           if(teacher == null){
               log.error(">>log: cannot get teaInfo :[{}]",tea_id);
               throw new SqlErrorException("teacher is null");
           }

           teaTele = teacher.getTea_tele();
           int num = orderDao.insertOrder(new Order(order_id, tea_id, stu_id, discount, price, service_type, 1, isonline, requirement,
                   experience, tele, realname,teacher.getTea_nickname(),teacher.getTea_tele(),teacher.getTea_tag(),
           teacher.getTea_img_url(),teacher.getGender(),teacher.getTea_email(),teacher.getTea_description(),teacher.getTea_company(),
                   teacher.getPosition()));

            order = orderDao.selectByOrderId(order_id);

           if (num != 1 || order == null) {
               log.error(">>log: sql exception :order is null ");
               throw new SqlErrorException("order is null");
           }

           //如果有预订单，删除之
           String  preOrderId  = preOrderDao.selectPreOrderId(tea_id,stu_id);
           if(null != preOrderId && !preOrderId.equals("")){
               preOrderDao.deletePreOrder(preOrderId);
               log.info(">>log :delete preOrder :[{}]",preOrderId);
           }

       }catch (Exception e){
           log.error(">>log: sql exception");
           e.printStackTrace();
           throw new SqlErrorException("sql exception");
       }

       res_map.put("orderId",order.getOrder_id());
       res_map.put("teaId",order.getTea_id());
       res_map.put("stuId",order.getStu_id());
       res_map.put("tele",teaTele);
       res_map.put("teaNickname",order.getTea_nickname());

       return res_map;

    }

    @Override
    @Transactional(rollbackFor = SqlErrorException.class)
    public Map<String, Object> createOrder(String preOrderId) throws SqlErrorException {

        String teaTele = "";
        Map<String ,Object> res_map = new HashMap<>(3);

        PreOrder preOrder = preOrderDao.selectAllByPreOrderId(preOrderId);
        if(preOrder == null){
            log.error(">>log :preOrder select is null");
            return null;
        }


        Order order = orderDao.selectByTeaIdAndStuId(preOrder.getTea_id(),preOrder.getStu_id());
        if(order != null){
            log.error(">>log :Order has exist");
            res_map.put("repeat",null);
            return res_map;
        }

        try {
            int count = orderDao.getCount();
            String order_id = idGenerator.createOrderId(preOrder.getService_id(), count);

            Teacher teacher = teaDao.selectByTeaId(preOrder.getTea_id());
            if(teacher == null){
                log.error(">>log: cannot get teaInfo :[{}]",preOrder.getTea_id());
                throw new SqlErrorException("teacher is null");
            }
            log.info("show orderId[{}]",order_id);

            //TODO: 完善价格逻辑，将填写价格和后台老师提供的价格进行匹配，如果价格不匹配器，订单无效


            teaTele = teacher.getTea_tele();
            int num = orderDao.insertOrder(new Order(order_id, preOrder.getTea_id(), preOrder.getStu_id(), preOrder.getDiscount(), preOrder.getPrice(),
                    preOrder.getOrder_type(), 1, preOrder.getIsonline(), preOrder.getRequirement(), preOrder.getExperience(), preOrder.getTele(),
                    preOrder.getRealname(),teacher.getTea_nickname(),teacher.getTea_tele(),teacher.getTea_tag(),
                    teacher.getTea_img_url(),teacher.getGender(),teacher.getTea_email(),teacher.getTea_description(),teacher.getTea_company(),
                    teacher.getPosition()));


            order = orderDao.selectByOrderId(order_id);

            log.info(">>insertOrder's num is [{}]",num);
            if (num != 1 || order == null) {
                log.error(">>log: sql exception :order is null ");
                throw new SqlErrorException("order is null");
            }


            //删除待付款订单
            if(preOrderId != null || !preOrderId.equals("")){
                preOrderDao.deletePreOrder(preOrderId);
                log.info(">>log :delete preOrder :[{}]",preOrderId);
            }

        }catch (Exception e){
            log.error(">>log: sql exception");
            e.printStackTrace();
            throw  new SqlErrorException("sql exception");
        }

        res_map.put("orderId",order.getOrder_id());
        res_map.put("teaId",order.getTea_id());
        res_map.put("stuId",order.getStu_id());
        res_map.put("teaTele",teaTele);
        res_map.put("teaNickname",order.getTea_nickname());
        return res_map;
    }


    @Override
    @Transactional(rollbackFor = IOException.class)
    public int uploadFile(List<MultipartFile> files, String orderId,int isTea,String filename) throws UploadException {

            int failNum = 0;

            for (MultipartFile file : files) {
                if(file.getSize()> Constant.MAX_FILE_SIZE){
                    throw new UploadException("exceed max size");
                }
                String originFileName = file.getOriginalFilename();
                String[] strs = originFileName.split("\\.");
                String fileSuffix = strs[strs.length-1];
                String preName = SecretUtil.MD5Encode(orderId).substring(0, 10);
                String fileName = preName +"." + fileSuffix ;
                String fileFullName = orderId.substring(0,5)+"/"+fileName;
                String fileFullUrl = fileUrl +fileFullName;
                File dir = new File(fileUrl+orderId.substring(0,5));
                dir.mkdirs();
                File savedfile = new File(fileFullUrl);
                try {
                    file.transferTo(savedfile);
                    int num = fileDao.insertFileUrl(orderId, webFileUrl+fileFullName, CommonUtil.getNowDate("yyyy-MM-dd HH:mm:ss"),isTea,filename);

                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(">>log filename:{} upload fail",fileFullName);
                    failNum++;
                }
            }
            return failNum;
    }

    public static void main(String[] args) {

//        Map<String,Object> map = new HashMap<>();
//        map.put("preOrderId","802019111120142514572492369");
//        map.put("stuId","1111");
//        map.put("teaId","11111");
//        map.put("serviceId","sadfsdfsdf");
//        map.put("orderType","resume");
//        map.put("realName","蒋龙");
//        map.put("tele","123454654");
//        map.put("experience","这是一段经历，大概要写好些话");
//        map.put("guidance","这是用户的需求，以及渴望得到的指导");
//        map.put("isonline",1);
//        map.put("price",20000);
//        map.put("discount",98);
//        map.put("isUploadFile",1);
//
//        System.out.println(JSON.toJSONString(map));
    }
}
