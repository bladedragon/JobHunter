package team.legend.jobhunter.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;
import team.legend.jobhunter.dao.ItemDao;
import team.legend.jobhunter.dao.OrderDao;
import team.legend.jobhunter.dao.PreOrderDao;
import team.legend.jobhunter.dao.TeaDao;
import team.legend.jobhunter.exception.OrderErrorException;
import team.legend.jobhunter.exception.SqlErrorException;
import team.legend.jobhunter.model.DO.OrderDo;
import team.legend.jobhunter.model.Order;
import team.legend.jobhunter.model.PreOrder;
import team.legend.jobhunter.service.OrderService;
import team.legend.jobhunter.utils.Constant;
import team.legend.jobhunter.utils.IDGenerator;
import team.legend.jobhunter.utils.RedisLockHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisTemplate<String, PreOrder> preOrderRedisTemplate;
    @Autowired
    RedisLockHelper redisLockHelper;
    @Autowired
    PreOrderDao preOrderDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    TeaDao teaDao;
    @Autowired
    IDGenerator idGenerator;

    @Override
    public Map<String, Object> createOrder(String tea_id,String stu_id,String service_id,String service_type,int price,
                                          int discount,int isonline,String realname,String tele,String experience,String requirement) throws OrderErrorException, SqlErrorException {
            Map<String,Object> res_map = new HashMap<>(10);

            Order order= null;

       try {
           int count = orderDao.getCount();
           String order_id = idGenerator.createOrderId(service_id, count);
           int num = orderDao.insertOrder(new Order(order_id, tea_id, stu_id, discount, price, service_type, 1, isonline, requirement,
                   experience, tele, realname));
            order = orderDao.selectByOrderId(order_id);

           if (order == null) {
               throw new SqlErrorException("order is null");
           }

           PreOrder preOrder = preOrderDao.selectAll(tea_id,stu_id);
           if(preOrder!= null){
               preOrderDao.deletePreOrder(preOrder.getPreorder_id());
           }
       }catch (Exception e){
           e.printStackTrace();
           log.error(">>log: sql exception");
       }


//        if(preOrder!=null ){
//            int item_isOrdered = itemDao.selectStatus(service_id,preOrder.getItem_id());
//            if(item_isOrdered != 0 ){
//                res_map.put("be ordered",null);
//                redisLockHelper.unlock(order_id,lockTimestamp);
//                return res_map;
//            }
//            Teacher teaDO = teaDao.selectByTeaId(preOrder.getTea_id());
//            orderDao.insertOrder(new OrderDo(order_id,teaDO.getTea_id(),teaDO.getTea_nickname(),teaDO.getTea_img_url(),teaDO.getGender(),
//                    preOrder.getStu_id(),preOrder.getItem_time(),preOrder.getItem_price(),preOrder.getItem_isonline(),
//                    preOrder.getItem_time_detail(),CommonUtil.getNowDate(),preOrder.getOrder_type(),200,CommonUtil.getNowDate(),
//                   null,0));
//            itemDao.updateIsordered(preOrder.getItem_id());
//                OrderDo orderDo= orderDao.selectByOrderId(order_id);
//                if(orderDo == null){
//                    throw new OrderErrorException("生成订单失败");
//            }
                //返回订单逻辑
            //当预订单不存在的时候怎么办？
            //当订单存在的时候怎么办？
            //创造订单必须在创建预订单之后，但是其中加两次锁是否会出现异常情况
            //

//        }else{
//            res_map.put("preorder is null",null);
//
//        }

//        redisLockHelper.unlock(order_id,lockTimestamp);
        return res_map;

    }

    @Override
    public Map<String, Object> createOrder(String preOrderId) {
        return null;
    }

    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd/HH-mm");
        String datetime = simpleDateFormat.format(new Date());
        System.out.println(datetime);
    }
}
